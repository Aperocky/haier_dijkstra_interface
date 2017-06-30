
package algorithm;

import model.map.Node;
import model.map.Edge;
import model.map.path.EdgePath;
import model.map.path.NodePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the actual Dijkstra implementation in which 
 * a adjacency-list of predecessors with the smallest edge-weight  
 * starting with a given start node gets created and the shortest 
 * path according to a given destination node will be created.
 * 
 * @author MonkeySR1, Feng
 * @version 1.0
 * @since   2017-01-10
 */
public class Dijkstra {
    List<Node> nodes = new ArrayList(); 
    List<Edge> edges = new ArrayList(); 

    Node startNode;
    Node endNode;
    HashMap<Node, Double> distance = new HashMap<>(); 
    HashMap<Node, Node> predecessor = new HashMap<>(); 
    HashMap<Node, Edge> predecessorEdge = new HashMap<>();
    List<Node> unvisited = new ArrayList<>(); //Q

    /**
     * Add graph data to the Dijkstra Object and start the initialization.
     * The sets of nodes and edges build the graph, the startNode is the 
     * node from which the shortest path gets calculated to all other 
     * nodes.
     * 
     * @param newNodes
     * @param newEdges
     * @param newStartNode 
     */
    public Dijkstra(Node newNodes[], Edge newEdges[], Node newStartNode, Node newEndNode){
        nodes.addAll(Arrays.asList(newNodes));
        edges.addAll(Arrays.asList(newEdges));
        startNode   = newStartNode;
        endNode = newEndNode;
        initializeDijkstra();
    }

    /**
    * To initialize the needed Maps and Lists, set all distances for each node to unlimited
    * and all predecessors of each node to NULL (unknown). 
    * Only the startNode receives the distance 0 as this is were the path begins.
    * All nodes get added to the list of unvisited Nodes, nodes remain in list unvisited 
    * as long as no shortest path has been found to that remaining node.
    * 
    * After the initialization of the needed Maps and Lists,
    * take a node with the shortest distance out of the list of unvisited nodes,
    * find all its neighbours and update their distance to the startNode.
    * Do this, until all nodes have been visited and all shortest distances
    * are updated in the distance map. 
    * Nodes remaining in the unvisited list should only be isolated nodes, 
    * these nodes also remain with distance unlimited in the distance map.
    */    
    private void initializeDijkstra() {
        for(Node currNode: nodes){
            distance.put(currNode, Double.MAX_VALUE);
            predecessor.put(currNode, null);
            predecessorEdge.put(currNode, null);
        }
        //shortest distance from startNode to all other nodes, distance from start to start is 0
        distance.replace(startNode, (double)0);
        unvisited.addAll(nodes);
        
        while (!unvisited.isEmpty()) {
            //helper function - first node returned will be the start node as only here weight is 0
            Node currNode = getClosestNode();
            //helper function to find neighbours of the current Node in Edge-List
            List<Node> neighbours = getNeighbours(currNode); 
            //update the distance, if the path via an unvisited neighbourNode is shorter
            for (Node neighbourNode : neighbours) {
                if(unvisited.contains(neighbourNode)) {
                    distanceUpdate(currNode,neighbourNode);
                }                        
            }
            if (currNode == null)
            {     //stop searching if currNode is null
                  //as currNode is an isolated node
                  //and its distance in unlimited!!!  
                  return;
            }
            unvisited.remove(currNode);            
        }
    }
    
    /**
     * helper function to get unvisited node with the smallest edge weight 
     * First node returned will be the start node, as it has distance 0. 
     * After that, the neighborNode distances of the startNode will be 
     * updated via getNeighbours() and distance_update() and so on.
     * 
     * @return Node with smallest distance
     */    
    private Node getClosestNode() {
        double currDistance = Integer.MAX_VALUE;
        Node closest = null;

        for (Node currNode : unvisited) {
            if (distance.get(currNode).intValue() < currDistance) {
                currDistance = distance.get(currNode);
                closest = currNode;
            }
        }
        return closest;
    }      
    
    /**
     * helper function to get all neighbours of the current unvisited Node 
     * check wether the currNode is unvisited, if yes, check if the currNode
     * is a startNode or endNode of an edge and add the node(s) it is connected
     * to into a neighbour list and return the list
     * breadth-first, return empty list if current node was already visited
     */
    private List<Node> getNeighbours(Node currNode) {            
        List<Node> neighbours = new ArrayList<>();

        for (Edge currEdge : edges) {
            if(unvisited.contains(currNode) && currEdge != null) {
                if (currEdge.getNodeA() == currNode) {
                    neighbours.add(currEdge.getNodeB());
                }
                if (currEdge.getNodeB() == currNode) {
                    neighbours.add(currEdge.getNodeA());
                }
            }
        }
        return neighbours;
    }

    /**
    * Check if the distance from startNode to neighbourNode via currNode 
    * is shorter then the way to that particular neighbourNode so far.
    * If that is the case, the distance and predecessor map get updated 
    * with alternative distance and predecessor of neighbourNode will be 
    * updated to currNode.
    *   
    */
    private void distanceUpdate(Node currNode,Node neighbourNode){
        Edge edge = getEdgeBetween(currNode, neighbourNode);
        //alternative path is the full path to the neighbourNode via the currentNode 
        double alternative = distance.get(currNode)+ 
                              edge.getTotalWeight();
        //if it is shorter then the path to the neighbourNode directly
        if (alternative < distance.get(neighbourNode)) {
            //update the accumulated distance from StartNode to the NeighbourNode 
            //with the alternative accumulated distance via the currentNode
            distance.replace(neighbourNode , alternative);
            //replace the predecessor node of the neighbourNode with the better alternative currentNode
            predecessor.replace(neighbourNode, currNode);
            predecessorEdge.replace(neighbourNode, edge);
        }
    }
             
    /**
     * helper-function to get the edge between two nodes
     * as the Graph is undirected, we can check for edges that have 
     * startNode and endNode at either side of the edge (uRv) is equal to (vRu) 
     */
    private Edge getEdgeBetween(Node startNode, Node endNode) {
        for (Edge currEdge : edges) {
            if (currEdge.getNodeA().equals(startNode) && currEdge.getNodeB().equals(endNode)) {
                return currEdge;
            }
            else if (currEdge.getNodeB().equals(startNode) && currEdge.getNodeA().equals(endNode)) {
                return currEdge;
            }
        }
        return null;
    }
    
    /**
     * get the total weight of the edges between the start and destination node
     * as the stored distance is the accumulated distance from startNode
     * to each other Node(shortestPathTree), we can get the distance by just getting
     * the distance (integer) object which is associated with the destinationNode
     * 
     * @param destinationNode The node for which you like to know the distance to from startNode
     * @return integer weight = total distance from startNode to destinationNode
     */
    public double getPathWeight(){
        double weight = distance.get(endNode);
        return weight;
    }
    /**  
    * Get shortest path by going backwards through the predecessor map.
    * Starting with the destinationNode, get each predecessor until we 
    * reach the startNode, which is the first node in the map.
    * 
    * @param destinationNode The Node from which to find the path to startNode 'backwards'
    * @return The shortest path from startNode to destinatioNode
    */
    public EdgePath createShortestPath() {
        List<Edge> path = new ArrayList<>();
        Node currNode = endNode;
        Edge currEdge = null;
        //last node to be accessed in predecessor Map is the startNode, its predecessor is null
        while (predecessorEdge.get(currNode) != null) {
            currEdge = (Edge)predecessorEdge.get(currNode); //add to front
            path.add(0,currEdge);
            currNode = currEdge.getNodeA().equals(currNode) ? currEdge.getNodeB():currEdge.getNodeA();
        }
        return new EdgePath(path);
    }
    
    
    public NodePath createShortestNodePath() {
        List<Node> path = new ArrayList<>();
        Node currNode = endNode;
        path.add(0,currNode);
        //last node to be accessed in predecessor Map is the startNode, its predecessor is null
        while (predecessor.get(currNode) != null) {
            currNode = (Node)predecessor.get(currNode); //add to front
            path.add(0,currNode);
        }
        //System.out.println("System Path: " + path.size());
        return new NodePath(path);
    }
}
