/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riskawarenesssimulation.Controller;

import alerts.HaierAlert;
import algorithm.ScoreCalculator;
import context.Context;
import context.MapPool;
import static constant.GameConstants.FAILURE;
import model.database.Game;
import service.database.DBWriter;
import service.stage.StageService;
import static service.stage.StageService.BG_IMG_S;
import static service.stage.StageService.DEFAULT_IMAGE_PACKAGE;
import static service.stage.StageService.MAP_CANVAS;
import static service.stage.StageService.WELCOME_PANEL;
import com.jfoenix.controls.JFXComboBox;
import exception.DBException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

/**
 * Controls the success/failure panel, responsible for recording last game's 
 * result into database, showing final score and creating new game for the next round
 * @author Feng
 */
public class SuccessController implements Initializable {

    private DBWriter mDBWriter;
    private final StageService mSceneService;
    private final Object[] mLeftMaps;
    private final Game mLastGame;
    private Game mNewGame;
    // TODO: Discuss how to compute the score
    private Integer mScore = 0;

    public SuccessController() throws DBException {
        mDBWriter = Context.getInstance().getDBWriter();
        mSceneService = Context.getInstance().getSceneService();
        mLeftMaps = intToStr(MapPool.getInstance().getAvailableKeys().toArray());
        // Update the status of the Game object in the context
        mLastGame = Context.getInstance().getGame();
        mNewGame = new Game(new Random().nextInt(), (Integer)mLastGame.get(Game.UID));
        ScoreCalculator score = Context.getInstance().getScoreBuilder().build();
        mScore = score.getScore();
        if(mLastGame.get(Game.RESULT).equals(FAILURE))
            mScore = 0;
        mLastGame.put(Game.SCORE, mScore);
        Context.getInstance().setGame(mLastGame);

        // Database Storage
        if (Context.getInstance().isDBEnabled()) {
            mDBWriter.writeModel(mLastGame);
        }
        
    }
    
    private String[] intToStr(Object[] ints){
        String[] strs = new String[ints.length];
        for(int i = 0; i < ints.length; i++){
            strs[i] = ints[i].toString();
        }
        return strs;
    }

    @FXML
    private AnchorPane AnchorBG;
    @FXML
    private Button restartBtn;
    @FXML
    private Button tryagainBtn;
    @FXML
    private Group NextRoundGroup;
    @FXML
    private Group GameFinishedGroup;
    @FXML
    private Group TryAgainGroup;
    @FXML 
    private Label ScoreLabel;
    @FXML
    private JFXComboBox MapPicker;

    @FXML
    private void onNewRoundAction(ActionEvent event) throws IOException {
        if(!mNewGame.keySet().contains(Game.MAPID)){
            HaierAlert.getAlert(HaierAlert.NO_MAP_CHOSEN).showAndWait();
            return;
        }
        if (MapPool.getInstance().hasMap((Integer)mNewGame.get(Game.MAPID))) {
            Context.getInstance().setGame(mNewGame);
            mSceneService.showStage(MAP_CANVAS);
            mSceneService.closeStage((Stage) restartBtn.getScene().getWindow());
        }
    }
    
    @FXML
    private void onRestartAction(ActionEvent event) throws IOException {
        mSceneService.closeStage((Stage) restartBtn.getScene().getWindow());
        if (MapPool.getInstance().hasAvailableMap()) {
            mSceneService.showStage(WELCOME_PANEL);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean tryAgain = Context.getInstance().getGame().get(Game.RESULT).equals(FAILURE);
        boolean moreRounds = MapPool.getInstance().hasAvailableMap();
        NextRoundGroup.setVisible(moreRounds && !tryAgain);
        TryAgainGroup.setVisible(moreRounds && tryAgain);
        GameFinishedGroup.setVisible(!moreRounds);
        if (moreRounds) {
            if (!tryAgain) {
                restartBtn.setOnAction(event -> {
                    try {
                        this.onNewRoundAction(event);
                    } catch (IOException e) {
                    }
                });
            } else {
                tryagainBtn.setOnAction(event -> {
                    try {
                        this.onRestartAction(event);
                    } catch (IOException e) {
                    }
                });
            }
        } else {
            try {
                Context.getInstance().getDBWriter().destroy();
            } catch (DBException ex) {
                ex.printStackTrace();
            }
        }
        ScoreLabel.setText("You scored " + Context.getInstance().getScoreBuilder().build().getScore());
        Context.getInstance().getScoreBuilder().clear();
        BackgroundImage myBG = new BackgroundImage(new Image(DEFAULT_IMAGE_PACKAGE
                .concat(BG_IMG_S)),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        AnchorBG.setBackground(new Background(myBG));

        MapPicker.getItems().addAll(mLeftMaps);
        MapPicker.valueProperty().addListener((ov, old_item, new_item) -> { 
                mNewGame.put(Game.MAPID, Integer.parseInt((String) new_item));
            }    
        );
        
    }

}
