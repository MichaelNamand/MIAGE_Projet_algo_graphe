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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import sample.ArrowSecond;

import static graphe.Sommet.SOMMET_RADIUS;

public class Arc extends Parent {
    private int cout;
    private final Sommet depart;
    private final Sommet arrivee;
    private ArrowSecond arrow;
    private Line line;
    private StackPane stackPaneCout;
    private Graphe graphe;


    public Arc(int cout, Sommet depart, Sommet arrivee, Graphe graphe) {
        this.cout = cout;
        this.depart = depart;
        this.graphe = graphe;
        this.arrivee = arrivee;
        stackPaneCout = new StackPane();
        line = new Line();
        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK);
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
        arrow = new ArrowSecond(points, line);
        getChildren().addAll(arrow, line, stackPaneCout);

        if (!graphe.isAfficherCoutsArcsEnclenches()) {
            stackPaneCout.setVisible(false);
        }
        Text textCout = new Text(cout + "");
        Circle circle = new Circle(10, Color.LIGHTGRAY);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.BLACK);
        stackPaneCout.getChildren().addAll(circle, textCout);
        stackPaneCout.toFront();
        stackPaneCout.setOnMouseClicked(mouseEvent -> {
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
                        this.cout = c;
                        textCout.setText(c + "");
                    }
                });
            }
        });
        setLine();
    }

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
