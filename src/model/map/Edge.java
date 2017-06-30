/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map;

import model.map.Node;
import java.util.Objects;

/**
 *
 * @author Feng
 */
public class Edge {
    double riskweight;
    double lengthweight;
    double totalweight;
    Node nodeA;
    Node nodeB;
    
    public Edge(Node a, Node b, double edgeRisk, double length){
        riskweight = edgeRisk;
        lengthweight = length;
        totalweight = riskweight + lengthweight;
        nodeA = a;
        nodeB = b;
    } 
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if(this.nodeA.equals(edge.nodeA) && this.nodeB.equals(edge.nodeB))
            return true;
        if(this.nodeA.equals(edge.nodeB) && this.nodeB.equals(edge.nodeA))
            return true;

        return false;
    }

    /**
     * If equals() evaluate to be true, then hashCodes need to be the same
     * @return 
     */
    @Override
     public int hashCode(){
         Integer hash = this.nodeA.getColumn() * this.nodeB.getColumn()+
                    this.nodeA.getRow() * this.nodeB.getRow();
         return hash;
     }
    
    public void updateRiskWeight(double risk){
        riskweight = risk;
        totalweight = lengthweight + riskweight;
    }
    public Node getNodeA(){
        return nodeA;
    }
    public Node getNodeB(){
        return nodeB;
    }
    public double getTotalWeight(){
        return totalweight;
    }
}
