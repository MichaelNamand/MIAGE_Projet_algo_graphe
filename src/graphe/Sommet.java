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
import javafx.scene.text.Text;
import sample.Fenetre;

public class Sommet extends StackPane {
    public final int id;
    public String valeur;
    static public double SOMMET_RADIUS = 20;
    double x, y;
    private Circle cercle;
    private Text nomAffichage;

    public Sommet(int id, double x, double y, String valeur, Graphe graphe) {
        this.id = id;
        this.valeur = valeur;
        this.x = x; this.y = y;
        nomAffichage = new Text(graphe.isAfficherNomsSommetsEnclenches() ? valeur : id + "");
        cercle = new Circle();
        setStyleSommet(graphe.isAfficherNomsSommetsEnclenches());
        cercle.setStroke(Color.BLACK);
        cercle.setStrokeWidth(2);
        cercle.setFill(Color.LIGHTBLUE);
        getChildren().addAll(cercle, nomAffichage);
        setLayoutX(x - getSommetRadius());
        setLayoutY(y - getSommetRadius() * 2);
        setCursor(Cursor.HAND);
        setOnMousePressed((t) -> {
            this.x = t.getSceneX();
            this.y = t.getSceneY();
        });
        setPrefHeight(44);
        // Gestion du déplacement d'un sommet
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
            cercle.setFill(Color.LIGHTBLUE);
        });
        // Création du menu contextuel lorsqu'on clique droit sur un sommet
        ContextMenu contextMenu = new ContextMenu();
        Image imagePoubelle = new Image(getClass().getResourceAsStream("/assets/icons/trash.png"));
        Image imageEdit = new Image(getClass().getResourceAsStream("/assets/icons/pencil.png"));
        MenuItem item1 = new MenuItem("Supprimer", new ImageView(imagePoubelle));
        MenuItem item2 = new MenuItem("Renommer", new ImageView(imageEdit));

        // Gestion du clic Supprimer
        item1.setOnAction(event -> {
            graphe.getSommets().remove(Sommet.this);
            while (graphe.getArcFromSommet(Sommet.this) != null) {
                Arc a = graphe.getArcFromSommet(Sommet.this);
                graphe.getPane().getChildren().remove(a);
                graphe.getArcs().remove(a);
            }
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
                if(t.getButton() == MouseButton.PRIMARY && graphe.isCreationArcsEnclenches()) {
                    if (graphe.getPremierSommetRelie() == null) {
                        graphe.setPremierSommetRelie(Sommet.this);
                        getCercle().setFill(Color.LIGHTGREEN);
                    } else if (graphe.getPremierSommetRelie().id == Sommet.this.id) {
                        getCercle().setFill(Color.LIGHTBLUE);
                        graphe.setPremierSommetRelie(null);
                    } else {
                        Arc arc = new Arc(1, graphe.getPremierSommetRelie(), Sommet.this, graphe);
                        if (graphe.ajouterArc(arc)) {
                            getCercle().setFill(Color.LIGHTBLUE);
                            if (graphe.isAfficherCoutsArcsEnclenches())
                                Fenetre.rafraichirInterface();
                            arc.setLine();
                        }
                    }
                }
            }
        });
    }

    public void setNomAffichageSommet(String nom) { nomAffichage.setText(nom); }

    public double getSommetRadius() { return cercle.getRadius() + cercle.getStrokeWidth() * 2; }

    public Circle getCercle() { return cercle; }

    public Text getNomAffichage() { return nomAffichage; }

    public double getX() { return x; }

    public double getY() { return y; }

    public int id() { return id; }

    public String getValeur() { return valeur; }

    public void setValeur(String valeur) { this.valeur = valeur; }
    
    public void setStyleSommet(boolean parValeur) {
        if (parValeur) {
            Sommet.SOMMET_RADIUS = 5;
            setNomAffichageSommet(getValeur());
            StackPane.setAlignment(getNomAffichage(), Pos.BOTTOM_CENTER);
            cercle.setRadius(Sommet.SOMMET_RADIUS);
        } else {
            Sommet.SOMMET_RADIUS = 20;
            setNomAffichageSommet(id() + "");
            StackPane.setAlignment(getNomAffichage(), Pos.CENTER);
            cercle.setRadius(Sommet.SOMMET_RADIUS);
        }
    }
}
