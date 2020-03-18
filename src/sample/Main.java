package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        menu.getItems().add(new MenuItem("Item 1"));
        menuBar.getMenus().add(menu);

        BorderPane border = new BorderPane();
        border.setTop(menuBar);

        StackPane pile = new StackPane();
        Text text = new Text("Aucun graphe n'est actuellement ouvert");
        Rectangle r = new Rectangle(400, 100);
        r.setFill(Color.LIGHTGRAY);
        pile.getChildren().add(r);
        pile.getChildren().add(text);
        border.setCenter(pile);

        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(border, 800, 600, Color.LIGHTGREEN);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
