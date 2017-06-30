/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riskawarenesssimulation.Controller;

import context.Context;
import context.MapPool;
import model.database.Game;
import model.database.User;
import service.database.DBWriter;
import service.stage.StageService;
import static service.stage.StageService.BG_IMG_S;
import static service.stage.StageService.DEFAULT_IMAGE_PACKAGE;
import static service.stage.StageService.MAP_CANVAS;
import alerts.HaierAlert;
import static alerts.HaierAlert.INVALID_ID;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import exception.DBException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Feng
 */
public class WelcomeController implements Initializable {

    private DBWriter mDBWriter;
    private StageService mSceneService;
    private User mUser;
    private Game mGame;
    private Object[] mMaps;
    private Integer mUserId;
    private Integer mGroupId;

    @FXML
    private AnchorPane AnchorBG;
    @FXML
    private JFXButton BeginBtn;
    @FXML
    private JFXTextField UIDText;
    @FXML
    private JFXTextField GIDText;
    @FXML
    private JFXComboBox MapPicker;

    public WelcomeController() {
        mMaps = intToStr(MapPool.getInstance().getAvailableKeys().toArray());
        mGame = new Game(new Random().nextInt(), 0);
    }

    @FXML
    private void onBeginAction(ActionEvent event) throws IOException, SQLException {
        if (UIDText.getText().isEmpty() || UIDText.getText().isEmpty() || !mGame.keySet().contains(Game.MAPID)) {
            return;
        }
        try {
            mUserId = Integer.parseInt(UIDText.getText());
            mGroupId = Integer.parseInt(GIDText.getText());
        } catch (NumberFormatException e) {
            HaierAlert.getAlert(INVALID_ID).showAndWait();
            return;
        }
        if (mGroupId > 4 || mGroupId < 1) {
            HaierAlert.getAlert(INVALID_ID).showAndWait();
            return;
        }
        mUser = new User(mUserId, mGroupId);
        mGame.put(Game.UID, mUserId);
        Context.getInstance().setUser(mUser);
        Context.getInstance().setGame(mGame);
        // Insert new user in User table
        if (Context.getInstance().isDBEnabled()) {
            try {
                mDBWriter = Context.getInstance().getDBWriter();
                mDBWriter.writeModel(mUser);
            } catch (DBException ex) {
                ex.printStackTrace();
            }
        }

        ((Stage) BeginBtn.getScene().getWindow()).close();
        mSceneService.showStage(MAP_CANVAS);
    }
    

    private String[] intToStr(Object[] ints) {
        String[] strs = new String[ints.length];
        for (int i = 0; i < ints.length; i++) {
            strs[i] = ints[i].toString();
        }
        return strs;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mSceneService = Context.getInstance().getSceneService();
        BackgroundImage myBG = new BackgroundImage(new Image(DEFAULT_IMAGE_PACKAGE
                .concat(BG_IMG_S)),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        AnchorBG.setBackground(new Background(myBG));

        BeginBtn.setOnAction(event -> {
            try {
                this.onBeginAction(event);
            } catch (IOException | SQLException ex) {
            }
        });
        MapPicker.getItems().addAll(mMaps);
        MapPicker.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String old_item, String new_item) {
                mGame.put(Game.MAPID, Integer.parseInt(new_item));
            }
        });

    }

}
