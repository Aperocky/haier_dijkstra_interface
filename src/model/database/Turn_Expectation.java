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
public class Turn_Expectation extends DBModel{
    public static final String TID = "tid";
    public static final String EXPECT = "expectation";
    
    public Turn_Expectation(Integer tid){
        NAME = "Turn_Expectation";
        this.put(TID, tid);
    }
    
}
