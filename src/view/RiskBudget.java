/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Feng
 */
public class RiskBudget {

    private Rectangle mBackgroundRect;
    private Rectangle mCurrRect;
    private Rectangle mCurrAttemptRect;
    private Rectangle mPrevAttemptRect;

    private Polygon mCurrTri;
    private Polygon mCurrAttemptTri;
    private Polygon mPrevAttemptTri;
    private int mBarHeight;
    private int mBarWidth;

    public RiskBudget(int maxwidth, int height) {
        mBarHeight = height;
        mBarWidth = maxwidth;
    }

    private Rectangle getStandardRect(Color color) {
        // The upper left corner is at (0,0) in the parent node
        Rectangle rect = new Rectangle(0, 0, 0, mBarHeight);
        rect.setFill(color);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        return rect;
    }

    private Polygon getStandardTriangle(Color color, Double offset) {
        Polygon tri = new Polygon();
        tri.getPoints().addAll(new Double[]{
            offset, 0d,
            offset - 5, -20d,
            offset + 5, -20d});
        tri.setFill(color);
        return tri;
    }

    public Rectangle getBackgroundRect(Color color) {
        mBackgroundRect = getStandardRect(color);
        mBackgroundRect.setWidth(mBarWidth);
        return mBackgroundRect;
    }

    public Rectangle getCurrRect(double ratio) {
        mCurrRect = getStandardRect(ColorfulPath.CURRCOLOR);
        mCurrRect.setWidth(ratio * mBarWidth);
        return mCurrRect;
    }

    public Polygon getCurrTri(double ratio) {
        mCurrTri = getStandardTriangle(ColorfulPath.CURRCOLOR, ratio * mBarWidth);
        return mCurrTri;
    }

    public Rectangle getCurrAttemptRect(double ratio) {
        mCurrAttemptRect = getStandardRect(ColorfulPath.ATMPCOLOR);
        mCurrAttemptRect.setWidth(ratio * mBarWidth);
        return mCurrAttemptRect;
    }

    public Polygon getCurrAttemptTri(double ratio) {
        mCurrAttemptTri = getStandardTriangle(ColorfulPath.ATMPCOLOR, ratio * mBarWidth);
        return mCurrAttemptTri;
    }

    public Rectangle getPrevAttemptRect(double ratio) {
        mPrevAttemptRect = getStandardRect(ColorfulPath.PREVCOLOR);
        mPrevAttemptRect.setWidth(ratio * mBarWidth);
        return mPrevAttemptRect;
    }

    public Polygon getPrevAttemptTri(double ratio) {
        mPrevAttemptTri = getStandardTriangle(ColorfulPath.PREVCOLOR, ratio * mBarWidth);
        return mPrevAttemptTri;
    }

}
