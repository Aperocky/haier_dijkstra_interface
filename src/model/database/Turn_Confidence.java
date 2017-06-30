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
public class Turn_Confidence extends DBModel{
    public static final String TID = "tid";
    public static final String CONFIDENCE = "confidence";
    public static final String DEVIATE = "deviate";
    
    public Turn_Confidence(Integer tid){
        NAME = "Turn_Confidence";
        this.put(TID, tid);
    }
    
    
}
