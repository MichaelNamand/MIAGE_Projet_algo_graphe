package sample;

import graphe.Graphe;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    Fenetre fenetre;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graphes");
        fenetre = new Fenetre(primaryStage);
        Scene scene = new Scene(fenetre.getBorder(), 1280, 768);

        primaryStage.setScene(scene);
        primaryStage.widthProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() > 1300.0) {
                primaryStage.setResizable(false);
                primaryStage.setWidth(1300.0);
                primaryStage.setResizable(true);
            }
        });
        primaryStage.heightProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() > 798) {
                primaryStage.setResizable(false);
                primaryStage.setHeight(798);
                primaryStage.setResizable(true);
            }
        });
        primaryStage.show();

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, t -> {
            if (fenetre.getGraphe() != null && Fenetre.changementsEffectues) {
                Alert alert = fenetre.getConfirmationQuitter(new Graphe(""), "Voulez-vous enregistrer vos éventuelles" +
                        " modifications avant de quitter ?");
                alert.showAndWait().ifPresent(type -> {
                    if (type.getText().equals("Oui")) {
                        Main.this.fenetre.enregistrerModifications();
                        primaryStage.close();
                    } else if (type.getText().equals("Non")) {
                        primaryStage.close();
                    } else {
                        alert.close();
                        t.consume();
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
