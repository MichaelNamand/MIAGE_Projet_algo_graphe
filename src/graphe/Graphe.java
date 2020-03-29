package graphe;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    private boolean creationSommetsEnclenches;
    private boolean creationArcsEnclenches;
    private boolean afficherNomsSommetsEnclenches;
    private boolean afficherCoutsArcsEnclenches;

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
                        t.getPickResult().getIntersectedNode() instanceof Text) && creationSommetsEnclenches &&
                        t.getButton() == MouseButton.PRIMARY) {
                    // Création du nouveau sommet
                    if (isAfficherNomsSommetsEnclenches()) {
                        TextInputDialog dialog = Fenetre.getTextInputFromDialog("Renseignez le nom du sommet :");
                        dialog.showAndWait().ifPresent(nomSommet -> {
                            // Ajout du sommet dans notre liste sommets et dans le pane
                            ajouterSommet(new Sommet(idIncrement, t.getSceneX(), t.getSceneY(), nomSommet, Graphe.this));
                        });
                    } else {
                        // Ajout du sommet dans notre liste sommets et dans le pane
                        ajouterSommet(new Sommet(idIncrement, t.getSceneX(), t.getSceneY(), "Sans nom", Graphe.this));
                    }
                    idIncrement++;
                }
            }
        });

        this.getChildren().add(pane);
    }

    public void ajouterSommet(Sommet s) {
        sommets.add(s);
        pane.getChildren().add(s);
    }

    public ArrayList<Arc> getArcs() {
        return arcs;
    }

    public boolean ajouterArc(Arc arc) {
        for (Arc a : arcs) {
            if (a.getDepart().id == arc.getDepart().id && a.getArrivee().id == arc.getArrivee().id) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impossible de créer l'arc");
                alert.setHeaderText("Le sommet " + a.getDepart().id + " est déjà relié au sommet " + a.getDepart().id + " !");
                alert.showAndWait();
                premierSommetRelie = null;
                return false;
            }
        }
        arcs.add(arc);
        pane.getChildren().add(arc);
        arc.getDepart().getCercle().setFill(Color.LIGHTBLUE);
        premierSommetRelie = null;
        arc.getDepart().toFront(); arc.getArrivee().toFront();
        return true;
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    public Sommet getSommet(int id) {
        for (Sommet s : sommets) {
            if (s.id == id) {
                return s;
            }
        }
        return null;
    }

    public Arc getArcFromSommet(Sommet s) {
        for (Arc arc : arcs) {
            if (arc.getDepart().id() == s.id || arc.getArrivee().id() == s.id) {
                return arc;
            }
        }
        return null;
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        String s = "";
        for (Sommet som : sommets) {
            s += "{id : " + som.id() + ", Valeur : " + som.getValeur() + ", X : " + som.getX() + ", Y : " + som.getY() + "}\n";
        }
        String a = "";
        for (Arc arc : arcs) {
            a += "{S1 : " + arc.getDepart().id() + ", S2 : " + arc.getArrivee().id() + ", cout " + arc.getCout() + "}\n";
        }
        return "Graphe{" +
                "sommets=" + s +
                ", arcs=" + a +
                ", nom='" + nom + '\'' +
                '}';
    }

    public void setCreationSommetsEnclenches(boolean creationSommetsEnclenches) {
        this.creationSommetsEnclenches = creationSommetsEnclenches;
    }

    public void setCreationArcsEnclenches(boolean creationArcsEnclenches) {
        this.creationArcsEnclenches = creationArcsEnclenches;
    }

    public void setAfficherNomsSommetsEnclenches(boolean afficherNomsSommetsEnclenches) {
        this.afficherNomsSommetsEnclenches = afficherNomsSommetsEnclenches;
        for (Sommet s : sommets) {
            s.setStyleSommet(afficherNomsSommetsEnclenches);
        }
        Fenetre.rafraichirInterface();
        for (Arc a : arcs) {
            a.setLine();
            a.getArrow().update();
        }
    }

    public void setAfficherCoutsArcsEnclenches(boolean afficherCoutsArcsEnclenches) {
        this.afficherCoutsArcsEnclenches = afficherCoutsArcsEnclenches;
        for (Arc a : arcs) {
            if (afficherCoutsArcsEnclenches) {
                a.afficherCouts();
            } else {
                a.masquerCouts();
            }
        }

    }

    public boolean isCreationSommetsEnclenches() {
        return creationSommetsEnclenches;
    }

    public boolean isCreationArcsEnclenches() {
        return creationArcsEnclenches;
    }

    public boolean isAfficherNomsSommetsEnclenches() {
        return afficherNomsSommetsEnclenches;
    }

    public boolean isAfficherCoutsArcsEnclenches() {
        return afficherCoutsArcsEnclenches;
    }
}
