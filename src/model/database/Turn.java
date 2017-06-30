/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.database;

import java.util.HashMap;

/**
 * Each round of the game is treated as an object and has an id.
 * @author Feng
 */
public class Turn extends DBModel{
    public static final String HIGHEVAL = "high";
    public static final String MEDEVAL = "medium";
    public static final String LOWEVAL = "low";

    public static final String HIGHCHOICE = "high";
    public static final String MEDCHOICE = "medium";
    public static final String LOWCHOICE = "low";

    public static final String STEPBIG = "big";
    public static final String STEPMED = "med";
    public static final String STEPSMALL = "small";
    
//    private Integer Tid;
//    private Integer Gid;
//    private Integer Uid;
//    private Integer Deviate;
    
    public static final String TID = "tid";
    public static final String GID = "gid";
    public static final String UID = "uid";


    public Turn(Integer startTid){
        NAME = "Turn";
        this.put(TID, startTid);
    }
    
    public Integer incrementTid(){
        Integer id;
        if(!this.containsKey(TID))
            id = 1;
        else 
            id = (Integer) this.get(TID)+ 1;
        this.put(TID, id);
        return id;
    }
//    public void setTid(int tid) {
//        Tid = tid;
//    }
//
//    public int getTid() {
//        return Tid;
//    }
//
//    public Integer getGid() {
//        return Gid;
//    }
//
//    public void setGid(Integer Gid) {
//        this.Gid = Gid;
//    }
//
//    public Integer getUid() {
//        return Uid;
//    }
//
//    public void setUid(Integer Uid) {
//        this.Uid = Uid;
//    }
    
    
//    public void setDeviate(int d){
//        Deviate = d;
//    }
//    
//    public int getDeviate(){
//        return Deviate;
//    }

}
