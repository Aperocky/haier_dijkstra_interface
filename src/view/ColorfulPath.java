/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static constant.GameConstants.SINGLE_STEP;
import model.map.Edge;
import model.map.Map;
import model.map.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import riskawarenesssimulation.Controller.MapController;

/**
 * Responsible for creating views for {@link model.map.Edge}, including a whole
 * collection of edges which represent a path Drawing types include: current
 * path, attempting path, previous attempted path
 *
 * @author Feng
 */
public class ColorfulPath extends Painter{

    public static final Color BGCOLOR = Color.GREEN;
    public static final Color CURRCOLOR = Color.BLACK;
    public static final Color ATMPCOLOR = Color.WHITE;
    public static final Color PREVCOLOR = Color.GRAY;
    public static final Color ALERTCOLOR = Color.YELLOW;
    public static final Color DEPLETECOLOR = Color.RED;
    public static final int PATHWIDTH = 6;


    public Collection<Line> drawPath(Collection<Edge> edges, Color color) {
        if (edges == null || edges.isEmpty()) {
            return null;
        }
        ArrayList<Line> lines = new ArrayList<>();
        for (Edge edge : edges) {
            lines.add(edgeToLine(edge, color, PATHWIDTH));
        }
        return lines;
    }

    public Collection<Circle> drawUncertainty(int step, List<Node> nodes) {
        int radius1 = Map.SPACING * 1;
        int radius2 = Map.SPACING * 2;
        int radius3 = Map.SPACING * 3;
        Collection<Circle> circles = new ArrayList<Circle>();
        circles.add(nodeToTransCircle(nodes.get(0), radius1));
        if (step / SINGLE_STEP >= 2) {
            circles.add(nodeToTransCircle(nodes.get(1), radius2));
        }
        if (step / SINGLE_STEP >= 3) {
            circles.add(nodeToTransCircle(nodes.get(2), radius3));
        }

        return circles;
    }

    public Collection<Line> drawCurrentPath(Collection<Edge> edges) {
        return drawPath(edges, CURRCOLOR);
    }

    public Collection<Line> drawCurrAttemptPath(Collection<Edge> edges) {
        return drawPath(edges, ATMPCOLOR);
    }

    public Collection<Line> drawPrevAttemptPath(Collection<Edge> edges) {
        return drawPath(edges, PREVCOLOR);
    }


    
    

}
