package service.database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Feng
 */
public interface DBInterface {

    public static final String DBNAME = "com.mysql.jdbc.Driver";
    public static final String DATABASE = "jdbc:mysql://localhost:3306/haier?zeroDateTimeBehavior=convertToNull";
    public static final String USER = "Feng";
    public static final String PASSWORD = "Fys19970807!";

    public void init();

    public void destroy();

}
