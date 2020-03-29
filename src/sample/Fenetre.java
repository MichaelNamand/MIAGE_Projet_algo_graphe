package sample;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Scanner;

public class Fenetre extends Parent {
    private Graphe graphe;
    private BorderPane border = new BorderPane();
    private ScrollPane scrollPane = new ScrollPane();
    private Stage primaryStage;

    public Fenetre(Stage stage) {
        // Images de l'application
        Image imageAjouter = new Image(getClass().getResourceAsStream("/assets/icons/plus.png"));
        Image imageFichier = new Image(getClass().getResourceAsStream("/assets/icons/folder.png"));
        Image imageQuitter = new Image(getClass().getResourceAsStream("/assets/icons/quit.png"));
        Image imageEnregistrer = new Image(getClass().getResourceAsStream("/assets/icons/save.png"));

        this.primaryStage = stage;
        // Création de la barre de menu
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        // Création des éléments du menu
        MenuItem menuCreer = new MenuItem("Créer un nouveau graphe", new ImageView(imageAjouter));
        menuCreer.setOnAction(t -> setGraphe(new Graphe("Sans nom"), false));

        MenuItem menuOuvrir = new MenuItem("Ouvrir depuis un fichier", new ImageView(imageFichier));
        menuOuvrir.setOnAction(t -> { ouvrirGraphe(); });

        MenuItem menuEnregistrer = new MenuItem("Enregistrer...", new ImageView(imageEnregistrer));
        menuEnregistrer.setOnAction(t -> { enregistrerModifications(); });

        // Ajout des éléments au menu
        menu.getItems().addAll(menuCreer, menuOuvrir, menuEnregistrer);
        menu.getItems().add(new MenuItem("Quitter", new ImageView(imageQuitter)));
        menuBar.getMenus().add(menu);

        // Ajout du menu au layout racine
        border.setTop(menuBar);

        // Création du layout "Aucun graphe"
        VBox vbox = new VBox();
        Text text = new Text("Aucun graphe n'est actuellement ouvert");
        Button bCreer = new Button("Créer un nouveau graphe", new ImageView(imageAjouter));
        Button bOuvrir = new Button("Ouvrir un graphe...", new ImageView(imageEnregistrer));
        bCreer.setOnMouseClicked(t -> setGraphe(new Graphe("Sans nom"), false));
        bOuvrir.setOnMouseClicked(t -> ouvrirGraphe());

        // Ajout du layout "Aucun graphe" au layout vbox
        vbox.getChildren().addAll(text, bCreer, bOuvrir);
        vbox.setSpacing(10);

        vbox.setAlignment(Pos.CENTER);

        // Ajout du layout vbox au layout racine
        border.setCenter(vbox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
    }

    /**
     * Ouvre une boite de dialogue permettant d'ouvrir un graphe depuis un fichier
     */
    public void ouvrirGraphe() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un graphe...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Graph Algorithm Java", "*.gaj"));
        File gajFile = fileChooser.showOpenDialog(primaryStage);
        Scanner sc = null;
        if (gajFile != null) {
            try {
                sc = new Scanner(gajFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (sc != null) {
            // Création du nouveau graphe depuis le fichier gajFile
            Graphe graphe = new Graphe("Sans nom");
            boolean done = false;
            while (sc.hasNext()) {
                String[] elements = sc.next().split("/");
                switch (elements[0]) {
                    case "S":
                        graphe.ajouterSommet(new Sommet(Integer.parseInt(elements[1]), Double.parseDouble(elements[2]),
                                Double.parseDouble(elements[3]), elements[4], graphe));
                        break;
                    case "A":
                        graphe.ajouterArc(new Arc(Integer.parseInt(elements[3]), graphe.getSommet(Integer.parseInt(elements[1])),
                                graphe.getSommet(Integer.parseInt(elements[2])), graphe));
                        break;
                    case "N":
                        graphe.setNom(elements[1]);
                        primaryStage.setTitle("Graphes - " + elements[1]);
                        break;
                    case "O":
                        graphe.setAfficherCoutsArcsEnclenches(elements[1].equals("true"));
                        graphe.setAfficherNomsSommetsEnclenches(elements[2].equals("true"));
                }
            }
            setGraphe(graphe, false);
            Fenetre.rafraichirInterface();
            for (Arc arc : this.graphe.getArcs()) {
                arc.setLine();
                arc.getArrow().update();
            }

            Graphe.idIncrement = graphe.getSommets().size() + 1;
        }
    }

    public static void rafraichirInterface() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patientez...");
        alert.setHeaderText("Chargement en cours");
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
        pauseTransition.setOnFinished(t -> {
            alert.close();
        });
        pauseTransition.play();
        alert.showAndWait();
    }

    public static TextInputDialog getTextInputFromDialog(String titre) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Renseignement d'information");
        dialog.setHeaderText(titre);
        dialog.setContentText("Nom :");
        return dialog;
    }

    public void enregistrerModifications() {
        if (graphe != null) {
            if (graphe.getNom().equals("Sans nom")) {
                // Avant d'enregistrer le graphe, on demande à l'utilisateur son nom
                TextInputDialog dialog = Fenetre.getTextInputFromDialog("Donnez un nom au graphe");
                dialog.showAndWait().ifPresent(nom -> {
                    graphe.setNom(nom);
                    sauvegardeFichier();
                });
            } else {
                sauvegardeFichier();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aucun graphe ouvert");
            alert.setHeaderText("Créez ou ouvrez un graphe pour procéder à son enregistrement !");
            alert.showAndWait();
        }
    }

    public void sauvegardeFichier() {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Graph Algorithm Java", "*.gaj");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(graphe.getNom());
        // Fichier créé par fileChooser, une fois que le user a choisi la destination du fichier
        File file = fileChooser.showSaveDialog(primaryStage);
        primaryStage.setTitle("Graphes - " + graphe.getNom());
        BufferedWriter out = null;
        if (file != null) {
            try {
                out = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (out != null) {
                // On écrit d'abord le nom du graphe dans le fichier
                try {
                    out.write("N/" + graphe.getNom());
                    out.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Puis on écrit les sommets dans le fichier
                for (Sommet s : graphe.getSommets()) {
                    try {
                        out.write("S/" + s.id() + "/" + s.getX() + "/" + s.getY() + "/" + s.getValeur());
                        out.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Enfin on enregistre les arcs
                for (Arc a : graphe.getArcs()) {
                    try {
                        out.write("A/" + a.getDepart().id() + "/" + a.getArrivee().id() + "/" + a.getCout());
                        out.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //On enregistre les options du graphe et on ferme le buffer
                try {
                    out.write("O/" + graphe.isAfficherCoutsArcsEnclenches() + "/" + graphe.isAfficherNomsSommetsEnclenches());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setGraphe(Graphe g, boolean debugArc) {
        Graphe.idIncrement = 1;
        if (graphe == null) {
            graphe = g;

            scrollPane.setContent(graphe);
            scrollPane.setStyle("-fx-background-color: lightgray, white ;" +
                    "    -fx-background-insets: 0, 5 ;");
            border.setCenter(scrollPane);
            HBox flow = new HBox();
            flow.setPadding(new Insets(10, 0, 10, 10));
            flow.setSpacing(10); // preferred width allows for two columns
            flow.setStyle("-fx-background-color: DAE6F3;");
            Image imageSommet = new Image(getClass().getResourceAsStream("/assets/icons/pin.png"));
            Image imageArc = new Image(getClass().getResourceAsStream("/assets/icons/link.png"));

            ToggleGroup group = new ToggleGroup();
            ToggleButton bSommet = new ToggleButton("Créer des sommets", new ImageView(imageSommet));
            ToggleButton bArc = new ToggleButton("Créer des arcs", new ImageView(imageArc));
            bSommet.setToggleGroup(group);
            bArc.setToggleGroup(group);

            bSommet.setOnMouseClicked(t -> {
                if(bSommet.isSelected()) {
                    graphe.setCursor(Cursor.CROSSHAIR);
                } else {
                    graphe.setCursor(Cursor.DEFAULT);
                }
                graphe.setCreationArcsEnclenches(bArc.isSelected());
                graphe.setCreationSommetsEnclenches(bSommet.isSelected());
            });
            bArc.setOnMouseClicked(t -> {
                graphe.setCursor(Cursor.DEFAULT);
                graphe.setCreationArcsEnclenches(bArc.isSelected());
                graphe.setCreationSommetsEnclenches(bSommet.isSelected());
            });

            VBox vBox = new VBox();

            CheckBox cbArcsCouts = new CheckBox("Afficher le coût des arcs");
            CheckBox cbNomsSommets = new CheckBox("Afficher le nom des sommets plutôt que leur identifiant");

            cbArcsCouts.setSelected(g.isAfficherCoutsArcsEnclenches());
            cbNomsSommets.setSelected(g.isAfficherNomsSommetsEnclenches());

            cbArcsCouts.setOnAction(t -> {
                graphe.setAfficherCoutsArcsEnclenches(cbArcsCouts.isSelected());
            });
            cbNomsSommets.setOnAction(t -> {
                graphe.setAfficherNomsSommetsEnclenches(cbNomsSommets.isSelected());
            });

            vBox.getChildren().addAll(cbNomsSommets, cbArcsCouts);
            vBox.setSpacing(5);

            flow.getChildren().addAll(bSommet, bArc, vBox);
            border.setBottom(flow);
        } else {
            if (!debugArc) {
                Alert alert = getConfirmationQuitter(g, "Si vous créer un nouveau graphe, celui actuel risque " +
                        "de perdre des informations non enregistrées");
                alert.showAndWait().ifPresent(type -> {
                    if (type.getText().equals("Oui")) {
                        enregistrerModifications();
                    } else if (type.getText().equals("Non")) {
                        graphe = null;
                        setGraphe(g, false);
                    } else {
                        alert.close();
                    }
                });
            }
        }
    }

    public Alert getConfirmationQuitter(Graphe g, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.setContentText("Souhaitez-vous enregistrer vos modifications ?");
        ButtonType okButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        return alert;
    }

    public Graphe getGraphe() {
        return graphe;
    }

    public BorderPane getBorder() {
        return this.border;
    }
}
