package sample;

import graphe.Sommet;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class ArrowSecond extends Polygon {
    private Line line;
    double radius;
    public static int DECALAGE_POINTE = 7;

    public ArrowSecond(double[] points, Line line) {
        super(points);
        this.line = line;
        initialize();
    }

    private void initialize() {
        radius = Sommet.SOMMET_RADIUS + DECALAGE_POINTE;
        double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;

        double height = line.getEndY() - line.getStartY();
        double width = line.getEndX() - line.getStartX();
        double length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));

        double subtractWidth = radius * width / length;
        double subtractHeight = radius * height / length;

        setRotate(angle - 90);
        setTranslateX(line.getStartX());
        setTranslateY(line.getStartY());
        setTranslateX(line.getEndX() - subtractWidth);
        setTranslateY(line.getEndY() - subtractHeight);
    }

    public Line getLine() {
        return line;
    }

    public void update(){
        initialize();
    }
}