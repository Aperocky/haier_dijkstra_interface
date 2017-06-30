/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.planner;

import static constant.GameConstants.BIGSTEP;
import static constant.GameConstants.MEDSTEP;
import static constant.GameConstants.SMALLSTEP;
import model.map.path.NodePath;
import service.cache.CacheService;
import javafx.concurrent.Task;

/**
 *
 * @author Feng
 */
public class ChooseStepTask extends Task<Integer> {

    private final StepPlanner mPlanner;
    private final CacheService mCache;
    private final double mLeftRisk;
    private final int mLeftSurface;
    private final int mRiskLevel;

    public ChooseStepTask(CacheService cache, int risk, double leftRisk, int leftSurface) {
        mPlanner = new StepPlanner();
        mLeftRisk = leftRisk;
        mLeftSurface = leftSurface;
        mCache = cache;
        mRiskLevel = risk;
    }

    @Override
    protected Integer call() throws Exception {
        int[] steps = new int[]{BIGSTEP, MEDSTEP, SMALLSTEP};
        double[] risks = new double[steps.length];
        NodePath pathBig = mCache.getNodePath(mRiskLevel, steps[0]);
        NodePath pathMed = mCache.getNodePath(mRiskLevel, steps[1]);
        NodePath pathSmall = mCache.getNodePath(mRiskLevel, steps[2]);
        
        // Test
        if(pathBig == null)
            System.out.println("PathBig is null.");
        if(pathMed == null)
            System.out.println("PathMed is null.");
        if(pathSmall == null)
            System.out.println("PathSmall is null.");
        
        risks[0] = pathBig.calcRiskConsumption(mRiskLevel);
        risks[1] = pathMed.calcRiskConsumption(mRiskLevel);
        risks[2] = pathSmall.calcRiskConsumption(mRiskLevel);
        int minDist = mCache.getMinimumPathLength();
       
        
        System.out.println();
        System.out.println("Choosing Optimal Step Size: ");
        System.out.println();
        System.out.println("MinDist: " + minDist);
        System.out.println();
        System.out.println("Remaining Risk budget: " + (mLeftRisk));
        System.out.println();
        System.out.println("Big Step: " + steps[0]);
        System.out.println("Medium Step: " + steps[1]);
        System.out.println("Small Step: " + steps[2]);
        System.out.println();
        System.out.println("Risk for big step: " + risks[0]);
        System.out.println("Risk for med step: " + risks[1]);
        System.out.println("Risk for small step: " + risks[2]);
        System.out.println();

        return mPlanner.getOptimalStep(minDist, mLeftRisk, mLeftSurface, steps, risks);
    }

}
