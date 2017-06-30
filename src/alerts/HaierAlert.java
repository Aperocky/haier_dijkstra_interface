package alerts;

import static constant.GameConstants.DEFAULT_RESOURCE_PACKAGE;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Creates specific type of alert and interrupts the game Requires user
 * interaction to proceed Available types: 1 Blank choices 2 Out of risk
 * budget 3 Out of surfacing budget 4 Collision
 *
 * @author Feng
 */
public class HaierAlert {
    private final static ResourceBundle mAlertResources = 
            ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE.concat("alert/alert"));
    public static final String BLANK_CHOICE = "blankchoice";
    public static final String COLLISION = "collision";
    public static final String EMPTY_CACHE = "cacheempty";
    public static final String INVALID_ID = "invalidid";
    public static final String NO_MAP_CHOSEN = "nomapchosen";
    public static final String MAP_UNAVAILABLE = "mapunavailable";
    public static final String DATABASE_ERROR = "databaseerror"; 


    public static Alert getAlert(String alert) {
        String alertinfo = mAlertResources.getString(alert);
        String title = alertinfo.split("\\$")[0];
        String header = alertinfo.split("\\$")[1];
        String content = alertinfo.split("\\$")[2];
        Alert currAlert = new Alert(AlertType.ERROR);
        currAlert.setTitle(title);
        currAlert.setHeaderText(header);
        currAlert.setContentText(content);
        return currAlert;
    }

}
