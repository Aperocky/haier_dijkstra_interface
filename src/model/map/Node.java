/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map;

import constant.GameConstants;

/**
 * The basic unit of the map
 * @author Feng
 */
public class Node {
    private int index;
    private Integer row;
    private Integer column;
    private Integer locationX;
    private Integer locationY;
    private double risk;
    private boolean isDanger;
    public Node(int row, int col, double risk, boolean isDanger){
        this.isDanger = isDanger;
        this.row = row;
        this.column = col;
        this.risk = risk;
        this.index = Map.PPR*row+column;
        this.locationX = col*Map.SPACING+20;
        this.locationY = row*Map.SPACING+40;
    }
    
    public Node(int row, int col){
        this(row, col, 0, false);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if(node.index != this.index)return false;
        if(node.column != this.column || node.row != this.row)return false;
        //if(node.risk != this.risk) return false;
        //if(node.isDanger != this.isDanger)return false;
        return true;
    }
    
    @Override 
    public int hashCode(){
        return this.row * 7 + this.column * 13;
    }
    
    
    public double distanceTo(Node node){
        double dis = (double)Math.pow((Math.pow((this.getX()-node.getX()),2)+
                               Math.pow((this.getY()-node.getY()),2)),0.5);
        return dis;
    }

    public Integer getX(){
        return locationX;
    }
    public Integer getY(){
        return locationY;
    }
    public Integer getColumn(){
        return column;
    }
    public Integer getRow(){
        return row;
    }
    public int getIndex(){
        return index;
    }
    public void setDanger(){
        this.isDanger = true;
    }
    
    public void setRisk(double risk){
        this.risk = risk;
    }
    public double getRisk(){
        return risk;
    }
    public boolean isDanger(){
        return isDanger;
    }
     // Check if two nodes are directly next to each other
    /*public boolean oneEdgeAway(Node node){
        int colA = this.getColumn();
        int colB = node.getColumn();
        int rowA = this.getRow();
        int rowB = node.getRow();
        if((colB == colA && rowB == rowA - 1)||
           (colB == colA && rowB == rowA + 1)||
           (rowB == rowA && colB == colA - 1)||
           (rowB == rowA && colB == colA + 1)){
           return true;
        }   
        else if((colB == colA - 1 && rowB == rowA - 1)||
                (colB == colA - 1 && rowB == rowA + 1)||
                (rowB == rowA - 1 && colB == colA + 1)||
                (rowB == rowA + 1 && colB == colA + 1)){
            return true;
        }
        return false;
    }
    // Check if two nodes are one node away from each other
    public boolean twoEdgeAway(Node node){
        int colA = this.getColumn();
        int colB = node.getColumn();
        int rowA = this.getRow();
        int rowB = node.getRow();
        if((rowA == rowB - 2 && colB >= colA-1 && colB <= colA + 1)||
           (rowA == rowB + 2 && colB >= colA-1 && colB <= colA + 1)||
           (colA == colB + 2 && rowB >= rowA-1 && rowB <= rowA + 1)||
           (colA == colB - 2 && rowB >= rowA-1 && rowB <= rowA + 1))
                return true;
        return false;
    }
    // Check if two nodes are one node away from each other
    public boolean threeEdgeAway(Node node){
        int colA = this.getColumn();
        int colB = node.getColumn();
        int rowA = this.getRow();
        int rowB = node.getRow();
        if((rowA == rowB - 3 && colB >= colA-2 && colB <= colA + 2)||
           (rowA == rowB + 3 && colB >= colA-2 && colB <= colA + 2)||
           (colA == colB + 3 && rowB >= rowA-2 && rowB <= rowA + 2)||
           (colA == colB - 3 && rowB >= rowA-2 && rowB <= rowA + 2))
                return true;
        return false;
    }*/
    
}
