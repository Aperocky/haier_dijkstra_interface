/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.database;

import java.util.HashMap;

/**
 *
 * @author Feng
 */
public class Turn_Eval extends DBModel {

    public static final String TID = "tid";
    public static final String RISKEVAL = "risk_eval";
    public static final String POSX = "pos_x";
    public static final String POSY = "pos_y";

    // Risk Evaluation Constants
    public static final String HIGHRISKEVAL = "HighRisk";
    public static final String MEDRISKEVAL = "MedRisk";
    public static final String LOWRISKEVAL = "LowRisk";

    public Turn_Eval(Integer tid) {
        NAME = "Turn_Eval";
        this.put(TID, tid);
    }
}
