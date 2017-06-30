/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.container;

import model.map.path.EdgePath;
import model.map.path.NodePath;

/**
 *
 * @author Feng
 */
public class PathPair extends Pair<NodePath, EdgePath>{
    public PathPair(NodePath nodePath, EdgePath edgePath){
        super(nodePath, edgePath);
    }
    
    public NodePath getNodePath(){
        return this.getA();
    }
    
    public EdgePath getEdgePath(){
        return this.getB();
    }
    
}
