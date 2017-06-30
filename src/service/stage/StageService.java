/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.stage;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Switch between different scenes
 *
 * @author Feng
 */
public class StageService {

    public static final String DEFAULT_SCENE_PACKAGE = "/riskawarenesssimulation/Scene/";
    public static final String DEFAULT_IMAGE_PACKAGE = "/image/";
    public static final String SUCCESS_PANEL = "SuccessPanel.fxml";
    public static final String EVALUATION_PANEL = "EvaluationPanel.fxml";
    public static final String MAP_CANVAS = "MapCanvas.fxml";
    public static final String COMPLETE_PANEL = "CompletePanel.fxml";
    public static final String WELCOME_PANEL = "WelcomePanel.fxml";
    public static final String BG_IMG_L = "frosted.png";
    public static final String BG_IMG_S = "SuccessBG.png";

    public StageService() {
    }

    public void showStage(String stage) throws IOException {
        Stage mStage = new Stage();
        mStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().
                getResource(DEFAULT_SCENE_PACKAGE.concat(stage)));
        mStage.setScene(new Scene(root));
        mStage.show();
    }

    public void closeStage(Stage stage) throws IOException {
        if (stage == null) {
            return;
        }
        stage.close();
    }

    /**
     * Independent utility method that can show a progress bar in between scenes
     *
     * @param progressBarStage
     * @param countTask
     */
    public Stage showProgressBar(CountTask countTask) {
        Stage progressBarStage = new Stage(StageStyle.UNDECORATED);
        final double wndwWidth = 500.0d;
        Label updateLabel = new Label("");
        updateLabel.setPrefWidth(wndwWidth);
        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(wndwWidth);

        VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(20));
        updatePane.setSpacing(20.0d);
        updatePane.getChildren().addAll(updateLabel, progress);

        progressBarStage.setScene(new Scene(updatePane));
        progressBarStage.show();

        progress.progressProperty().bind(countTask.progressProperty());
        updateLabel.textProperty().bind(countTask.messageProperty());

        progressBarStage.show();
        countTask.setOnSucceeded((WorkerStateEvent t) -> {
            progressBarStage.close();
        });
        new Thread(countTask).start();
        return progressBarStage;
    }

}
