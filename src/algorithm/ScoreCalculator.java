/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import constant.GameConstants;
import static constant.GameConstants.SCORE_SCALE;

/**
 * Calculate the player's game score
 *
 * @author Feng
 */
public class ScoreCalculator {

    private final int mExcessSurface;
    private final double mExcessRisk;
    private final int mDistance;
    private final int mMinDist;

    public ScoreCalculator(ScoreBuilder sb) {
        mExcessSurface = sb.mSurfacing;
        mExcessRisk = sb.mRisk;
        mDistance = sb.mDistance;
        mMinDist = sb.mMinDist;
    }

    public Integer getScore() {
        System.out.println("Score Calculation Details: ");
        System.out.println();
        Double score = 100d; // 100d
        if (mExcessRisk > 0) {
            score -= SCORE_SCALE * mExcessRisk / GameConstants.RISK_BUDGET;
        }
        System.out.println("Excessive Risk: " + mExcessRisk);
        System.out.println("Risk Penalty: " + SCORE_SCALE * mExcessRisk / GameConstants.RISK_BUDGET);
        System.out.println();
        if (mExcessSurface > 0) {
            score -= mExcessSurface * SCORE_SCALE / GameConstants.SURFACING;
        }
        System.out.println("Excessive Surface: " + mExcessSurface);
        System.out.println("Surface Penalty: " + mExcessSurface * SCORE_SCALE / GameConstants.SURFACING);
        System.out.println();
        
        score -= SCORE_SCALE * (mDistance - mMinDist) / mMinDist;
        System.out.println("Minimum Distance Possible: " + mMinDist);
        System.out.println("Current Distance: " + mDistance);
        System.out.println("Distance Penalty: " + SCORE_SCALE * (mDistance - mMinDist) / mMinDist);
        System.out.println();
        System.out.println("Total Score: " + score);
        return score.intValue();
    }

    public static class ScoreBuilder {
 
        private int mSurfacing;
        private double mRisk;
        private int mDistance;
        private int mMinDist;

        public ScoreBuilder() {
        }

        public ScoreBuilder minDistance(int min) {
            mMinDist = min;
            return this;
        }

        public ScoreBuilder excessSurface(int sur) {
            mSurfacing = sur;
            return this;
        }

        public ScoreBuilder excessRisk(double risk) {
            mRisk = risk;
            return this;
        }

        public ScoreBuilder addDistance(int dist) {
            mDistance += dist;
            return this;
        }
        
        public ScoreBuilder clear(){
            mDistance = 0;
            mRisk = 0;
            mSurfacing = 0;
            mMinDist = 0;
            return this;
        }

        public ScoreCalculator build() {
            ScoreCalculator score = new ScoreCalculator(this);
            return score;
        }

    }

}
