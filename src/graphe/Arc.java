package graphe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import sample.Fenetre;

import static graphe.Sommet.SOMMET_RADIUS;

/**
 * Arc
 * Gère la manipulation d'un arc graphiquement (gestion des clics utilisateurs) et logiquement.
 */
public class Arc extends Parent implements Comparable<Arc>{
    private int cout;
    private final Sommet depart;
    private final Sommet arrivee;

    //Elements graphiques
    private ArrowSecond arrow;          // Flèche de l'arc
    private Line line;                  // Ligne de l'arc
    private StackPane stackPaneCout;    // StackPane affichant le coût de l'arc


    public Arc(int cout, Sommet depart, Sommet arrivee, Graphe graphe) {
        this.cout = cout;
        this.depart = depart;
        this.arrivee = arrivee;

        // Définition des éléments graphiques
        stackPaneCout = new StackPane();
        line = new Line();
        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK);
        setLine();
        // Définition d'un click listener lorsqu'on clique sur un arc
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
                            Fenetre.changementsEffectues = true;
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

        // Coordonnées des points de la flèche (créant un triangle)
        double[] points = {0.0, 7.0, -7.0, -7.0, 7.0, -7.0};
        arrow = new ArrowSecond(points, line);

        // Ajout des éléments graphiques
        getChildren().addAll(arrow, line, stackPaneCout);

        stackPaneCout.setVisible(graphe.isAfficherCoutsArcsEnclenches());

        // Elements graphique du coût de l'arc
        Text textCout = new Text(cout + "");
        Circle circle = new Circle(10, Color.LIGHTGRAY);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.BLACK);
        stackPaneCout.getChildren().addAll(circle, textCout);
        stackPaneCout.toFront();

        // Ajout d'un click listener lorsqu'on clique sur le coût de l'arc
        stackPaneCout.setOnMouseClicked(mouseEvent -> {
            // Si on double-clique, on affiche une boîte de dialogue permettant de renseigner le coût
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Renseignement d'information");
                dialog.setHeaderText("Renseigner le coût de l'arc reliant le sommet " + depart.id() + " au sommet " + arrivee.id());
                dialog.setContentText("Coût :");
                dialog.showAndWait().ifPresent(valeur -> {
                    int c = Integer.MAX_VALUE;
                    try {
                        c = Integer.parseInt(valeur);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Valeur incorrecte");
                        alert.setHeaderText("La valeur saisie n'est pas un nombre !");
                        alert.show();
                    }
                    if (c != Integer.MAX_VALUE) {
                        // Redéfinition du coût
                        this.cout = c;
                        textCout.setText(c + "");
                    }
                });
            }
        });
        setLine();
    }

    public void setColor(Paint paint) {
        line.setStroke(paint);
        arrow.setFill(paint);
    }
    public void resetArcDisplay() {
        setColor(Color.BLACK);
        line.getStrokeDashArray().clear();
    }
    public void setDash() {
        line.getStrokeDashArray().addAll(25.0, 10.0);
        setColor(Color.BLACK);
    }

    /**
     * Met à jour les coordonnées de la flèche et la ligne de l'arc, ainsi que le coût
     */
    public void setLine() {
        double centerDepartX = (depart.getCercle().getLayoutX()) + depart.getLayoutX();
        double centerDepartY = (depart.getCercle().getLayoutY()) + depart.getLayoutY();
        double centerArriveeX = (arrivee.getCercle().getLayoutX()) + arrivee.getLayoutX();
        double centerArriveeY = ( arrivee.getCercle().getLayoutY()) + arrivee.getLayoutY();

        // Mise à jour de la position de la ligne composant la flèche
        line.setStartX(centerDepartX);
        line.setStartY(centerDepartY);
        line.setEndX(centerArriveeX);
        line.setEndY(centerArriveeY);

        // Mise à jour de la position de l'affichage du coût de l'arc
        if (arrow != null) {
            double angle = Math.toDegrees(Math.atan2(centerArriveeY - centerDepartY, centerArriveeX - centerDepartX));
            stackPaneCout.setLayoutX((arrow.getTranslateX() + centerDepartX + SOMMET_RADIUS / 2 * Math.cos(Math.toRadians(angle)) - stackPaneCout.getWidth()) / 2);
            stackPaneCout.setLayoutY((arrow.getTranslateY() + centerDepartY + 10 * Math.sin(Math.toRadians(angle)) - stackPaneCout.getHeight()) / 2);
            arrow.update();
        }
    }

    public void afficherCouts() {
        stackPaneCout.setVisible(true);
        setLine();
    }

    public void masquerCouts() {
        stackPaneCout.setVisible(false);
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
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

    @Override
    public int compareTo(Arc arc) {
        return arrivee.id() - arc.arrivee.id();
    }

    @Override
    public String toString() {
        return "depart=" + depart.id() +
                " arrivee=" + arrivee.id();
    }

    private class ArrowSecond extends Polygon {
        private Line line;
        double radius;
        private int DECALAGE_POINTE = 7;

        public ArrowSecond(double[] points, Line line) {
            super(points);
            this.line = line;
            initialize();
        }

        private void initialize() {
            radius = Sommet.SOMMET_RADIUS + DECALAGE_POINTE;
            double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;

            double height = line.getEndY() - line.getStartY();
            double width = line.getEndX() - line.getStartX();
            double length = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));

            double subtractWidth = radius * width / length;
            double subtractHeight = radius * height / length;

            setRotate(angle - 90);
            setTranslateX(line.getStartX());
            setTranslateY(line.getStartY());
            setTranslateX(line.getEndX() - subtractWidth);
            setTranslateY(line.getEndY() - subtractHeight);
        }

        public Line getLine() {
            return line;
        }

        public void update(){
            initialize();
        }
    }
}
