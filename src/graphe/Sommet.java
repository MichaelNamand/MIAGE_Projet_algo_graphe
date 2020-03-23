package graphe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sample.Fenetre;

public class Sommet extends Parent {
    public final int id;
    public final String valeur;
    static public final double SOMMET_RADIUS = 20;
    double orgSceneX, orgSceneY;
    private Circle cercle;
    private StackPane stackPane;

    public Sommet(int id, double x, double y, String valeur, Graphe graphe) {
        this.id = id;
        this.valeur = valeur;

        stackPane = new StackPane();
        cercle = new Circle(20);
        cercle.setStroke(Color.BLACK);
        cercle.setStrokeWidth(2);
        cercle.setFill(Color.LIGHTBLUE);

        stackPane.getChildren().addAll(cercle, new Text(this.id + ""));
        stackPane.setLayoutX(x - 23);
        stackPane.setLayoutY(y - 43);
        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });

        // Gestion du déplacement d'un sommet
        stackPane.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            stackPane.setLayoutX(stackPane.getLayoutX() + offsetX);
            stackPane.setLayoutY(stackPane.getLayoutY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            for (Arc arc : graphe.getArcs()) {
                arc.setLine();
                arc.getArrow().update();
            }
        });
        // Création du menu contextuel lorsqu'on clique droit sur un sommet
        ContextMenu contextMenu = new ContextMenu();
        Image imagePoubelle = new Image(getClass().getResourceAsStream("../assets/icons/trash.png"));
        Image imageEdit = new Image(getClass().getResourceAsStream("../assets/icons/pencil.png"));
        MenuItem item1 = new MenuItem("Supprimer", new ImageView(imagePoubelle));
        MenuItem item2 = new MenuItem("Renommer", new ImageView(imageEdit));

        // Gestion du clic Supprimer
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphe.getSommets().remove(Sommet.this);
                graphe.getPane().getChildren().remove(Sommet.this);
            }
        });
        contextMenu.getItems().addAll(item1, item2);

        // Gestion du clic droit
        this.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(Sommet.this, event.getScreenX(), event.getScreenY());
            }
        });

        // Gestion du clic d'un sommet
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.PRIMARY && Fenetre.creerArcsEnclenche) {
                    if (graphe.getPremierSommetRelie() == null) {
                        graphe.setPremierSommetRelie(Sommet.this);
                        getCercle().setFill(Color.LIGHTGREEN);
                    } else if (graphe.getPremierSommetRelie().id == Sommet.this.id) {
                        getCercle().setFill(Color.LIGHTBLUE);
                        graphe.setPremierSommetRelie(null);
                    } else {
                        Arc arc = new Arc(1, graphe.getPremierSommetRelie(), Sommet.this, graphe);
                        if (graphe.ajouterArc(arc)) {
                            graphe.getPane().getChildren().add(arc);
                            graphe.getPremierSommetRelie().toFront(); Sommet.this.toFront();
                            graphe.getPremierSommetRelie().getCercle().setFill(Color.LIGHTBLUE);
                            getCercle().setFill(Color.LIGHTBLUE);
                            graphe.setPremierSommetRelie(null);
                        }
                    }
                }
            }
        });
        this.getChildren().add(stackPane);
    }

    public Circle getCercle() {
        return cercle;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public int id() {
        return id;
    }

    public String getValeur() {
        return valeur;
    }
}
