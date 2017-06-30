/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map;

import constant.GameConstants;
import java.util.HashSet;

/**
 * Generate the "dangerous map" based on the given RISK_LEVEL Give each node and
 * edge corresponding degree of danger(risk and riskweight respectively), so
 * that Dijkstra path planning well take into account the existence of dangerous
 * nodes
 *
 * @author Feng
 */
public class Map implements GameConstants{

    // A higher RISK_LEVEL means we are more risk aversed, while a lower value
    // means we are less worried about risk.
    // RISK_LEVEL is highly related to SPACING: ~SPACING^3 to be significant
    // enough to affect the actual path
    private int RISK_LEVEL = 0;
    // Total number of nodes
    private static Node[] mNodes = new Node[PPR * PPR];
    // Total number of edges
    private static Edge[] mEdges = new Edge[4 * PPR * PPR - 6 * PPR + 2];

    public Map(int risklevel) {
        RISK_LEVEL = risklevel;
        // Initialize all nodes
        for (Integer row = 0; row < PPR; row++) {
            for (Integer col = 0; col < PPR; col++) {
                Node currNode = new Node(row, col, 0, false);
                mNodes[PPR * row + col] = currNode;
            }
        }
        // Initialize all edges
        HashSet<Edge> edgesSet = new HashSet<>();
        for (Node nodeA : mNodes) {
            for (Node nodeB : mNodes) {
                if (nodeA.getIndex() != nodeB.getIndex()) {
                    int colA = nodeA.getColumn();
                    int colB = nodeB.getColumn();
                    int rowA = nodeA.getRow();
                    int rowB = nodeB.getRow();
                    // Add edges vertically or horizontally
                    if ((colB == colA && rowB == rowA - 1)
                            || (colB == colA && rowB == rowA + 1)
                            || (rowB == rowA && colB == colA - 1)
                            || (rowB == rowA && colB == colA + 1)) {
                        Edge currEdge = new Edge(nodeA, nodeB, 0, SPACING);
                        Edge revEdge = new Edge(nodeB, nodeA, 0, SPACING);
                        // Ensure that only one edge between any nodes exist.
                        edgesSet.add(currEdge);
                        edgesSet.add(revEdge);

                    }
                    // Add edges diagnolly
                    if ((colB == colA - 1 && rowB == rowA - 1)
                            || (colB == colA - 1 && rowB == rowA + 1)
                            || (rowB == rowA - 1 && colB == colA + 1)
                            || (rowB == rowA + 1 && colB == colA + 1)) {
                        Edge currEdge = new Edge(nodeA, nodeB, 0, SPACING * Math.sqrt(2));
                        // Ensure that only one edge between any nodes exist.
                        edgesSet.add(currEdge);
                    }
                }
            }
        }
        if (edgesSet.size() != mEdges.length) {
            System.out.println(String.format(
				"Edge Initialization Failed.\nExpected : %1$d \nReceived:  %2$d \n",
				mEdges.length, edgesSet.size()));
        } else {
            mEdges = edgesSet.toArray(new Edge[edgesSet.size()]);
        }

    }
    // Mark the node at (row, column) to be dangerous; Update the risks of
    // nodes around it; Update the riskweight of the edges around it

    public void updateDanger(int row, int column) {
        updateDangerousNode(row, column);
        updateNodesRisk();
        updateEdgesWeight();
    }
    // Add in multiple dangerous nodes

    public void updateDangers(int[][] dangers) {
        for (int[] danger : dangers) {
            updateDangerousNode(danger[0], danger[1]);
        }
        updateNodesRisk();
        updateEdgesWeight();
    }

    private void updateDangerousNode(int row, int column) {
        for (Node node : mNodes) {
            if (node.getColumn() == column && node.getRow() == row) {
                node.setDanger();
                node.setRisk(1 * RISK_LEVEL);
                break;
            }
        }
    }
    // Adjust the risks of nodes 3 SPACING away from the dangerous nodes

    private void updateNodesRisk() {
        for (Node nodeA : mNodes) {
            for (Node nodeB : mNodes) {
                if (nodeA.distanceTo(nodeB) < 3 * SPACING && !nodeA.equals(nodeB)) {
                    if (nodeB.isDanger()) {
                        double risk = nodeA.getRisk();
                        risk += (double) nodeB.getRisk() / (Math.pow((nodeA.distanceTo(nodeB)), 2));
                        //System.out.println(nodeA.distanceTo(nodeB));
                        nodeA.setRisk(risk);
                    }

                }
            }
        }
    }

    private void updateEdgesWeight() {
        for (Edge edge : mEdges) {
            double risk = (edge.getNodeA().getRisk() + edge.getNodeB().getRisk()) / 2;
            edge.updateRiskWeight(risk);
        }
    }

    public boolean isCollided(Node currNode) {
        for (Node nodeA : mNodes) {
            if (nodeA.equals(currNode)) {
                return nodeA.isDanger();
            }
        }
        return false;
    }

    public Node[] getNodes() {
        return mNodes;
    }

    public Edge[] getEdges() {
        return mEdges;
    }

    public static void main(String[] args) {
        Map testMap = new Map((int) Math.pow(20, 3));
    }

}
