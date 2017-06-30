/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.map.Edge;
import model.map.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Feng
 */
public class Painter {

    public Line edgeToLine(Edge edge) {
        return edgeToLine(edge, Color.BLUE, 2);
    }

    public Line edgeToLine(Edge edge, Color color, int strokewidth) {
        Node nodeA = edge.getNodeA();
        Node nodeB = edge.getNodeB();
        Line line = new Line(nodeA.getX(), nodeA.getY(),
                nodeB.getX(), nodeB.getY());
        line.setStroke(color);
        line.setStrokeWidth(strokewidth);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        return line;
    }

    protected Circle nodeToCircle(Node node) {
        return nodeToCircle(node, Color.BLUE, 5, 2);
    }

    protected Circle nodeToCircle(Node node, Color color, int r, int stroke) {
        Circle circle = new Circle(node.getX(), node.getY(), r, Color.WHITE);
        circle.setStroke(color);
        circle.setFill(color);
        circle.setStrokeWidth(stroke);
        circle.setStrokeType(StrokeType.OUTSIDE);
        return circle;
    }

    protected Circle nodeToTransCircle(Node node, int radius) {
        if (node == null) {
            return null;
        }
        Circle cir = new Circle(radius);
        cir.setFill(new Color(1, 1, 0, 0.4f));
        cir.setCenterX(node.getX());
        cir.setCenterY(node.getY());
        cir.setStrokeWidth(4);
        return cir;
    }

    protected Rectangle nodeToRect(Node node) {
        Rectangle rect = new Rectangle(node.getX(), node.getY(), 10, 10);
        return rect;
    }

    protected Rectangle nodeToSquare(Node node, Color color, int size) {
        if (node == null) {
            return null;
        }
        Rectangle rect = new Rectangle(node.getX() - size / 2, node.getY() - size / 2, size, size);
        rect.setFill(color);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        return rect;
    }

    protected Polygon nodeToTriangle(Node node, Color color, int size) {
        if (node == null) {
            return null;
        }
        Polygon tri = new Polygon();
        double height = Math.pow(3, 1 / 2d) * size / 2;
        double width = size;
        tri.getPoints().addAll(new Double[]{
            0.0, 0.0,
            width, 0.0,
            width / 2, height});
        tri.setFill(color);
        tri.setStrokeLineCap(StrokeLineCap.ROUND);
        tri.setLayoutX(node.getX() - width / 2);
        tri.setLayoutY(node.getY() - height / 2);
        return tri;

    }

}
