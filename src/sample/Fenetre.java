package sample;

import graphe.Graphe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Optional;

public class Fenetre extends Parent {
    private Graphe graphe;
    private BorderPane border = new BorderPane();
    private ScrollPane scrollPane = new ScrollPane();

    public static boolean creerSommetsEnclenche;
    public static boolean creerArcsEnclenche;

    public Fenetre() {
        // Images de l'application
        Image imageAjouter = new Image(getClass().getResourceAsStream("../assets/icons/plus.png"));
        Image imageFichier = new Image(getClass().getResourceAsStream("../assets/icons/folder.png"));
        Image imageQuitter = new Image(getClass().getResourceAsStream("../assets/icons/quit.png"));

        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        // Création des éléments du menu
        MenuItem menuCreer = new MenuItem("Créer un nouveau graphe", new ImageView(imageAjouter));
        menuCreer.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        setGraphe();
                    }
                }
        );

        // Ajout des éléments au menu
        menu.getItems().add(menuCreer);
        menu.getItems().add(new MenuItem("Ouvrir depuis un fichier", new ImageView(imageFichier)));
        menu.getItems().add(new MenuItem("Quitter", new ImageView(imageQuitter)));
        menuBar.getMenus().add(menu);

        // Ajout du menu au layout racine
        border.setTop(menuBar);

        // Création du layout "Aucun graphe"
        VBox vbox = new VBox();
        Text text = new Text("Aucun graphe n'est actuellement ouvert");
        Button bCreer = new Button("Créer un nouveau graphe", new ImageView(imageAjouter));
        bCreer.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent t) {
                        setGraphe();
                    }
                }
        );

        // Ajout du layout "Aucun graphe" au layout vbox
        vbox.getChildren().add(text);
        vbox.getChildren().add(bCreer);
        vbox.setSpacing(10);

        vbox.setAlignment(Pos.CENTER);

        // Ajout du layout vbox au layout racine
        border.setCenter(vbox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setGraphe() {
        if (graphe == null) {
            graphe = new Graphe("Sans nom");
            scrollPane.setContent(graphe);
            scrollPane.setStyle("-fx-background-color: lightgray, white ;" +
                    "    -fx-background-insets: 0, 5 ;");
            border.setCenter(scrollPane);
            HBox flow = new HBox();
            flow.setPadding(new Insets(10, 0, 10, 10));
            flow.setSpacing(10); // preferred width allows for two columns
            flow.setStyle("-fx-background-color: DAE6F3;");
            Image imageSommet = new Image(getClass().getResourceAsStream("../assets/icons/pin.png"));
            Image imageArc = new Image(getClass().getResourceAsStream("../assets/icons/link.png"));

            ToggleGroup group = new ToggleGroup();
            ToggleButton bSommet = new ToggleButton("Créer des sommets", new ImageView(imageSommet));
            ToggleButton bArc = new ToggleButton("Créer des arcs", new ImageView(imageArc));
            bSommet.setToggleGroup(group);
            bArc.setToggleGroup(group);

            bSommet.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent t) {
                    if(bSommet.isSelected()) {
                        graphe.setCursor(Cursor.CROSSHAIR);
                    } else {
                        graphe.setCursor(Cursor.DEFAULT);
                    }
                    Fenetre.creerSommetsEnclenche = bSommet.isSelected();
                    if (!bSommet.isSelected() && !bArc.isSelected()) {
                        Fenetre.creerArcsEnclenche = false;
                    } else {
                        Fenetre.creerArcsEnclenche = !bSommet.isSelected();
                    }
                }
            });
            bArc.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent t) {
                    graphe.setCursor(Cursor.DEFAULT);
                    Fenetre.creerArcsEnclenche = bArc.isSelected();
                    if (!bArc.isSelected() && !bSommet.isSelected()) {
                        Fenetre.creerSommetsEnclenche = false;
                    } else {
                        Fenetre.creerSommetsEnclenche = !bArc.isSelected();
                    }
                }
            });

            flow.getChildren().add(bSommet);
            flow.getChildren().add(bArc);
            border.setBottom(flow);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la création");
            alert.setHeaderText("Si vous créer un nouveau graphe, celui actuel risque de perdre des informations non enregistrées");
            alert.setContentText("Souhaitez-vous vraiment créer un nouveau graphe ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }

    }

    public BorderPane getBorder() {
        return this.border;
    }
}
