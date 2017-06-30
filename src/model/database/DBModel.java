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
public class DBModel extends HashMap<String, Object>{
    protected String NAME;
    
    public String getName(){
        return NAME;
    }
    
}
