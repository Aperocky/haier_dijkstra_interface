/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riskawarenesssimulation.Controller;

import algorithm.ScoreCalculator.ScoreBuilder;
import context.Context;
import context.MapPool;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import model.map.Edge;
import alerts.HaierAlert;
import model.map.Map;
import model.map.Node;
import service.cache.CacheService;
import service.parse.ParseService;
import service.stage.StageService;
import view.ColorfulMap;
import view.ColorfulPath;
import view.RiskBudget;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;

import constant.GameConstants;
import static constant.GameConstants.RISK_BUDGET;
import static constant.GameConstants.mStartCol;
import static constant.GameConstants.mStartRow;
import model.database.Game;
import model.database.Turn;
import static model.database.Turn.HIGHCHOICE;
import static model.database.Turn.LOWCHOICE;
import static model.database.Turn.MEDCHOICE;
import static model.database.Turn.STEPBIG;
import static model.database.Turn.STEPMED;
import static model.database.Turn.STEPSMALL;
import model.database.Turn_Confidence;
import model.database.Turn_Control;
import model.database.Turn_Eval;
import model.database.User;
import model.map.path.EdgePath;
import model.map.path.NodePath;
import service.database.DBWriter;
import static service.stage.StageService.BG_IMG_L;
import static service.stage.StageService.DEFAULT_IMAGE_PACKAGE;
import static service.stage.StageService.EVALUATION_PANEL;
import static service.stage.StageService.SUCCESS_PANEL;
import static alerts.HaierAlert.BLANK_CHOICE;
import java.util.Iterator;
import javafx.scene.paint.Color;
import service.planner.ChooseStepTask;
import exception.DBException;
import exception.MapUnavailableException;
import service.stage.CountTask;

/**
 * Responsible for displaying the game window and control panel
 *
 * @author Feng
 */
public class MapController implements Initializable, GameConstants {

    // Cache Values
    private static ArrayList<Integer> mStepSizes;
    private static ArrayList<Integer> mRiskLevels;

    // Services
    private final ParseService mParseService;
    private StageService mSceneService;
    private final CacheService mCacheService;
    private final DBWriter mDBWriter;

    // UI
    private ColorfulMap mColorfulMap;
    private ColorfulPath mColorfulPath;

    // Model/Game related
    private int[][] mDangers;
    private Node mPlannedStopNode;
    private Node mCurrentStartNode;
    private Node mDestinationNode;

    // Constraints related
    private RiskBudget mRiskBudget;
    private Double mRiskConsumption;
    private Double mCurrAttemptConsumption;
    private Double mPrevAttemptConsumption;
    private Integer mSurfacing;
    private Integer mDeviate;

    // Global variable
    private Integer mTimeStep;
    private Integer mRiskLevel;
    private User mUser;
    private Game mGame;
    private ScoreBuilder mScoreBuilder;

    private final boolean mIsControlGroup;
    private final boolean mIsObserveGroup;

    // Clean up
    private Shape mPrevStart;
    private Shape mPrevPlanned;
    private Group mPrevPathGroup;
    private Group mCurrPlanGroup;
    private Group mPrevPlanGroup;
    private Collection<Edge> mCurrPlanEdges;
    private Collection<Edge> mPrevPlanEdges;

    @FXML
    private JFXButton ExecuteBtn;
    @FXML
    private Pane canvas;
    @FXML
    private AnchorPane AnchorBG;
    @FXML
    private JFXRadioButton btn1;
    @FXML
    private JFXRadioButton btn2;
    @FXML
    private JFXRadioButton btn3;
    @FXML
    private JFXRadioButton btn4;
    @FXML
    private JFXRadioButton btn5;
    @FXML
    private JFXRadioButton btn6;
    @FXML
    private JFXRadioButton btn7;
    @FXML
    private JFXRadioButton btn8;
    @FXML
    private JFXRadioButton btn9;
    @FXML
    private JFXRadioButton nonControlBtn1;
    @FXML
    private JFXRadioButton nonControlBtn2;
    @FXML
    private JFXRadioButton nonControlBtn3;
    @FXML
    private ToggleGroup risktime;
    @FXML
    private ToggleGroup riskonly;
    @FXML
    private JFXRadioButton riskevalBtn1;
    @FXML
    private JFXRadioButton riskevalBtn2;
    @FXML
    private JFXRadioButton riskevalBtn3;
    @FXML
    private JFXButton SaveAssessBtn;
    @FXML
    private ToggleGroup riskeval;
    @FXML
    private Label surfacinglabel;
    @FXML
    private Pane riskbar;
    @FXML
    private Label warningLabel;
    @FXML
    private Group ObserveGroup;
    @FXML
    private Group ControlGroup;
    @FXML
    private Group nonControlGroup;
    @FXML
    private Group RiskEvalGroup;

    public MapController() {
        mParseService = new ParseService();
        mColorfulPath = new ColorfulPath();
        mColorfulMap = new ColorfulMap();

        mDeviate = -1;
        mRiskConsumption = 0d;
        mCurrAttemptConsumption = 0d;
        mPrevAttemptConsumption = 0d;
        mSurfacing = SURFACING;
        // Increasing Step Size
        mStepSizes = new ArrayList<>();
        mStepSizes.add(SMALLSTEP);
        mStepSizes.add(MEDSTEP);
        mStepSizes.add(BIGSTEP);
        // Increasing Risk Level
        mRiskLevels = new ArrayList<>();
        mRiskLevels.add(LOWRISK);
        mRiskLevels.add(MEDRISK);
        mRiskLevels.add(HIGHRISK);

        mCurrPlanGroup = new Group();
        mPrevPlanGroup = new Group();
        mPrevPathGroup = new Group();
        mCurrentStartNode = new Node(mStartRow, mStartCol);
        mDestinationNode = new Node(mEndRow, mEndCol);

        // Set Context variables
        mUser = Context.getInstance().getUser();
        mGame = Context.getInstance().getGame();
        Integer userID = (Integer) mUser.get(User.UID);
        Integer groupID = (Integer) mUser.get(User.CONDITIONID);
        Integer gameID = (Integer) mGame.get(Game.GID);
        mIsControlGroup = (groupID == 3 || groupID == 4);
        mIsObserveGroup = (groupID == 2 || groupID == 4);

        mDBWriter = Context.getInstance().getDBWriter();

        mScoreBuilder = Context.getInstance().getScoreBuilder();
        Turn mTurn = new Turn(userID + gameID);
        mTurn.put(Turn.GID, gameID);
        mTurn.put(Turn.UID, userID);
        Context.getInstance().setTurn(mTurn);

        try {
            mDangers = mParseService.
                    getDangers(MapPool.getInstance().getMap((Integer) mGame.get(Game.MAPID)));
        } catch (MapUnavailableException ex) {
             HaierAlert.getAlert(HaierAlert.MAP_UNAVAILABLE).showAndWait();
        }
        // Load the paths cache for the first time
        mCacheService = new CacheService(mDangers, mCurrentStartNode, mDestinationNode,
                mStepSizes, mRiskLevels);
        mScoreBuilder.minDistance(mCacheService.getMinimumPathLength());
        //System.out.println(MapPool.getInstance().getAvailableKeys().size());
    }
    
    @FXML
    private void onEval1(ActionEvent event) {
        writeEvalChoice(Turn_Eval.LOWRISKEVAL);
    }

    @FXML
    private void onEval2(ActionEvent event) {
        writeEvalChoice(Turn_Eval.MEDRISKEVAL);
    }

    @FXML
    private void onEval3(ActionEvent event) {
        writeEvalChoice(Turn_Eval.HIGHRISKEVAL);
    }

    /**
     * Insert new entry in Turn_Eval table
     *
     * @param riskeval
     */
    private void writeEvalChoice(String riskeval) {
//        if (!Context.getInstance().isDBEnabled()) {
//            return;
//        }
//        try {
            Integer tid = Context.getInstance().getTurn().incrementTid();

//            mDBWriter.writeModel(Context.getInstance().getTurn());
            mDBWriter.writeRecord(Context.getInstance().getTurn());

            Turn_Eval eval = new Turn_Eval(tid);
            eval.put(Turn_Eval.RISKEVAL, riskeval);
            eval.put(Turn_Eval.POSX, mCurrentStartNode.getColumn());
            eval.put(Turn_Eval.POSY, mCurrentStartNode.getRow());
//            mDBWriter.writeModel(eval);
            mDBWriter.writeRecord(eval);
//        } catch (DBException e) {
//            e.printStackTrace();
//        }

    }

    private void showControlGroup(boolean show) {
        ControlGroup.setVisible(mIsControlGroup && show);
        nonControlGroup.setVisible(!mIsControlGroup && show);
        ExecuteBtn.setVisible(show);
    }

    private void showAssessGroup(boolean show) {
        RiskEvalGroup.setVisible(show);
    }

    @FXML
    private void onClick1(ActionEvent event) {
        mRiskLevel = HIGHRISK;
        mTimeStep = SMALLSTEP;
        showAttemptedStepPath();
        writeControlChoice(HIGHCHOICE, STEPSMALL);
    }

    @FXML
    private void onClick2(ActionEvent event) {
        mRiskLevel = HIGHRISK;
        mTimeStep = MEDSTEP;
        showAttemptedStepPath();
        writeControlChoice(HIGHCHOICE, STEPMED);
    }

    @FXML
    private void onClick3(ActionEvent event) {
        mRiskLevel = HIGHRISK;
        mTimeStep = BIGSTEP;
        showAttemptedStepPath();
        writeControlChoice(HIGHCHOICE, STEPBIG);
    }

    @FXML
    private void onClick4(ActionEvent event) {
        mRiskLevel = MEDRISK;
        mTimeStep = SMALLSTEP;
        showAttemptedStepPath();
        writeControlChoice(MEDCHOICE, STEPSMALL);
    }

    @FXML
    private void onClick5(ActionEvent event) {
        mRiskLevel = MEDRISK;
        mTimeStep = MEDSTEP;
        showAttemptedStepPath();
        writeControlChoice(MEDCHOICE, STEPMED);
    }

    @FXML
    private void onClick6(ActionEvent event) {
        mRiskLevel = MEDRISK;
        mTimeStep = BIGSTEP;
        showAttemptedStepPath();
        writeControlChoice(MEDCHOICE, STEPBIG);
    }

    @FXML
    private void onClick7(ActionEvent event) {
        mRiskLevel = LOWRISK;
        mTimeStep = SMALLSTEP;
        showAttemptedStepPath();
        writeControlChoice(LOWCHOICE, STEPSMALL);
    }

    @FXML
    private void onClick8(ActionEvent event) {
        mRiskLevel = LOWRISK;
        mTimeStep = MEDSTEP;
        showAttemptedStepPath();
        writeControlChoice(LOWCHOICE, STEPMED);
    }

    @FXML
    private void onClick9(ActionEvent event) {
        mRiskLevel = LOWRISK;
        mTimeStep = BIGSTEP;
        showAttemptedStepPath();
        writeControlChoice(LOWCHOICE, STEPBIG);
    }

    @FXML
    private void onNonControClick1(ActionEvent event) {
        mRiskLevel = HIGHRISK;
        showProperStepSize();
        writeControlChoice(HIGHCHOICE, "NULL");
    }

    @FXML
    private void onNonControClick2(ActionEvent event) {
        mRiskLevel = MEDRISK;
        showProperStepSize();
        writeControlChoice(MEDCHOICE, "NULL");
    }

    @FXML
    private void onNonControClick3(ActionEvent event) {
        mRiskLevel = LOWRISK;
        showProperStepSize();
        writeControlChoice(LOWCHOICE, "NULL");
    }

    private void showProperStepSize() {
        ChooseStepTask chooseTask = new ChooseStepTask(mCacheService, mRiskLevel,
                RISK_BUDGET - mRiskConsumption, mSurfacing);
        chooseTask.run();
        chooseTask.setOnSucceeded(evt -> {
            mTimeStep = chooseTask.getValue();
            System.out.println("StepChooser chooses: " + mTimeStep);
            showAttemptedStepPath();
        });
    }

    /**
     * Insert new entry into Turn_Control table
     *
     * @param risk
     * @param step
     */
    private void writeControlChoice(String risk, String step) {
//        if (!Context.getInstance().isDBEnabled()) {
//            return;
//        }
//        try {
            Integer tid = Context.getInstance().getTurn().incrementTid();

//            mDBWriter.writeModel(Context.getInstance().getTurn());
            mDBWriter.writeRecord(Context.getInstance().getTurn());

            Boolean samepath = checkSamePath(mPrevPlanEdges, mCurrPlanEdges);
            Turn_Control control = new Turn_Control(tid);
            control.put(Turn_Control.RISK, risk);
            control.put(Turn_Control.STEP, step);
            control.put(Turn_Control.POSX, mCurrentStartNode.getColumn());
            control.put(Turn_Control.POSY, mCurrentStartNode.getRow());
            control.put(Turn_Control.ATMP_RISK_REN, (Double) (RISK_BUDGET - mCurrAttemptConsumption));
            control.put(Turn_Control.CURR_RISK_REN, (Double) (RISK_BUDGET - mRiskConsumption));
            control.put(Turn_Control.SURFACE_RMN, mSurfacing);
            control.put(Turn_Control.SAME_PATH, samepath);
//            mDBWriter.writeModel(control);
            mDBWriter.writeRecord(control);
//        } catch (DBException e) {
//            HaierAlert.getAlert(HaierAlert.DATABASE_ERROR).showAndWait();
//        }
    }

    @FXML
    private void onExecuteAction(ActionEvent event) throws IOException, DBException {
        if (mIsControlGroup
                && !selected(risktime, HaierAlert.BLANK_CHOICE)) {
            return;
        }
        if (!mIsControlGroup
                && !selected(riskonly, HaierAlert.BLANK_CHOICE)) {
            return;
        }
        cleanSelection();
        showAssessGroup(true);
        cleanStep();
        NodePath executeNodeStepPath = mCacheService.getNodePath(
                mRiskLevel, mTimeStep);
        EdgePath executeEdgeStepPath = mCacheService.getEdgePath(
                mRiskLevel, mTimeStep);
        mDeviate = executeNodeStepPath.deviate();
        storeDeviate(mDeviate);
        mScoreBuilder.addDistance(executeEdgeStepPath.length());
        updateCurrentNode(executeNodeStepPath);
        mPrevStart = mColorfulMap.drawCurrent(mCurrentStartNode);
        mPrevPlanned = mColorfulMap.drawPlanned(mPlannedStopNode);
        mRiskConsumption = mCurrAttemptConsumption;
        mSurfacing--;
        // Cache all possible planned paths
        // Update UI
        refresh();
        showAssessGroup(false);
        showControlGroup(false);
        showEvaluationPanel();
    }

    private void storeDeviate(Integer dev) throws DBException {
        Turn_Confidence confidence = new Turn_Confidence(0);
        confidence.put(Turn_Confidence.DEVIATE, dev);
        Context.getInstance().setConfidence(confidence);
    }

    /**
     * Show user a progress bar indicating that submarine is traveling
     * underwater Refresh path information in the cache Check for game status
     * and update the user interface
     */
    private void refresh() {
        // Show user a progress bar to avoid UI lagging
        CountTask cacheTask = new CountTask("Submarine is travelling... ");
        Stage progressStage = mSceneService.showProgressBar(cacheTask);
        cacheTask.setOnSucceeded((WorkerStateEvent t) -> {
            try {
                progressStage.close();;
                // Check game status
                if (isCollided(mCurrentStartNode)) {
                    Context.getInstance().getGame().put(Game.RESULT, FAILURE);
                    Optional<ButtonType> result = HaierAlert.getAlert(HaierAlert.COLLISION).showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        showSuccessPanel();
                    }
                    return;
                }
                if (mSurfacing <= 0) {
                    disableSmallSteps();
                }
                if (mCurrentStartNode.equals(mDestinationNode)) {
                    Context.getInstance().getGame().put(Game.RESULT, SUCCESS);
                    if (mRiskConsumption > RISK_BUDGET) {
                        mScoreBuilder.excessRisk(mRiskConsumption - RISK_BUDGET);
                    }
                    if (mSurfacing < 0) {
                        mScoreBuilder.excessSurface(-mSurfacing);
                    }
                    showSuccessPanel();
                }
                // Update the UI 
                canvas.getChildren().add(mPrevPlanned); // Draw the planned Node
                canvas.getChildren().add(mPrevStart); // Draw the current Node
                updateRiskBudgetBar(mRiskConsumption, mRiskConsumption, mRiskConsumption);
                updateSurfacingBudget(mSurfacing);
                showAssessGroup(true);
                showControlGroup(false);
            } catch (IOException ex) {
                Logger.getLogger(MapController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Heavylifting cache refreshing in the worker thread
        Runnable task = () -> {
            mCacheService.refreshCache(mCurrentStartNode);
        };
        new Thread(task).start();
    }

    private void disableSmallSteps() {
        btn1.setDisable(true);
        btn2.setDisable(true);
        btn4.setDisable(true);
        btn5.setDisable(true);
        btn7.setDisable(true);
        btn8.setDisable(true);
    }

    /**
     * Check whether user has made a selection If yes, record the selection
     *
     * @param tg
     * @param type
     */
    private boolean selected(ToggleGroup tg, String type) {
        if (tg.getSelectedToggle() == null) {
            HaierAlert.getAlert(BLANK_CHOICE).showAndWait();
            return false;
        }
        return true;
    }
    
    private void cleanSelection(){
        if (mIsControlGroup && risktime.getSelectedToggle() != null) {
            risktime.getSelectedToggle().setSelected(false);
        } else if (riskonly.getSelectedToggle() != null) {
            riskonly.getSelectedToggle().setSelected(false);
        }
        if(riskeval != null && riskeval.getSelectedToggle() != null)
            riskeval.getSelectedToggle().setSelected(false);
    }

    private boolean isCollided(Node current) {
        for (int[] danger : mDangers) {
            if (current.getRow() == danger[0] && current.getColumn() == danger[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clean all previously drawn paths and current node
     */
    private void cleanStep() {
        canvas.getChildren().removeAll(mCurrPlanGroup, mPrevPlanGroup, mPrevStart, mPrevPlanned);
        mCurrPlanGroup.getChildren().clear();
        mPrevPlanGroup.getChildren().clear();
        if (mCurrPlanEdges != null) {
            mCurrPlanEdges = null;
        }
        if (mPrevPlanEdges != null) {
            mPrevPlanEdges = null;
        }
        mPrevStart = null;
        warningLabel.setVisible(false);
    }

    // Update the currentStartNode
    private void updateCurrentNode(NodePath nodepath) {
        if (nodepath.length() == mTimeStep + 1) {
            mCurrentStartNode = nodepath.nodeAt(mTimeStep);
            mPrevStart = mColorfulMap.drawCurrent(mCurrentStartNode);
        } else {
            // We have reached the destination.
            mCurrentStartNode = mDestinationNode;
        }
    }

    /**
     * Clear the previously drawn paths and draw two attempted paths in
     * different colors; Indicate the use of risk budget in the budget bar; Show
     * SurfacingOut warning if instructed
     *
     *
     */
    private void showAttemptedStepPath() {
        // Clear all previous attempted paths
        canvas.getChildren().removeAll(mPrevPlanGroup, mCurrPlanGroup);
        mPrevPlanGroup.getChildren().clear();
        mCurrPlanGroup.getChildren().clear();
        // Replace previous edges with current edges
        mPrevPlanEdges = mCurrPlanEdges;
        if (mColorfulPath.drawPrevAttemptPath(mPrevPlanEdges) != null) {
            mPrevPlanGroup.getChildren().addAll(mColorfulPath.drawPrevAttemptPath(mPrevPlanEdges));
        }
        mCurrPlanEdges = mCacheService.getEdgePath(
                mRiskLevel, mTimeStep).getEdges();

        List<Node> UncertainNodes = new ArrayList<>();
        UncertainNodes.add((Node) mCacheService.getUncertainNode(mRiskLevel, SINGLE_STEP * 1));
        UncertainNodes.add((Node) mCacheService.getUncertainNode(mRiskLevel, SINGLE_STEP * 2));
        UncertainNodes.add((Node) mCacheService.getUncertainNode(mRiskLevel, SINGLE_STEP * 3));
        mCurrPlanGroup.getChildren().addAll(mColorfulPath.drawCurrAttemptPath(mCurrPlanEdges));
        mCurrPlanGroup.getChildren().addAll(mColorfulPath.drawUncertainty(mTimeStep, UncertainNodes));
        mPlannedStopNode = (Node) mCacheService.getUncertainNode(mRiskLevel, mTimeStep);

        canvas.getChildren().addAll(mPrevPlanGroup, mCurrPlanGroup);
        mPrevAttemptConsumption = mCurrAttemptConsumption;
        mCurrAttemptConsumption = mRiskConsumption + mCacheService.getNodePath(
                mRiskLevel, mTimeStep).calcRiskConsumption(mRiskLevel);
        updateRiskBudgetBar(mRiskConsumption, mCurrAttemptConsumption,
                mPrevAttemptConsumption);

        // Show warning for potential out of surfacing
        int surfaceNeed = getSurfacePrediction(mRiskLevel, mTimeStep);
        warningLabel.setText("Warning:\nTake at least " + surfaceNeed
                + " surfacings to arrive.\n"
                + "Will run out of Surfacing budget!");
        warningLabel.setVisible(surfaceNeed > mSurfacing && mSurfacing > 0);
    }
    
    
    public int getSurfacePrediction(int risk, int currentstep) {
        return (mCacheService.getFullEdgePath(risk).length() - currentstep) / BIGSTEP;
    }

    private boolean checkSamePath(Collection<Edge> edgesA, Collection<Edge> edgesB) {
        if (edgesA == null || edgesB == null) {
            return false;
        }
        if (edgesA.size() != edgesB.size()) {
            return false;
        }
        Iterator<Edge> aiter = edgesA.iterator();
        Iterator<Edge> biter = edgesB.iterator();
        while (aiter.hasNext()) {
            if (!aiter.next().equals(biter.next())) {
                return false;
            }
        }
        return true;
    }

    // Draw the background node and edges along with dangerous blocks on Canvas
    private void drawDangerousMap() {
        Map map = new Map(0);
        map.updateDangers(mDangers);
        List<Node> arrayNodes = Arrays.asList(map.getNodes());

        mPrevStart = mColorfulMap.drawCurrent(mCurrentStartNode);
        canvas.getChildren().addAll(mColorfulMap.drawMap(arrayNodes));
        canvas.getChildren().addAll(mColorfulMap.drawDestination(mDestinationNode),
                mPrevStart);
    }

    // Draw corresponding risk budget bar and indicator
    // All risks are greater than or equal to zero
    private void updateRiskBudgetBar(double currRisk, double attemptRisk,
            double prevAttemptRisk) {
        riskbar.getChildren().clear();
        double currRatio = getRiskRatio(currRisk);
        double atmpRatio = getRiskRatio(attemptRisk);
        double prevAtmptRatio = getRiskRatio(prevAttemptRisk);
        Color bgColor = currRatio >= RISK_ALERT_THRESHOLD ? ColorfulPath.ALERTCOLOR : ColorfulPath.BGCOLOR;
        riskbar.getChildren().add(mRiskBudget.getBackgroundRect(bgColor));

        riskbar.getChildren().addAll(
                mRiskBudget.getPrevAttemptRect(prevAtmptRatio),
                mRiskBudget.getCurrAttemptRect(atmpRatio),
                mRiskBudget.getCurrRect(currRatio));
        riskbar.getChildren().addAll(
                mRiskBudget.getPrevAttemptTri(prevAtmptRatio),
                mRiskBudget.getCurrAttemptTri(atmpRatio),
                mRiskBudget.getCurrTri(currRatio));
        if (currRatio == 1) {
            riskbar.getChildren().clear();
            riskbar.getChildren().add(mRiskBudget.getBackgroundRect(ColorfulPath.DEPLETECOLOR));
        }

    }

    private double getRiskRatio(double risk) {
        if (risk > RISK_BUDGET) {
            risk = RISK_BUDGET;
        }
        return risk / RISK_BUDGET;
    }

    private void updateSurfacingBudget(int Surfacing) {
        surfacinglabel.setText("Surfacing " + Surfacing);
        if (mSurfacing == 0) {
            surfacinglabel.setTextFill(Color.RED);
        }

    }

    private void showSuccessPanel() throws IOException {
        mSceneService.closeStage((Stage) canvas.getScene().getWindow());
        mSceneService.showStage(SUCCESS_PANEL);
    }

    private void showEvaluationPanel() throws IOException {
        mSceneService.showStage(EVALUATION_PANEL);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mSceneService = Context.getInstance().getSceneService();

        mRiskBudget = new RiskBudget((int) riskbar.getPrefWidth(), (int) riskbar.getPrefHeight());

        BackgroundImage myBG = new BackgroundImage(new Image(DEFAULT_IMAGE_PACKAGE
                .concat(BG_IMG_L)),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        AnchorBG.setBackground(new Background(myBG));
        ExecuteBtn.setOnAction(event -> {
        	System.out.println("Execute Button Clicked");
            try {
                this.onExecuteAction(event);
            } catch (IOException | DBException ex) {
                ex.printStackTrace();
            }
        });

        SaveAssessBtn.setOnAction(event -> {
            if (riskeval.getSelectedToggle() == null) {
                HaierAlert.getAlert(HaierAlert.BLANK_CHOICE).showAndWait();
                return;
            }
            showControlGroup(true);
            showAssessGroup(false);
        });

        btn1.setOnAction(
                (event) -> this.onClick1(event));
        btn2.setOnAction(
                (event) -> this.onClick2(event));
        btn3.setOnAction(
                (event) -> this.onClick3(event));
        btn4.setOnAction(
                (event) -> this.onClick4(event));
        btn5.setOnAction(
                (event) -> this.onClick5(event));
        btn6.setOnAction(
                (event) -> this.onClick6(event));
        btn7.setOnAction(
                (event) -> this.onClick7(event));
        btn8.setOnAction(
                (event) -> this.onClick8(event));
        btn9.setOnAction(
                (event) -> this.onClick9(event));

        riskevalBtn1.setOnAction(
                (event) -> this.onEval1(event));
        riskevalBtn2.setOnAction(
                (event) -> this.onEval2(event));
        riskevalBtn3.setOnAction(
                (event) -> this.onEval3(event));

        nonControlBtn1.setOnAction(
                (event) -> {
                    this.onNonControClick1(event);
                });
        nonControlBtn2.setOnAction(
                (event) -> {
                    this.onNonControClick2(event);
                });
        nonControlBtn3.setOnAction(
                (event) -> {
                    this.onNonControClick3(event);
                });
        changeColor(btn1);
        changeColor(btn2);
        changeColor(btn3);
        changeColor(btn4);
        changeColor(btn5);
        changeColor(btn6);
        changeColor(btn7);
        changeColor(btn8);
        changeColor(btn9);
        changeColor(riskevalBtn1);
        changeColor(riskevalBtn2);
        changeColor(riskevalBtn3);
        changeColor(nonControlBtn1);
        changeColor(nonControlBtn2);
        changeColor(nonControlBtn3);
        ControlGroup.setVisible(false);
        nonControlGroup.setVisible(false);
        ObserveGroup.setVisible(mIsObserveGroup);
        warningLabel.setVisible(
                false);
        ExecuteBtn.setVisible(false);

        drawDangerousMap();
        updateRiskBudgetBar(mRiskConsumption, mPrevAttemptConsumption,
                mCurrAttemptConsumption);
        updateSurfacingBudget(mSurfacing);
    }

    private void changeColor(JFXRadioButton button) {
        button.setUnSelectedColor(Color.WHITE);
        button.setSelectedColor(Color.ORANGE);
    }
}
