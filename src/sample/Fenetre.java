package sample;

import graphe.Graphe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Optional;

public class Fenetre extends Parent {
    private Graphe graphe;
    private BorderPane border = new BorderPane();

    public Fenetre() {
        Image imageAjouter = new Image(getClass().getResourceAsStream("../assets/icons/plus.png"));
        Image imageFichier = new Image(getClass().getResourceAsStream("../assets/icons/folder.png"));
        Image imageQuitter = new Image(getClass().getResourceAsStream("../assets/icons/quit.png"));

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem menuCreer = new MenuItem("Créer un nouveau graphe", new ImageView(imageAjouter));
        menuCreer.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        setGraphe();
                    }
                }
        );
        menu.getItems().add(menuCreer);
        menu.getItems().add(new MenuItem("Ouvrir depuis un fichier", new ImageView(imageFichier)));
        menu.getItems().add(new MenuItem("Quitter", new ImageView(imageQuitter)));
        menuBar.getMenus().add(menu);

        border.setTop(menuBar);

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
        vbox.getChildren().add(text);
        vbox.getChildren().add(bCreer);
        vbox.setSpacing(10);

        vbox.setAlignment(Pos.CENTER);

        border.setCenter(vbox);
    }

    public void setGraphe() {
        if (graphe == null) {
            graphe = new Graphe();
            border.setCenter(graphe);
            FlowPane flow = new FlowPane();
            flow.setPadding(new Insets(5, 0, 5, 0));
            flow.setVgap(4);
            flow.setHgap(4);
            flow.setPrefWrapLength(170); // preferred width allows for two columns
            flow.setStyle("-fx-background-color: DAE6F3;");
            flow.setAlignment(Pos.TOP_CENTER);
            Image imageFichier = new Image(getClass().getResourceAsStream("../assets/icons/pin.png"));

            flow.getChildren().add(new Button("Créer un sommet", new ImageView(imageFichier)));
            border.setLeft(flow);
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
