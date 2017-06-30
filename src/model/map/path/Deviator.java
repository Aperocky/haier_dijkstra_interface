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
import java.util.Random;

/**
 *
 * @author Feng
 */
public class Deviator {

    /**
     * Generate the executed path based on the planned path, with only the last
     * Node deflected according to some normal Distr.
     * @param original nodepath
     * @return the amount of deviation
     */
    public int deviate(NodePath path) {
        int timestep = path.getNodes().size();
        if(timestep < SINGLE_STEP)
            return 0;
        // Standard Devaition for the fist step: sqrt(2) / 3
        int[][] NormDistribution1 = new int[][]{
        {0, 0, 0, 0, 0},
        {0, 0, 2, 0, 0},
        {0, 2, 92, 2, 0},
        {0, 0, 2, 0, 0},
        {0, 0, 0, 0, 0}};
        int[][] NormDistribution2 = new int[][]{
        {0, 0, 0, 0, 0},
        {0, 6, 7, 6, 0},
        {0, 7, 48, 7, 0},
        {0, 6, 7, 6, 0},
        {0, 0, 0, 0, 0}};
        int[][] NormDistribution3 = new int[][]{
        {0, 2, 4, 2, 0},
        {2, 4, 6, 4, 2},
        {4, 6, 28, 6, 4},
        {2, 4, 6, 4, 2},
        {0, 2, 4, 2, 0}};
        ArrayList<int[][]> NormDistributions = new ArrayList<>();
        NormDistributions.add(0, NormDistribution1);
        NormDistributions.add(1, NormDistribution2);
        NormDistributions.add(2, NormDistribution3);
        Node stopNode = path.nodeAt(timestep - 1);
        // mStopRow and mStopColumn are centered at (2,2)
        int mStopRow = 2;
        int mStopColumn = 2;
        int[][] mNormDistribution
                = NormDistributions.get(timestep / SINGLE_STEP - 1);
        int[] rowsProb = new int[5];
        for (int i = 0; i < 5; i++) {
            int sum = 0;
            for (int j = 0; j < 5; j++) {
                sum += mNormDistribution[i][j];
            }
            rowsProb[i] = sum;
        }
        int[] cumRowsProb = getCumSum(rowsProb);

        Random random = new Random();
        int m = random.nextInt(100);
        for (int i = 0; i < 5; i++) {
            if (cumRowsProb[i] >= m) {
                mStopRow = i;
                break;
            }
        }
        int sumCol = 0;
        for (int i = 0; i < 5; i++) {
            sumCol += mNormDistribution[mStopRow][i];
        }
        int n = sumCol == 0 ? 0 : random.nextInt(sumCol);
        int[] cumColsProb = getCumSum(mNormDistribution[mStopRow]);
        for (int i = 0; i < 5; i++) {
            if (cumColsProb[i] >= n) {
                mStopColumn = i;
                break;
            }
        }
        mStopRow = stopNode.getRow() - 2 + mStopRow; // mStopRow is originally centered at 2
        mStopColumn = stopNode.getColumn() - 2 + mStopColumn;
        Node mStopNode = new Node(mStopRow, mStopColumn, 0, false);
        if (mStopRow < 0 || mStopRow >= Map.PPR || mStopColumn < 0 || mStopColumn >= Map.PPR) {
            mStopNode = stopNode;
            System.out.println("Executed path goes out of map boundary. Keep the same path");
        }
        path.replace(timestep - 1, mStopNode);
        int dev = Math.max(Math.abs(mStopRow - stopNode.getRow()), Math.abs(mStopColumn - stopNode.getColumn()));
        return dev;
    }

    private int[] getCumSum(int[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                result[i] += array[j];
            }
        }
        return result;
    }
}
