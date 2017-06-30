/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Feng
 */
public class MapUnavailableException extends Exception{
    public MapUnavailableException(String mapid){
        super("Chosen map with id " + mapid + " is unavailable.");
    }
}
