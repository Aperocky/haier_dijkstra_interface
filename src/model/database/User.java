/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.database;

import java.util.HashMap;

/**
 * Each player corresponds to a unique user with a user id and group id.
 * @author Feng
 */
public class User extends DBModel{
    public static final String UID = "uid";
    public static final String CONDITIONID = "condition_id";  
    // 1: w/o Control+ w/o Observe
    // 2: w/o Control+ w/ Observe
    // 3: w/ Control+ w/o Observe
    // 4: w/ Control+ w/ Observe
    
    public User(Integer uid, Integer conditionid){
        NAME = "User";
        this.put(UID, uid);
        this.put(CONDITIONID, conditionid);
    }
    
}
