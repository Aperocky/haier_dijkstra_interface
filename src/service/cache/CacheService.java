/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.cache;

import model.container.Pair;
import model.map.Map;
import model.map.Node;
import model.map.path.EdgePath;
import model.map.path.NodePath;
import alerts.HaierAlert;
import static alerts.HaierAlert.EMPTY_CACHE;
import service.planner.GlobalPlanner;
import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import model.container.PathPair;

/*
 * Receives a 2D array of dangerous blocks and starting & ending Node; Caches
 * PLANNED paths of all possible combination of risk levels and step size by
 * creating different maps for each risk level; Able to return a
 * {@link factory.PathFactory} of desired risk level and step size
 *
 * @author Feng
 */
public class CacheService {

    private int[][] mDangers;
    private Node mCurrent;
    private Node mEnd;
    private Collection<Integer> mTimeSteps;
    private Collection<Integer> mRiskLevels;
    private GlobalPlanner mPlanner;
    private Table<Integer, Integer, PathPair> mStepCache;
    private HashMap<Integer, PathPair> mFullCache;

    public CacheService(int[][] dangers, Node current, Node end, Collection<Integer> timesteps, Collection<Integer> risks) {
        mDangers = dangers;
        mCurrent = current;
        mEnd = end;
        mTimeSteps = timesteps;
        mRiskLevels = risks;
        mStepCache = HashBasedTable.create(mTimeSteps.size(), mRiskLevels.size());
        mFullCache = new HashMap<>();
        
        cachePath();
        
    }

    private void cachePath() {
        mRiskLevels.forEach((risk) -> {
            
            Map map = new Map(risk);
            map.updateDangers(mDangers);
            
            mPlanner = new GlobalPlanner(map);
            mCurrent = getNodeOnMap(mCurrent, map);
            mEnd = getNodeOnMap(mEnd, map);
            
            NodePath nodepath = mPlanner.getNodePath(mCurrent, mEnd);
            EdgePath edgepath = mPlanner.getEdgePath(mCurrent, mEnd);
            
            
            PathPair pathpair = new PathPair(nodepath, edgepath);
            mFullCache.put(risk, pathpair);
            mTimeSteps.forEach((step) -> {
                PathPair stepPathPair = new PathPair(nodepath.subNodePath(0, step),
                        edgepath.subEdgePath(0, step));
                            
                mStepCache.put(risk, step, stepPathPair);
            });
        });
    }

    // Use given fake starting and ending nodes to locate the real nodes in the map
    private Node getNodeOnMap(Node current, Map map) {
        for (Node node : map.getNodes()) {
            if (node.equals(current)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Return the Planned node path of the desired step size
     *
     * @param risk
     * @param step
     * @return nodepath
     */
    public NodePath getNodePath(int risk, int step) {
        checkEmptyCache();
        return mStepCache.get(risk, step).getNodePath();
    }

    /**
     * Return the Planned edge path of the desired step size Show attempted
     * paths on map
     *
     * @param risk
     * @param step
     * @return edgepath
     */
    public EdgePath getEdgePath(int risk, int step) {
        checkEmptyCache();
        return mStepCache.get(risk, step).getEdgePath();
    }
    
    /**
     * Return the full path specified by the risk
     * @param risk
     * @return EdgePath
     */
    public EdgePath getFullEdgePath(int risk){
        checkEmptyCache();
        return mFullCache.get(risk).getEdgePath();
    }

    /**
     * Perceive risks as really insignificant to choose the shortest path
     * @return minimum distance to the destination
     */
    public int getMinimumPathLength() {
        checkEmptyCache();
        Integer highRisk = min(mRiskLevels);
        return getFullEdgePath(highRisk).length();
    }
    
    private Integer min(Collection<Integer> arr){
        Integer min = Integer.MAX_VALUE;
        for(Integer i : arr){
            if(i < min)
                min = i;
        }
        return min;
    }
    
    private Integer max(Collection<Integer> arr){
        Integer max = Integer.MIN_VALUE;
        for(Integer i : arr){
            if(i > max)
                max = i;
        }
        return max;
    }

    /**
     * Retrieve the last node of the executed path, which is the possible
     * deflected node.
     *
     * @param risk
     * @param step
     * @return
     * @throws HaierException
     */
    public Node getUncertainNode(int risk, int step){
        checkEmptyCache();
        NodePath stepNodePath = mStepCache.get(risk, step).getNodePath();
        return (Node) stepNodePath.nodeAt(stepNodePath.length()-1);
    }

    /**
     * Refresh the cache table to calculate all new paths starting from current
     * node
     *
     * @param currentNode
     */
    public void refreshCache(Node currentNode) {
        mCurrent = currentNode;
        cachePath();
    }

    private void checkEmptyCache(){
        if (mStepCache == null || mStepCache.isEmpty()) {
            HaierAlert.getAlert(EMPTY_CACHE).showAndWait();
        }
    }

}
