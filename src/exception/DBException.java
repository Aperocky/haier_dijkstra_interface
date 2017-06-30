/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import java.sql.SQLException;

/**
 *
 * @author Feng
 */
public class DBException extends SQLException{
    public DBException(String msg){
        super("Database Exception: " + msg);
    }
    
}
