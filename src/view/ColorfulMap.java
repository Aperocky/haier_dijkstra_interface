/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.map.Node;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 * Responsible for creating {@link model.map.Node} views, including current node,
 * destination node and dangerous blocks on the map
 *
 * @author Feng
 */
public class ColorfulMap extends Painter{

    public static final int SQUARE_SIZE = 14;   //30
    public static final int CIRCLE_RADIUS = 6; //13 
    public static final Color BLOCKCOLOR = Color.RED;
    public static final Color DESTINCOLOR = Color.WHITE;


    public Collection<Shape> drawMap(Collection<Node> blocks) {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (Node node : blocks) {
            if (node.isDanger()) {
                shapes.add(nodeToCircle(node, BLOCKCOLOR, CIRCLE_RADIUS, 1));
            }
        }
        return shapes;
    }

    public Shape drawDestination(Node end) {
        return nodeToTriangle(end, DESTINCOLOR, SQUARE_SIZE);
    }

    public Shape drawCurrent(Node current) {
        return nodeToSquare(current, ColorfulPath.CURRCOLOR, SQUARE_SIZE);
    }

    public Shape drawPlanned(Node planned) {
        return nodeToSquare(planned, ColorfulPath.PREVCOLOR, SQUARE_SIZE);
    }
    
}
