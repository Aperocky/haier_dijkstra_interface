/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.database;

import service.database.DBInterface;
import java.sql.*;

/**
 *
 * @author Feng
 */
public class DBService implements DBInterface {

    private static Connection mConnection;

    public DBService(){
        
    }
    /**
     * Should only call once throughout the project, since it poses huge overhead
     */
    @Override
    public void init() {
        try {
            Class.forName(DBNAME);
            mConnection = DriverManager.getConnection(
                    DATABASE, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Failed to get connection");
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        if (mConnection != null) {
            try {
                mConnection.close();
            } catch (Exception e) {
                System.out.println("Failed to kill connection");
            }
        }
    }

    /**
     * 
     * @return connection between local SQL and current application 
     */
    public Connection getConnection() {
        if(mConnection == null)
            init();
        return mConnection;
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }

        }
    }
    
    public void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }

        }
    }

}
