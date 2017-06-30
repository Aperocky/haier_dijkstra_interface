/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map.path;

import static constant.GameConstants.SINGLE_STEP;
import model.map.Map;
import model.map.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * An iterable path consisting of all the nodes on the path,
 * capable of deviating itself and calculating total risk
 * @author Feng
 * 
 */
public class NodePath extends AbstractPath<Node>{
    private final Deviator mDeviator;
    
    public NodePath(Collection<Node> nodePath){
        mPath = new ArrayList<>(nodePath);
        mDeviator = new Deviator();
    }

    public Collection<Node> getNodes() {
        return getPath();
    }
    
    /**
     * Retrieve the first x steps of the path as node collection
     * @param start inclusive
     * @param end exclusive
     * @return 
     */
    public Collection<Node> getPartialNodes(int start, int end){
        return getPartialPath(start, end+1);
    }
    
    public NodePath subNodePath(int start, int end){
        return new NodePath(getPartialPath(start, end+1));
    }
    
    public Node nodeAt(int x){
        if (x >= mPath.size())
            return null;
        return mPath.get(x);
    }
    
    public void replaceNode(int x, Node node){
        replace(x, node);
    }
    
    /**
     * Calculate the path risk consumption given the node path
     *
     * @param risklevel
     * @param nodePath
     * @return risk consumption
     */
    public double calcRiskConsumption(Integer risklevel) {
        double risk = 0;
        risk = mPath.stream()
                .map((pathnode) -> (double) pathnode.getRisk() / risklevel)
                .reduce(risk, (total, currrisk) -> total + currrisk);
        return risk;
    }
    
    public Node getLastNode(){
        return getLast();
    }
    
        
    /**
     * Generate the ExecutedNodeStepPath mostly based on the PlannedPath,
     * with only the last Node deflected according to some normal Distr.
     * @return List<Edge>
     */
    public int deviate(){
        int dev = mDeviator.deviate(this);
        return dev;
    }
}
