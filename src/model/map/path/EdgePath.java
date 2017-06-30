/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map.path;

import model.map.Edge;
import model.map.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Iterable path that is consisted of all edges
 * @author Feng
 */
public class EdgePath extends AbstractPath<Edge>{
    
    public EdgePath(Collection<Edge> edgePath){
        mPath = new ArrayList<>(edgePath);
    }

    public Collection<Edge> getEdges() {
        return getPath();
    }
    
    /**
     * Retrieve the first x steps of the path as node collection
     * @param steps
     * @return 
     */
    public Collection<Edge> getPartialEdges(int start, int end){
        return getPartialPath(start, end);
    }
    
    public EdgePath subEdgePath(int start, int end){
        return new EdgePath(getPartialPath(start, end));
    }
    
    public void replaceEdge(int x, Edge edge){
        replace(x, edge);
    }
    
    public Edge getLastEdge(){
        return getLast();
    }
    
}
