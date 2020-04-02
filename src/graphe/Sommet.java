package graphe;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sample.Fenetre;

import java.util.Objects;

/**
 * Sommet
 * Gère la partie affichage et gestion d'un sommet.
 */
public class Sommet extends StackPane {
    private int id;
    private String valeur;
    private int rang;

    static public double SOMMET_RADIUS = 20;    // Diamètre du cercle du sommet
    public double x, y;                         // Coordonnées du sommet dans le graphe
    private Circle cercle;                      // Cercle (élément graphique) composant le sommet
    private Text nomAffichage;                  // Texte (élément graphique) affichant la valeur / id du sommet
    private Text dureeAffichage;                // Texte (élément graphique) affichant la durée / longueur du sommet
    private StackPane rangAffichage;            // StackPane (élément graphique) affichant le rang du sommet

    public Sommet(int id, String valeur, Graphe g) {
        this(id, 0, 0, 0, valeur, g);
    }

    public Sommet(int id, double x, double y, int rang, String valeur, Graphe graphe) {
        this.id = id;
        this.valeur = valeur;
        this.rang = rang;

        // Coordonnées du sommet dans le graphe
        this.x = x; this.y = y;
        // Texte affiché sur le sommet (id ou valeur)
        nomAffichage = new Text(graphe.isAfficherNomsSommetsEnclenches() ? valeur : id + "");

        // Création de l'affichage du rang du sommet
        rangAffichage = new StackPane();
        // L'affichage est composé d'un texte...
        Text textRang = new Text(rang == -1 ? "+∞" : rang + "");
        // et d'un rectangle entourant le texte
        Rectangle rectangleRang = new Rectangle(18, 18, Color.LIGHTGRAY);
        rectangleRang.setStrokeWidth(1);
        rectangleRang.setStroke(Color.BLACK);

        rangAffichage.getChildren().addAll(rectangleRang, textRang);

        // Création du contenu graphique du sommet (cercle et positionnement)
        cercle = new Circle();
        cercle.setStroke(Color.BLACK);
        cercle.setStrokeWidth(2);
        cercle.setFill(Color.LIGHTBLUE);

        dureeAffichage = new Text();
        // Ajout du contenu graphique dans le StackPane (layout)
        getChildren().addAll(cercle, nomAffichage, rangAffichage, dureeAffichage);
        setStyleSommet(graphe.isAfficherNomsSommetsEnclenches());
        rangAffichage.setVisible(graphe.isAfficherRangSommetsEnclenches());

        setCursor(Cursor.HAND);
        setOnMousePressed((t) -> {
            this.x = t.getSceneX();
            this.y = t.getSceneY();
        });
        setPrefHeight(44);
        // Gestion du déplacement d'un sommet. On doit mettre à jour non seulement la position du sommet mais aussi ses
        // éventuels arcs.
        setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - this.x;
            double offsetY = t.getSceneY() - this.y;

            setLayoutX(getLayoutX() + offsetX);
            setLayoutY(getLayoutY() + offsetY);

            this.x = t.getSceneX();
            this.y = t.getSceneY();

            for (Arc arc : graphe.getArcs()) {
                arc.setLine();
                arc.getArrow().update();
            }
            Fenetre.changementsEffectues = true;
        });
        // Création du menu contextuel lorsqu'on clique droit sur un sommet
        ContextMenu contextMenu = new ContextMenu();
        Image imagePoubelle = new Image(getClass().getResourceAsStream("/assets/icons/trash.png"));
        Image imageEdit = new Image(getClass().getResourceAsStream("/assets/icons/pencil.png"));
        MenuItem item1 = new MenuItem("Supprimer", new ImageView(imagePoubelle));
        MenuItem item2 = new MenuItem("Renommer", new ImageView(imageEdit));

        // Gestion du clic Supprimer
        item1.setOnAction(event -> {
            Graphe.idIncrement--;
            // Avant suppression, on réaffecte les ids des autres sommets supérieurs à celui qu'on supprime
            for (Sommet s : graphe.getSommets()) {
                if (s.id() > Sommet.this.id) {
                    s.decrementeId();
                    s.setStyleSommet(graphe.isAfficherNomsSommetsEnclenches());
                }
            }
            graphe.getSommets().remove(Sommet.this);

            // On supprime tous les arcs reliés à ce sommet
            while (graphe.getArcFromSommet(Sommet.this) != null) {
                Arc a = graphe.getArcFromSommet(Sommet.this);
                graphe.getPane().getChildren().remove(a);
                graphe.getArcs().remove(a);
            }
            Fenetre.changementsEffectues = true;
            // Et on l'enlève graphiquement du graphe
            graphe.getPane().getChildren().remove(Sommet.this);
        });
        // Gestion du clic Renommer
        item2.setOnAction(event -> {
            TextInputDialog dialog = Fenetre.getTextInputFromDialog("Donnez un nouveau nom au sommet");
            dialog.showAndWait().ifPresent(t -> {
                nomAffichage.setText(t);
                Sommet.this.setValeur(t);
            });
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
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                // Si le bouton "Créer des arcs" est enclenché
                if(t.getButton() == MouseButton.PRIMARY && graphe.isCreationArcsEnclenches()) {
                    // On regarde s'il a déjà cliqué sur un ancien sommet
                    if (graphe.getPremierSommetRelie() == null) {
                        // Si non, on enregistre le premier sommet
                        graphe.setPremierSommetRelie(Sommet.this);
                        getCercle().setFill(Color.LIGHTGREEN);
                    } else if (graphe.getPremierSommetRelie().id == Sommet.this.id) {
                        // S'il reclique sur le même sommet, on annule la création de l'arc
                        getCercle().setFill(Color.LIGHTBLUE);
                        graphe.setPremierSommetRelie(null);
                    } else {
                        // Si oui, on créé l'arc et on l'ajoute graphiquement au graphe
                        Arc arc = new Arc(1, graphe.getPremierSommetRelie(), Sommet.this, graphe);
                        if (graphe.ajouterArc(arc)) {
                            getCercle().setFill(Color.LIGHTBLUE);
                            if (graphe.isAfficherCoutsArcsEnclenches())
                                Fenetre.rafraichirInterface();
                            arc.setLine();
                            Fenetre.changementsEffectues = true;
                        }
                    }
                }
            }
        });

        setLayoutX(x - getSommetRadius());
        setLayoutY(y - getSommetRadius() * 2);
    }

    /**
     * Met à jour le noeud dureeAffichage avec le texte passé en paramètre
     * @param duree
     */
    public void setDureeAffichage(int duree) { dureeAffichage.setText("(" + duree + ")"); }

    public StackPane getRangAffichage() {
        return rangAffichage;
    }

    public int getRang() {
        return rang;
    }

    public void decrementeId() {
        id--;
    }

    /**
     * Met à jour le noeud affichant le rang du sommet
     * @param rang
     */
    public void setRang(int rang) {
        this.rang = rang;
        Text text = (Text) rangAffichage.getChildren().get(1);
        if (rang == -1) {
            text.setText("+∞");
        } else {
            text.setText(rang + "");
        }
    }

    public void setNomAffichageSommet(String nom) { nomAffichage.setText(nom); }

    public double getSommetRadius() { return cercle.getRadius() + cercle.getStrokeWidth() * 2; }

    public Circle getCercle() { return cercle; }

    public double getX() { return x; }

    public double getY() { return y; }

    public int id() { return id; }

    public String getValeur() { return valeur; }

    public void setValeur(String valeur) { this.valeur = valeur; }

    /**
     * Suivant les paramètres utilisateur, un sommet a deux types d'affichage : L'un avec sa valeur, l'autre
     * avec son id.
     * @param parValeur
     */
    public void setStyleSommet(boolean parValeur) {
        if (parValeur) {
            Sommet.SOMMET_RADIUS = 5;
            setNomAffichageSommet(getValeur());
            StackPane.setAlignment(nomAffichage, Pos.BOTTOM_CENTER);
            cercle.setRadius(Sommet.SOMMET_RADIUS);
        } else {
            Sommet.SOMMET_RADIUS = 20;
            setNomAffichageSommet(id() + "");
            StackPane.setAlignment(nomAffichage, Pos.CENTER);
            cercle.setRadius(Sommet.SOMMET_RADIUS);
        }
        rangAffichage.setTranslateX(rangAffichage.getLayoutX() + getSommetRadius());
        rangAffichage.setTranslateY(rangAffichage.getLayoutY() - getSommetRadius());

        dureeAffichage.setTranslateX(rangAffichage.getLayoutX() - getSommetRadius());
        dureeAffichage.setTranslateY(rangAffichage.getLayoutY() + getSommetRadius());
    }

    @Override
    public String toString() {
        return "Sommet{" +
                "id=" + id +
                ", valeur='" + valeur + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sommet sommet = (Sommet) o;
        return id == sommet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
