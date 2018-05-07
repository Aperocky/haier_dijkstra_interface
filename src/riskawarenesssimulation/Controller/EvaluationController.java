/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riskawarenesssimulation.Controller;

import context.Context;
import model.database.Turn_Confidence;
import model.database.Turn_Expectation;
import service.database.DBWriter;
import alerts.HaierAlert;
import static alerts.HaierAlert.BLANK_CHOICE;
import static service.stage.StageService.BG_IMG_S;
import static service.stage.StageService.DEFAULT_IMAGE_PACKAGE;

import com.jfoenix.controls.JFXRadioButton;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Records each round information and presents a questionaire
 *
 * @author Feng
 */
public class EvaluationController implements Initializable {

    private DBWriter mDBWriter;
    private Integer mTrustLevel;
    private Integer mConfidenceLevel;

    @FXML
    private AnchorPane AnchorBG;
    @FXML
    private Button submitBtn;
    @FXML
    private ToggleGroup confidence;
    @FXML
    private ToggleGroup trust;
    @FXML
    private Group AssessGroup;
    @FXML
    private JFXRadioButton confidence1;
    @FXML
    private JFXRadioButton confidence2;
    @FXML
    private JFXRadioButton confidence3;
    @FXML
    private JFXRadioButton confidence4;
    @FXML
    private JFXRadioButton confidence5;
    @FXML
    private JFXRadioButton trust1;
    @FXML
    private JFXRadioButton trust2;
    @FXML
    private JFXRadioButton trust3;
    @FXML
    private JFXRadioButton trust4;
    @FXML
    private JFXRadioButton trust5;

    public EvaluationController(){
        mDBWriter = Context.getInstance().getDBWriter();
    }
    
    private void backToGame() {
        if (confidence.getSelectedToggle() == null || trust.getSelectedToggle() == null) {
            HaierAlert.getAlert(BLANK_CHOICE).showAndWait();
            return;
        }
        mConfidenceLevel = Integer.parseInt(confidence.getSelectedToggle().getUserData().toString());
        mTrustLevel = Integer.parseInt(trust.getSelectedToggle().getUserData().toString());
        writeAssessment(mConfidenceLevel, mTrustLevel);
        AssessGroup.setVisible(false);
        closeCurrentWindow();
    }

    private void writeAssessment(Integer confid, Integer trust) {
//        if (!Context.getInstance().isDBEnabled()) {
//            return;
//        }
//        try {
            Integer tid = Context.getInstance().getTurn().incrementTid();
//
//            mDBWriter.writeModel(Context.getInstance().getTurn());
            mDBWriter.writeRecord(Context.getInstance().getTurn());
            
            Integer dev = (Integer) Context.getInstance().getConfidence().get(Turn_Confidence.DEVIATE);
            Turn_Confidence confidence = new Turn_Confidence(tid);
            confidence.put(Turn_Confidence.DEVIATE, dev);
            confidence.put(Turn_Confidence.CONFIDENCE, confid);
//            mDBWriter.writeModel(confidence);
            mDBWriter.writeRecord(confidence);
            
            Integer tid2 = Context.getInstance().getTurn().incrementTid();
//            mDBWriter.writeModel(Context.getInstance().getTurn());
            mDBWriter.writeRecord(Context.getInstance().getTurn());
            Turn_Expectation expect = new Turn_Expectation(tid2);
            expect.put(Turn_Expectation.TID, tid2);
            expect.put(Turn_Expectation.EXPECT, trust);
//            mDBWriter.writeModel(expect);
            mDBWriter.writeRecord(expect);
//        } catch (DBException e) {
//            e.printStackTrace();
//        }
    }

    private void closeCurrentWindow() {
        Stage gamestage = (Stage) submitBtn.getScene().getWindow();
        gamestage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AssessGroup.setVisible(true);
        BackgroundImage myBG = new BackgroundImage(new Image(DEFAULT_IMAGE_PACKAGE
                .concat("frosted.png")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        AnchorBG.setBackground(new Background(myBG));

        confidence1.setUserData(1);
        confidence2.setUserData(2);
        confidence3.setUserData(3);
        confidence4.setUserData(4);
        confidence5.setUserData(5);
        trust1.setUserData(1);
        trust2.setUserData(2);
        trust3.setUserData(3);
        trust4.setUserData(4);
        trust5.setUserData(5);
        changeColor(trust1);
        changeColor(trust2);
        changeColor(trust3);
        changeColor(trust4);
        changeColor(trust5);
        changeColor(confidence1);
        changeColor(confidence2);
        changeColor(confidence3);
        changeColor(confidence4);
        changeColor(confidence5);

        submitBtn.setOnAction(event -> {
            backToGame();
        });
    }

    private void changeColor(JFXRadioButton button) {
        button.setUnSelectedColor(Color.WHITE);
        button.setSelectedColor(Color.ORANGE);
    }

}
