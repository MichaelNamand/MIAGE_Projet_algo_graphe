package graphe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import sample.ArrowSecond;

import static graphe.Sommet.SOMMET_RADIUS;

public class Arc extends Parent {
    private final int cout;
    private final Sommet depart;
    private final Sommet arrivee;
    private ArrowSecond arrow;
    private Line line;

    public Arc(int cout, Sommet depart, Sommet arrivee, Graphe graphe) {
        this.cout = cout;
        this.depart = depart;
        this.arrivee = arrivee;
        line = new Line();
        setLine();
        line.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    // Création du menu contextuel lorsqu'on clique droit sur un sommet
                    ContextMenu contextMenu = new ContextMenu();
                    Image imagePoubelle = new Image(getClass().getResourceAsStream("/assets/icons/trash.png"));
                    MenuItem item1 = new MenuItem("Supprimer", new ImageView(imagePoubelle));

                    // Gestion du clic Supprimer
                    item1.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            graphe.getArcs().remove(Arc.this);
                            graphe.getPane().getChildren().remove(Arc.this);
                        }
                    });
                    contextMenu.getItems().addAll(item1);

                    // Gestion du clic droit
                    Arc.this.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent event) {
                            contextMenu.show(Arc.this, event.getScreenX(), event.getScreenY());
                        }
                    });
                }
            }
        });

        double[] points = {0.0, 7.0, -7.0, -7.0, 7.0, -7.0};
        arrow = new ArrowSecond(points, line, SOMMET_RADIUS);
        this.getChildren().addAll(arrow);
        this.getChildren().addAll(line);
    }

    public void setLine() {
        StackPane sDepart = depart.getStackPane();
        StackPane sArrivee = arrivee.getStackPane();

        double centerDepartX = (sDepart.getWidth() / 2) + sDepart.getLayoutX();
        double centerDepartY = (sDepart.getWidth() / 2) + sDepart.getLayoutY();
        double centerArriveeX = (sArrivee.getWidth() / 2) + sArrivee.getLayoutX();
        double centerArriveeY = (sArrivee.getWidth() / 2) + sArrivee.getLayoutY();

        line.setStartX(centerDepartX);
        line.setStartY(centerDepartY);
        line.setEndX(centerArriveeX);
        line.setEndY(centerArriveeY);
        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK);
    }

    public int getCout() {
        return cout;
    }

    public Sommet getDepart() {
        return depart;
    }

    public Sommet getArrivee() {
        return arrivee;
    }

    public ArrowSecond getArrow() {
        return arrow;
    }
}
