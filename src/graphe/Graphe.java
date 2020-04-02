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
import java.util.Collections;

/**
 * Graphe
 * Cette classe gère toute la partie affichage et gestion d'un graphe.
 * Elle gère donc les sommets et les arcs définissant un graphe et son interface utilisateur.
 * Grâce à cette classe, on peut gérer la création, modification et suppression d'un arc ou sommet graphiquement et logiquement.
 */
public class Graphe extends Parent {
    // Modèle d'un graphe :
    private ArrayList<Sommet> sommets = new ArrayList<>();
    private ArrayList<Arc> arcs = new ArrayList<>();
    private String nom;

    // Elements graphiques
    private Sommet premierSommetRelie = null;       // Sommet tampon pour la création d'arc
    public static int idIncrement = 1;              // Id static représentant aussi le nombre de sommets du graphe

    private boolean creationSommetsEnclenches;      // Statut du bouton "Créer des sommets"
    private boolean creationArcsEnclenches;         // Statut du bouton "Créer des arcs"
    private boolean afficherNomsSommetsEnclenches;  // Statut de la checkbox "Afficher le nom des sommets"
    private boolean afficherRangSommetsEnclenches;  // Statut de la checkbox "Afficher le rang des sommets"
    private boolean afficherCoutsArcsEnclenches;    // Statut de la checkbox "Afficher le coût des arcs"

    private Pane pane;                              // Pane contenant les sommets et les arcs  du graphe

    public Graphe(String nom) {
        this.nom = nom;

        // Définitions des éléments graphiques
        pane = new Pane();
        pane.setStyle("-fx-background-color:lightgray");
        pane.setPrefWidth(1277);
        pane.setPrefHeight(670);

        // Ajout d'un click listener au pane
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
                            ajouterSommet(new Sommet(idIncrement, t.getSceneX(), t.getSceneY(), -1, nomSommet, Graphe.this));
                        });
                    } else {
                        // Ajout du sommet dans notre liste sommets et dans le pane
                        ajouterSommet(new Sommet(idIncrement, t.getSceneX(), t.getSceneY(), -1, "Sans nom", Graphe.this));
                    }
                    Fenetre.changementsEffectues = true;
                    // A chaque création d'un sommet, idIncrement s'incrémente pour définir le nouvel id d'un futur sommet créé
                    idIncrement++;
                }
            }
        });

        // Ajout du pane à la fenêtre
        this.getChildren().add(pane);
    }

    /**
     * Ajoute un sommet dans le graphe.
     * D'abord on l'ajoute dans la liste sommets puis dans la fenêtre graphique.
     * @param s
     */
    public void ajouterSommet(Sommet s) {
        sommets.add(s);
        pane.getChildren().add(s);
    }

    public ArrayList<Arc> getArcs() {
        return arcs;
    }

    /**
     * Ajoute un arc dans le graphe, puis on l'affiche graphiquement
     * @param arc
     * @return true si l'ajout s'est effectué | false sinon
     */
    public boolean ajouterArc(Arc arc) {
        for (Arc a : arcs) {
            // Si un arc dans la liste d'arcs du graphe a les mêmes sommets départ et arrivé, on ne l'ajoute pas
            // et on avertir l'utilisateur
            if (a.getDepart().id() == arc.getDepart().id() && a.getArrivee().id() == arc.getArrivee().id()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impossible de créer l'arc");
                alert.setHeaderText("Le sommet " + a.getDepart().id() + " est déjà relié au sommet " + a.getDepart().id() + " !");
                alert.showAndWait();
                premierSommetRelie = null;
                return false;
            }
        }
        arcs.add(arc);
        pane.getChildren().add(arc);
        // Si l'utilisateur a enclenché le bouton "Créer des arcs", on colore le sommet dans sa couleur initiale : LIGHTBLUE
        if (isCreationArcsEnclenches())
            arc.getDepart().getCercle().setFill(Color.LIGHTBLUE);
        // On vide le sommet tampon permettant de sauvegarder le 1er sommet cliqué
        premierSommetRelie = null;
        // Met au premier plan l'arc (sa ligne + sa flèche)
        arc.getDepart().toFront(); arc.getArrivee().toFront();
        return true;
    }

    public int[][] getFsAps() {
        int[] aps = new int[sommets.size() + 1];
        int[] fs = new int[arcs.size() + sommets.size() + 1];
        aps[0] = sommets.size();
        fs[0] = arcs.size() + sommets.size();

        int i = 1, j = 1;
        for (Sommet sommet : sommets) {
            aps[j] = i;
            j++;
            for (Sommet successeur : getSuccesseursSommet(sommet)) {
                fs[i] = successeur.id();
                i++;
            }
            fs[i] = 0;
            i++;
        }
        int[][] res = new int[2][];
        res[0] = fs; res[1] = aps;
        return res;
    }

    public int[][] getMatAdj() {
        int[][] matAdj = new int[sommets.size() + 1][sommets.size() + 1];
        for(int i = 1; i < sommets.size() + 1; i++) {
            for(int j = 1; j < sommets.size() + 1; j++) {
                matAdj[i][j] = 0;
            }
        }
        for(int i = 0; i < arcs.size(); i++) {
            matAdj[arcs.get(i).getDepart().id()][arcs.get(i).getArrivee().id()] = arcs.get(i).getCout();
        }
        return matAdj;
    }

    public void afficheMatAdj() {
        int[][] matAdj = getMatAdj();
        System.out.println("Matrice d'adjacence :");
        for(int i = 1; i < matAdj.length; i++) {
            System.out.print("| ");
            for(int j = 1; j < matAdj[i].length; j++) {
                System.out.print(matAdj[i][j]+" | ");
            }
            System.out.println();
        }
    }

    public void afficheFsAps() {
        int[][] fsAps = getFsAps();
        System.out.print("FS :\n| ");
        for(int i = 1; i < fsAps[0].length; i++) {
            System.out.print(fsAps[0][i]+" | ");
        }
        System.out.print("\nAPS :\n| ");
        for(int i = 1; i < fsAps[1].length; i++) {
            System.out.print(fsAps[1][i]+" | ");
        }
        System.out.println();
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    public Sommet getSommet(int id) {
        for (Sommet s : sommets) {
            if (s.id() == id) {
                return s;
            }
        }
        return null;
    }

    public Arc getArcFromSommet(Sommet s) {
        for (Arc arc : arcs) {
            if (arc.getDepart().id() == s.id() || arc.getArrivee().id() == s.id()) {
                return arc;
            }
        }
        return null;
    }
    public Arc getArcFromSommets(Sommet depart, Sommet arrive) {
        for (Arc arc : arcs) {
            if (arc.getDepart().equals(depart) && arc.getArrivee().equals(arrive)) {
                return arc;
            }
        }
        return null;
    }

    public Sommet getPremierSommetRelie() {
        return premierSommetRelie;
    }

    /**
     * Retourne la liste de tous les successeurs du sommet passé en paramètre
     * @param s
     * @return
     */
    public ArrayList<Sommet> getSuccesseursSommet(Sommet s) {
        ArrayList<Sommet> successeurs = new ArrayList<>();
        Collections.sort(arcs);
        for (Arc arc : arcs) {
            if (arc.getDepart().id() == s.id()) {
                successeurs.add(arc.getArrivee());
            }
        }
        return successeurs;
    }

    /**
     * Retourne la liste de tous les prédécesseurs du sommet passé en paramètre
     * @param s
     * @return
     */
    public ArrayList<Sommet> getPredecesseursSommet(Sommet s) {
        ArrayList<Sommet> predecesseurs = new ArrayList<>();
        Collections.sort(arcs);
        for (Arc arc : arcs) {
            if (arc.getArrivee().id() == s.id()) {
                predecesseurs.add(arc.getDepart());
            }
        }
        return predecesseurs;
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

    /**
     * Affiche ou non le nom (la valeur) d'un sommet plutôt que son id
     * @param afficherNomsSommetsEnclenches
     */
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
        setAfficherCoutsArcsEnclenches(isAfficherCoutsArcsEnclenches());
    }

    /**
     * Affiche ou non le rang des sommets
     * @param afficherRangSommetsEnclenches
     */
    public void setAfficherRangSommetsEnclenches(boolean afficherRangSommetsEnclenches) {
        this.afficherRangSommetsEnclenches = afficherRangSommetsEnclenches;
        for (Sommet s : getSommets()) {
            s.getRangAffichage().setVisible(afficherRangSommetsEnclenches);
        }
    }

    /**
     * Affiche ou non le coût les arcs
     * @param afficherCoutsArcsEnclenches
     */
    public void setAfficherCoutsArcsEnclenches(boolean afficherCoutsArcsEnclenches) {
        this.afficherCoutsArcsEnclenches = afficherCoutsArcsEnclenches;
        for (Arc a : arcs) {
            if (afficherCoutsArcsEnclenches) {
                a.afficherCouts();
            } else {
                a.masquerCouts();
            }
        }
        Fenetre.rafraichirInterface();
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

    public boolean isAfficherRangSommetsEnclenches() {
        return afficherRangSommetsEnclenches;
    }

    /**
     * Après lancement d'un algorithme, la couleur des arcs et sommets a pu être modifiée. Cette méthode rétablit
     * leur couleur par défaut.
     */
    public void retablirAffichage() {
        for (Arc arc : arcs) {
            arc.resetArcDisplay();
        }
        for (Sommet sommet : sommets) {
            sommet.getCercle().setFill(Color.LIGHTBLUE);
        }
    }

}
