/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riskawarenesssimulation;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Feng
 */
public class RiskAwareApp extends Application{
     /**
     * Starts the primary stage for this application, 
     * onto which the application scene can be set.
     * Also loads the object hierarchy from the fxml file
     * via FXMLLoader.
     * 
     * @param stage primary stage
     * @throws Exception Exception
     */    
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Load the map and edge weights");
        Parent root = FXMLLoader.load(getClass().getResource("/riskawarenesssimulation/Scene/WelcomePanel.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
         
    /**
     * Launches application Graph and
     * constructs a new instance Graph
     * which is a subClass of Application.
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
