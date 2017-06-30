/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.planner;

import static constant.GameConstants.BIGSTEP;
import static constant.GameConstants.MEDSTEP;
import static constant.GameConstants.RISK_BUDGET;
import static constant.GameConstants.SMALLSTEP;
import model.map.path.NodePath;
import service.cache.CacheService;
import javafx.concurrent.Task;

/**
 *
 * @author Feng
 */
public class StepPlanner {

    public static final double SURFACING_COST = 0.025;
    private double[] values;
    private double[] weights;

    public StepPlanner() {
        values = new double[3];
        weights = new double[3];
    }

    public int getOptimalStep(int minDist, double riskbudget, int surfacebgt, int[] steps, double[] risks) {
        for (int i = 0; i < values.length; i++) {

            values[i] = (double) steps[i] / minDist;
            // Sanity Check
            if (riskbudget <= 0) {
                riskbudget = risks[i] == 0 ? 1 : risks[i];
            }
            if (surfacebgt <= 0) {
                surfacebgt = 1;
            }
            weights[i] = risks[i] / riskbudget + 1d / (double) (surfacebgt * surfacebgt);
            // - SURFACING_COST
            //weights[i] = risks[i] / riskbudget + (double) 1 / (surfacebgt * surfacebgt * surfacebgt);
        }
        int optimalStep = -1;
        double highest = Double.MIN_VALUE;
        System.out.println("Value & Weight");
        for (int i = 0; i < values.length; i++) {
            System.out.println();
            System.out.println("Value: " + values[i]);
            System.out.println("Weight: " + weights[i]);
            System.out.println("Ratio: " + values[i] / weights[i]);
            if (values[i] / weights[i] > highest) {
                optimalStep = steps[i];
                highest = values[i] / weights[i];
            }
        }
        return optimalStep;
    }



}
