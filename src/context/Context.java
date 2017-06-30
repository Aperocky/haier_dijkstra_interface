/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package context;

import algorithm.ScoreCalculator.ScoreBuilder;
import model.database.Game;
import model.database.Turn;
import model.database.Turn_Confidence;
import model.database.Turn_Control;
import model.database.Turn_Eval;
import model.database.Turn_Expectation;
import model.database.User;
import service.database.DBService;
import service.parse.ParseService;
import service.database.DBWriter;
import service.stage.StageService;

/**
 *
 * @author Feng
 */
public class Context {
    private final static Context instance = new Context();
            
    public static Context getInstance(){
        return instance;
    }
    
    private final static Boolean mDBEnabled = false;
    private static final ParseService mParseService = new ParseService();
    private static final DBWriter mDBWriter = new DBWriter(new DBService());
    private static final StageService sceneservice = new StageService();
    private static final ScoreBuilder mScoreBuilder = new ScoreBuilder();
    private static Game game;
    private static User user;
    private static Turn turn;
    private static Turn_Confidence confidence;
    private static Turn_Eval eval;
    private static Turn_Expectation expect;
    private static Turn_Control control;
    
    public DBWriter getDBWriter(){
        return mDBWriter;
    }
    
    public ScoreBuilder getScoreBuilder(){
        return Context.mScoreBuilder;
    }
    
    public boolean isDBEnabled(){
        return mDBEnabled;
    }
    public StageService getSceneService(){
        return sceneservice;
    }
    
    public void setGame(Game game){
        Context.game = game;
    }
    public Game getGame(){
        return Context.game;
    }

    public User getUser() {
        return Context.user;
    }

    public void setUser(User user) {
        Context.user = user;
    }

    public Turn getTurn() {
        return Context.turn;
    }

    public void setTurn(Turn turn) {
        Context.turn = turn;
    }

    public Turn_Confidence getConfidence() {
        return confidence;
    }

    public void setConfidence(Turn_Confidence confidence) {
        Context.confidence = confidence;
    }

    public Turn_Eval getEval() {
        return eval;
    }

    public void setEval(Turn_Eval eval) {
        Context.eval = eval;
    }

    public Turn_Expectation getExpect() {
        return expect;
    }

    public void setExpect(Turn_Expectation expect) {
        Context.expect = expect;
    }

    public Turn_Control getControl() {
        return control;
    }

    public void setControl(Turn_Control control) {
        Context.control = control;
    }
    

    
    
}
