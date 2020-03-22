package sample;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class ArrowSecond extends Polygon {
    private Line line;
    double radius;

    public ArrowSecond(double[] points, Line line, double AnchorRadius) {
        super(points);
        this.line = line;
        this.radius = AnchorRadius + 7;
        initialize();

    }

    private void initialize() {
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

    public void update(){
        initialize();
    }
}