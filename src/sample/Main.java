package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graphes");
        Fenetre fenetre = new Fenetre();
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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
