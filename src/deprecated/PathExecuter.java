/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated;

import model.map.Node;
import model.map.Map;
import model.map.Edge;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import algorithm.Dijkstra;
import constant.GameConstants;
import static constant.GameConstants.SINGLE_STEP;
import model.map.Edge;
import model.map.Map;
import model.map.Node;
import model.map.path.EdgePath;
import model.map.path.NodePath;
import java.util.Collection;
import riskawarenesssimulation.Controller.MapController;

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
public class PathExecuter implements GameConstants{
    private Map mMap;
    private NodePath mNodePath;
    private EdgePath mEdgePath;
    
    public PathExecuter(Map map, NodePath nodePlan, EdgePath edgePlan){
        mMap = map;
        mNodePath = new NodePath(nodePlan.getNodes());
        mEdgePath = new EdgePath(edgePlan.getEdges());
    }
    
    /**
     * Generate the ExecutedNodeStepPath mostly based on the PlannedPath,
     * with only the last Node deflected according to some normal Distr.
     * @return List<Edge>
     */
    public NodePath getExecutedNodeStepPath(int timestep){
        //List<Node> PlannedNodePath = new ArrayList<>(getPlannedNodeStepPath());

        // Probablistic distribution generated based on N(0,SPACING*TIME_STEP/3)
        // 3 is chosen as the denominator here so that after the first step, we 
        // can almost gurantee that we will fall within our desired path
        int[][] NormDistribution1 = new int[][]{{0,0,0,0,0},
                                                {0,0,0,0,0},
                                                {0,0,100,0,0},
                                                {0,0,0,0,0},
                                                {0,0,0,0,0}};
        int[][] NormDistribution2 = new int[][]{{0,0,0,0,0},
                                                {0,3,5,3,0},
                                                {0,5,68,5,0},
                                                {0,3,5,3,0},
                                                {0,0,0,0,0}};
        int[][] NormDistribution3 = new int[][]{{1,2,3,2,1},
                                                {2,5,5,5,2},
                                                {3,5,28,5,3},
                                                {2,5,5,5,2},
                                                {1,2,3,2,1}};
        ArrayList<int[][]> NormDistributions = new ArrayList<>();
        NormDistributions.add(0,NormDistribution1);
        NormDistributions.add(1,NormDistribution2);
        NormDistributions.add(2,NormDistribution3);
        Node stopNode = (Node) mNodePath.nodeAt(mNodePath.length()-1);
        // mStopRow and mStopColumn are centered at (2,2)
        int mStopRow = 2;
        int mStopColumn = 2;
        int[][] mNormDistribution = 
                NormDistributions.get(timestep/SINGLE_STEP-1);
        int[] rowsProb = new int[5];
        for(int i=0; i<5; i++){
            int sum=0;
            for(int j=0; j<5; j++){
                sum+=mNormDistribution[i][j];
            }
            rowsProb[i] = sum;
        }
        int[] cumRowsProb = getCumSum(rowsProb);
        
        Random random = new Random();
        int m = random.nextInt(100);
        for(int i=0; i<5; i++){
            if(cumRowsProb[i]<m)continue;
            else {
                mStopRow = i;
                break;
            }
        }
        int sumCol = 0;
        for(int i=0; i<5; i++){
            sumCol += mNormDistribution[mStopRow][i];
        }
        int n = random.nextInt(sumCol);
        int[] cumColsProb = getCumSum(mNormDistribution[mStopRow]);
        for(int i=0; i<5; i++){
            if(cumColsProb[i]<n)continue;
            else {
                mStopColumn = i;
                break;
            }
        }
        mStopRow = stopNode.getRow()-2+mStopRow; // mStopRow is originally centered at 2
        mStopColumn = stopNode.getColumn()-2+mStopColumn;
        Node mStopNode = new Node(mStopRow, mStopColumn, 0, false);
        if(mStopRow<0 || mStopRow>=Map.PPR || mStopColumn<0 || mStopColumn>=Map.PPR){
            mStopNode = stopNode;
            System.out.println("Executed path goes out of map boundary. Keep the same path");
       
        }
        mNodePath.replaceNode(mNodePath.length()-1, mStopNode);
        return mNodePath;
    }
    
    private int[] getCumSum(int[] array){
        int[] result = new int[array.length];
        for(int i=0; i<array.length; i++){
          for(int j=0; j<i+1; j++){
            result[i] += array[j];
          }
        }
        return result;
    }
    
    /**
     * Only call this method after calling getExecutedNodeStepPath()!
     * That will set up mStopNode to be used in this method.
     * @return List<Edge> ExecutedStepPath
     */
    public EdgePath getExecutedStepPath(int timestep){
        EdgePath plannedEdge = mEdgePath;
        NodePath executedNode = getExecutedNodeStepPath(timestep);
        Node lastNode = (Node)executedNode.nodeAt(executedNode.length()-1);
        Node secondLastNode = (Node)executedNode.nodeAt(executedNode.length()-2);

        Edge lastEdge = new Edge(lastNode, secondLastNode, 0, 0);
        for(Edge edge : mMap.getEdges()){
            if(edge.equals(lastEdge)){
                lastEdge = edge;
                break;
            }
        }        
        
        plannedEdge.replaceEdge(plannedEdge.length()-1, lastEdge);
        return plannedEdge;
        
    }

}
