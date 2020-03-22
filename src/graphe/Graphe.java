package graphe;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sample.Fenetre;

import java.util.ArrayList;

public class Graphe extends Parent {
    private ArrayList<Sommet> sommets = new ArrayList<>();
    private ArrayList<Arc> arcs = new ArrayList<>();
    private String nom;
    private Sommet premierSommetRelie = null;
    public static int idIncrement = 1;

    private Pane pane;

    public Graphe(String nom) {
        this.nom = nom;
        pane = new Pane();
        pane.setStyle("-fx-background-color:lightgray");
        pane.setPrefWidth(1277);
        pane.setPrefHeight(670);
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                // Si on clique sur un sommet avec un clic gauche avec le bouton "Créer des sommets" enclenché :
                if (!(t.getPickResult().getIntersectedNode() instanceof Circle ||
                        t.getPickResult().getIntersectedNode() instanceof Text) && Fenetre.creerSommetsEnclenche &&
                        t.getButton() == MouseButton.PRIMARY) {
                    // Création du nouveau sommet
                    Sommet s = new Sommet(idIncrement, t.getSceneX() - 3, t.getSceneY() - 30, "test", Graphe.this);
                    idIncrement++;
                    // Ajout du sommet dans notre liste sommets et dans le pane
                    sommets.add(s);
                    pane.getChildren().add(s);
                }
            }
        });

        this.getChildren().add(pane);
    }

    public ArrayList<Arc> getArcs() {
        return arcs;
    }

    public boolean ajouterArc(Arc arc) {
        for (Arc a : arcs) {
            if (a.depart.id == arc.depart.id && a.arrivee.id == arc.arrivee.id) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impossible de créer l'arc");
                alert.setHeaderText("Le sommet " + a.depart.id + " est déjà relié au sommet " + a.arrivee.id + " !");
                alert.showAndWait();
                premierSommetRelie = null;
                return false;
            }
        }
        arcs.add(arc);
        return true;
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    public Sommet getPremierSommetRelie() {
        return premierSommetRelie;
    }

    public void setPremierSommetRelie(Sommet premierSommetRelie) {
        this.premierSommetRelie = premierSommetRelie;
    }

    public Pane getPane() {
        return pane;
    }
}
