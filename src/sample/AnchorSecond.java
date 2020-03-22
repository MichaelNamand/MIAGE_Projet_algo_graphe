package sample;

import graphe.Arc;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class AnchorSecond extends Circle {
    private double x, y;
    Arc arc;

    public AnchorSecond(Color color, DoubleProperty xx, DoubleProperty yy, double radius, Arc arc) {
        //      x point     y point     radius
        super(xx.get(), yy.get(), radius);

        this.arc = arc;

        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);

        xx.bind(centerXProperty());
        yy.bind(centerYProperty());

        setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                x = getCenterX() - mouseEvent.getX();
                y = getCenterY() - mouseEvent.getY();
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event)
            {
                setCenterX(event.getSceneX() - x);
                setCenterY(event.getSceneY() - y);

                //  update arrow positions when circles are dragged
                arc.getArrow().update();
            }
        });
    }
}