/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.database;

import model.database.DBModel;
import model.database.Game;
import exception.DBException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Upon receiving a connection to the database, DBWriter sends all information
 * within a model to the database
 *
 * @author Feng
 */
public class DBWriter {

    private final DBService mService;
    private final Connection mConnection;
    public static final String log_file_path = "/Users/aperocky/workspace/Labwork/haier_dijkstra_interface/";
    public String log_file;

    public DBWriter(DBService service) {
        mService = service;
        mConnection = mService.getConnection();
    }

    public void writeModel(DBModel model) throws DBException {
        try {
            String insertStr = buildInsert(model);
            PreparedStatement stmt = mConnection.prepareStatement(insertStr);
            Integer index = 1;
            for (Object key : model.keySet()) {
                stmt.setObject(index, model.get(key));
                //setValue(stmt, model.get(key), index);
                index++;
            }
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DBException(ex.getMessage());
        }
    }

    public void setLog_file(int uid){
        String filename = "user" + Integer.toString(uid) + ".txt";
        this.log_file = log_file_path + filename;
    }

    public void writeRecord(DBModel model){
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(this.log_file, true));
            System.out.println("Executing writing session " + model.getName());
            output.write("NAME : ");
            output.write(model.getName());
            output.newLine();
            for (HashMap.Entry<String, Object> entry : model.entrySet()) {
                output.write(entry.getKey());
                output.write(" : ");
                output.write(entry.getValue().toString());
                output.newLine();
            }
            output.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String buildInsert(DBModel model) {
        StringBuilder insertBdr = new StringBuilder();
        String dbname = model.getName();
        insertBdr.append("INSERT INTO ").append(dbname).append("(");
        StringJoiner joiner = new StringJoiner(", ");
        model.keySet().forEach((Object key) -> {
            joiner.add((String) key);
        });
        insertBdr.append(joiner.toString()).append(") ").append("VALUES").append("(");
        StringJoiner questionJr = new StringJoiner(", ");
        for (int i = 0; i < model.keySet().size(); i++) {
            questionJr.add("?");
        }
        insertBdr.append(questionJr.toString()).append(")").append(";");
        return insertBdr.toString();
    }

    private void setValue(PreparedStatement stmt, Object val, Integer index) throws SQLException {
        if (val instanceof String) {
            System.out.println("String database input.");
            stmt.setString(index, (String) val);
        } else if (val instanceof Integer) {
            System.out.println("Integer database input.");
            stmt.setInt(index, (Integer) val);
        } else if (val instanceof Double) {
            stmt.setDouble(index, (Double) val);
        } else if (val instanceof Boolean) {
            stmt.setBoolean(index, (Boolean) val);
        } else {
            stmt.setString(index, val.toString());
        }
    }

    public void destroy() throws DBException {
        if (mConnection != null) {
            try {
                mConnection.close();
            } catch (SQLException ex) {
               throw new DBException(ex.getMessage());
                }
            }
        }

        // Test
//    public static void main(String[] args) throws DBException{
//        DBService service = new DBService();
//        DBWriter writer = new DBWriter(service.getConnection());
//        Game game = new Game(666, 8888, 3);
//        writer.writeModel(game);
//    }
    }
