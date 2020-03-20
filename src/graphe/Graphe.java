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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sample.Fenetre;

import java.util.ArrayList;

public class Graphe extends Parent {
    private ArrayList<Sommet> sommets = new ArrayList<>();
    private Arc[] arcs;
    private String nom;

    public Graphe(String nom) {
        this.nom = nom;
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:lightgray");
        pane.setPrefWidth(1277);
        pane.setPrefHeight(670);
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                // Si on clique sur un sommet avec un clic gauche avec le bouton "Créer des sommets" enclenché :
                if (!(t.getPickResult().getIntersectedNode() instanceof Circle) && Fenetre.creerSommetsEnclenche &&
                        t.getButton() == MouseButton.PRIMARY) {
                    // Création du nouveau sommet
                    Sommet s = new Sommet(sommets.size() + 1, t.getX(), t.getY(), "test");

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
                            sommets.remove(s);
                            pane.getChildren().remove(s);
                        }
                    });
                    contextMenu.getItems().addAll(item1, item2);

                    // Gestion du clic droit
                    s.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent event) {
                            contextMenu.show(s, event.getScreenX(), event.getScreenY());
                        }
                    });

                    // Ajout du sommet dans notre liste sommets et dans le pane
                    sommets.add(s);
                    pane.getChildren().add(s);
                }
            }
        });

        this.getChildren().add(pane);
    }
}
