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
public class Turn_Control extends DBModel{
    public static final String TID = "tid";
    public static final String RISK = "risk_choice";
    public static final String STEP = "step_choice";
    public static final String POSX = "pos_x";
    public static final String POSY = "pos_y";
    public static final String ATMP_RISK_REN = "atmp_risk_remain";
    public static final String CURR_RISK_REN = "curr_risk_remain";
    public static final String SURFACE_RMN = "surface_remain";
    public static final String SAME_PATH = "same_path";
    
    public Turn_Control(Integer tid){
        NAME = "Turn_Control";
        this.put(TID, tid);
    }
}
