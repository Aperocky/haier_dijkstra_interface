/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.planner;

import algorithm.Dijkstra;
import model.map.Map;
import model.map.Node;
import model.map.path.EdgePath;
import model.map.path.NodePath;

/**
 * Plan the path from startNode to destinationNode in a holistic manner,
 * then trim the path according to the specific TIME_STEP given;
 * 
 * Generate the actual executed path from startNode to stopNode, taking
 * into account the increasing probability of deviation;
 * 
 * Initialize this class only after the map is drawn with desired RISK_LEVEL
 * @author Feng
 */
public class GlobalPlanner{
    // Total size of each step
    private Map mMap;
    //private List<Edge> mWholePath;
    //private List<Node> mWholeNodePath;
    private Dijkstra mDijkstra;
    
    public GlobalPlanner(Map map){
        mMap = map;
    }
    
    public EdgePath getEdgePath(Node start, Node end){
        mDijkstra = new Dijkstra(mMap.getNodes(), mMap.getEdges(), start, end);
        return mDijkstra.createShortestPath();
    }
    
    public NodePath getNodePath(Node start, Node end){
        mDijkstra = new Dijkstra(mMap.getNodes(), mMap.getEdges(), start, end);
        return mDijkstra.createShortestNodePath();
    }
    
    public NodePath getNodeStepPath(Node start, Node end, int timestep){
        NodePath complete = getNodePath(start, end);
        return new NodePath(complete.getPartialNodes(0, timestep));
    }
    
    public EdgePath getEdgeStepPath(Node start, Node end, int timestep){
        EdgePath complete = getEdgePath(start, end);
        return new EdgePath(complete.getPartialEdges(0, timestep));
    }
    
}
