/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.database;

import java.util.HashMap;

/**
 * Each game is recorded as a single object and has a final result of success or 
 * failure with a score
 * @author Feng
 */
public class Game extends DBModel{
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    
    public static final String GID = "gid";
    public static final String UID = "uid";
    public static final String MAPID = "map_id";
    public static final String RESULT = "result";
    public static final String SCORE = "score";
//    private final int GID;
//    private int UID;
//    private int MapId;
//    private int TID;
//    private String Result;
//    private int Score;
    
//    
//    private int Distance;
//    private double ExcessRisk;
//    private int ExcessSurface;
//    private int MinDist;

    public Game(Integer gid, Integer uid) {
        NAME = "Game";
        this.put(UID, uid);
        this.put(GID, gid);
    }

//    public void setUid(int uid){
//        UID = uid;
//    }
//    
//    public int getUid() {
//        return UID;
//    }
//
//    public int getGid() {
//        return GID;
//    }
//
//    public int getTid() {
//        return TID;
//    }
//
//    public void setTid(int tid) {
//        TID = tid;
//    }
//
//    public int getMapId() {
//        return MapId;
//    }
//
//    public void setMapId(int mapId) {
//        MapId = mapId;
//    }
//
//    public int getDistance() {
//        return Distance;
//    }
//
//    public void setDistance(int Distance) {
//        this.Distance = Distance;
//    }
//
//    public double getExcessRisk() {
//        return ExcessRisk;
//    }
//
//    public void setExcessRisk(double ExcessRisk) {
//        this.ExcessRisk = ExcessRisk;
//    }
//
//    public int getExcessSurface() {
//        return ExcessSurface;
//    }
//
//    public void setExcessSurface(int ExcessSurface) {
//        this.ExcessSurface = ExcessSurface;
//    }
//
//    
//    
//    public int getMinDist() {
//        return MinDist;
//    }
//
//    public void setMinDist(int MinDist) {
//        this.MinDist = MinDist;
//    }
//
//    public int getScore() {
//        return Score;
//    }
//
//    public void setScore(int Score) {
//        this.Score = Score;
//    }
//
//    public String getResult() {
//        return Result;
//    }
//
//    public void setResult(String Result) {
//        this.Result = Result;
//    }
}
