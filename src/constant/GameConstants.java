/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constant;

import model.map.Map;
import riskawarenesssimulation.Controller.MapController;

/**
 * A set of game related constants for reference,
 * could be incorporated in the game environment
 * @author Feng
 */
public interface GameConstants {

    // Number of nodes per row
    public static final Integer PPR = 36; //25 || 36
    // Length of the space between nodes
    public static final Integer SPACING = 13; // 28 || 20
    // Surfacing Budget
    public static final Integer SURFACING = 6;
    // Risk Budget
    public static final Double RISK_BUDGET = 0.055;
    // Number of nodes per small step
    public static final Integer SINGLE_STEP = 3;
    // Score calculation scaling factor
    public static final Integer SCORE_SCALE = 25;
    // Risk Budget Bar alert threshold
    public static final Double RISK_ALERT_THRESHOLD = 0.8;
    // For group without controlability
    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
    
    public static final int mStartCol = 1;
    public static final int mStartRow = Map.PPR - 2;
    public static final int mEndCol = Map.PPR - 2;
    public static final int mEndRow = 1;

    
    public static final String FAILURE = "Failure";
    public static final String SUCCESS = "Success";
    
    // Control Constants
    public static final int BIGSTEP = MapController.SINGLE_STEP * 3;
    public static final int MEDSTEP = MapController.SINGLE_STEP * 2;
    public static final int SMALLSTEP = MapController.SINGLE_STEP * 1;
    public static final int HIGHRISK = (int) Math.pow(Map.SPACING, 1.6); // 3.0
    public static final int MEDRISK = (int) Math.pow(Map.SPACING, 3.0); // 3.3
    public static final int LOWRISK = (int) Math.pow(Map.SPACING, 5.0); // 3.6

}
