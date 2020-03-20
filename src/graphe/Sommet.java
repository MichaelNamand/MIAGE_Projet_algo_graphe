package graphe;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Sommet extends Parent {
    public final int id;
    public final String valeur;

    public Sommet(int id, double x, double y, String valeur) {
        this.id = id;
        this.valeur = valeur;

        StackPane stackPane = new StackPane();
        Circle sommet = new Circle(20);
        sommet.setStroke(Color.BLACK);
        sommet.setStrokeWidth(2);
        sommet.setFill(Color.LIGHTBLUE);

        sommet.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {

            }
        });
        stackPane.getChildren().add(sommet);
        stackPane.getChildren().add(new Text(this.id + ""));
        stackPane.setLayoutX(x - 20);
        stackPane.setLayoutY(y - 20);
        this.getChildren().add(stackPane);
    }

    public int id() {
        return id;
    }

    public String getValeur() {
        return valeur;
    }
}
