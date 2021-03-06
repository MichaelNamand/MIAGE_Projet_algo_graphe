package sample;

import algorithmes.*;
import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Scanner;

/**
 * Fenetre
 * Littérallement la fenêtre de notre application. C'est ici qu'on gère tous les menus et boutons de gestion d'un graphe
 */
public class Fenetre extends Parent {
    private Graphe graphe;                                  // Graphe courant
    private BorderPane border = new BorderPane();           // BorderPane (élément graphique) gérant la disposition des noeuds de la fenêtre
    private ScrollPane scrollPane = new ScrollPane();       // ScrollPane (élément graphique) contenant le graphe
    private Stage primaryStage;                             // Racine de notre application graphique
    public static boolean changementsEffectues = false;     // Boolean indiquant si des changements ont été fait au graphe

    // Checkbox de paramètres utilisateur
    private CheckBox cbRangsSommets;                        // Checkbox "Afficher le rang des sommets"
    private CheckBox cbArcsCouts;                           // Checkbox "Afficher le coût des arcs"
    private CheckBox cbNomsSommets;                         // Checkbox "Afficher le nom des sommets"

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
        Menu menuAlgorithmes = new Menu("Algorithmes");
        Menu menuConsole = new Menu("Console");
        Menu menuResetCouleur = new Menu("Rétablir Couleur");

        MenuItem menuResetCouleurLancer = new MenuItem("Lancer");
        menuResetCouleurLancer.setOnAction(t -> { graphe.retablirAffichage(); });
        menuResetCouleur.getItems().add(menuResetCouleurLancer);

        // Création des éléments du menu
        MenuItem menuCreer = new MenuItem("Créer un nouveau graphe", new ImageView(imageAjouter));
        menuCreer.setOnAction(t -> setGraphe(new Graphe("Sans nom"), false));

        MenuItem menuOuvrir = new MenuItem("Ouvrir depuis un fichier", new ImageView(imageFichier));
        menuOuvrir.setOnAction(t -> { ouvrirGraphe(); });

        MenuItem menuEnregistrer = new MenuItem("Enregistrer...", new ImageView(imageEnregistrer));
        menuEnregistrer.setOnAction(t -> { enregistrerModifications(); });

        // Création de menus des algorithmes
        Menu menuRang = new Menu("Algorithme du rang");
        Menu menuOrdonnancement = new Menu("Problème d'ordonnancement");
        Menu menuDistance = new Menu("Calcul distance");
        Menu menuTarjan = new Menu("CFC selon Tarjan");
        Menu menuKruskal = new Menu("Algorithme de Kruskal");
        Menu menuDijkstra = new Menu("Algorithme de Dijkstra"); //ici aussi j'ai ajouter
        Menu menuPrufer= new Menu("Algorithme de Prufer");


        // Création des sous-menus des algorithmes gérant leur lancement
        MenuItem menuDistanceLancer = new MenuItem("Lancer");
        MenuItem menuDistanceAide = new MenuItem("Aide");
        menuDistanceLancer.setOnAction(t -> { Distance.distance(graphe); });
        menuDistanceAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à calculer le nombre minimum d'arcs d'un chemin allant d'un sommet choisi à l'autre.\n" +
                    "Cliquez sur la fonction puis sélectionnez le sommet de départ.");

            alert.showAndWait();

        });
        menuDistance.getItems().addAll(menuDistanceLancer, menuDistanceAide);

        MenuItem menuOrdonnancementLancer = new MenuItem("Lancer");
        MenuItem menuOrdonnancementRetablir = new MenuItem("Rétablir");
        MenuItem menuOrdonnancementAide = new MenuItem("Aide");
        menuOrdonnancementLancer.setOnAction(t -> { Ordonnancement.setOrdonnancement(graphe); });
        menuOrdonnancementRetablir.setOnAction(t -> { graphe.retablirAffichage(); });
        menuOrdonnancementAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à répondre à la problématique suivante : Comment organiser un ensemble de tâches d’un projet pour l’exécuter en un minimum de temps ?\n" +
                    "Cliquez simplement sur la fonction pour l'exécuter.");

            alert.showAndWait();

        });
        menuOrdonnancement.getItems().addAll(menuOrdonnancementLancer, menuOrdonnancementRetablir,menuOrdonnancementAide);

        MenuItem menuRangLancer = new MenuItem("Lancer");
        MenuItem menuRangAide = new MenuItem("Aide");
        menuRangLancer.setOnAction(t -> {
            Rang.setRang(graphe);
            graphe.setAfficherRangSommetsEnclenches(true);
            if (cbRangsSommets != null) {
                cbRangsSommets.setSelected(true);
            }
        });
        menuRangAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à calculer le rang de chaque sommet, c'est-à-dire le plus long chemin arrivant à ce sommet.\n" +
                    "Cliquez simplement sur la fonction pour l'exécuter.");

            alert.showAndWait();

        });
        menuRang.getItems().addAll(menuRangLancer, menuRangAide);

        MenuItem menuTarjanLancer = new MenuItem("Lancer");
        MenuItem menuTarjanAide = new MenuItem("Aide");
        menuTarjanLancer.setOnAction(t -> {
            // L'algorithme tarjan renvoie le graphe réduit
            Graphe grapheReduit = Tarjan.tarjan(graphe);

            // On gère ici l'affichage dynamique de ses sommets
            int N = grapheReduit.getSommets().size();
            for (int i = 0; i < N; i++) {
                double x = 150.0f + 100 * Math.cos(Math.PI * 2 * i / N);
                double y = 150.0f + 100 * Math.sin(Math.PI * 2 * i / N);
                Sommet sommet = grapheReduit.getSommet(i + 1);
                sommet.setLayoutX(x);
                sommet.setLayoutY(y);
            }
            // stackPane pour afficher le graphe, du texte et un bouton
            StackPane stackPane = new StackPane();

            Button button = new Button("Fermer");
            button.setOnMouseClicked(f -> {
                border.setRight(null);
            });
            grapheReduit.getPane().setPrefWidth(300);
            grapheReduit.getPane().setPrefHeight(400);
            for (Arc arc : grapheReduit.getArcs()) { arc.setLine(); }
            Text info  = new Text("Graphe réduit : Du à un bug d'affichage, \ndéplacer un sommet pour remettre en \nplace leurs arcs");
            stackPane.getChildren().addAll(grapheReduit, button, info);

            // Positionnement des éléments graphiques
            StackPane.setAlignment(button, Pos.TOP_RIGHT);
            StackPane.setAlignment(info, Pos.TOP_LEFT);
            StackPane.setAlignment(grapheReduit, Pos.TOP_LEFT);

            Fenetre.rafraichirInterface();
            border.setRight(stackPane);
        });
        menuTarjanAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à déterminer les composantes fortement connexes, c'est-à-dire les ensembles maximaux de sommets dans lequel il existe un chemin entre n'importe quel sommet.\n" +
                    "Cliquez simplement sur la fonction pour l'exécuter. Le graphe réduit s'affiche à droite, cliquez sur un de ses sommets pour le remettre en place.");

            alert.showAndWait();

        });
        menuTarjan.getItems().addAll(menuTarjanLancer, menuTarjanAide);

        MenuItem menuKruskalLancer = new MenuItem("Lancer");
        MenuItem menuKruskalAide = new MenuItem("Aide");
        menuKruskalLancer.setOnAction(t -> {
            //Kruskal renvoie un tableau d'arcs
            Arc[] arcs = Kruskal.kruskal(graphe);

            StackPane stackPane = new StackPane();
            Button button = new Button("Fermer");
            button.setOnMouseClicked(f -> {
                border.setRight(null);
            });
            String s = "Arcs qui ne crée pas de cycle, dans l'ordre :\n\n";
            for(int i = 0; i < arcs.length; i++) {
                s += arcs[i].getDepart().id()+" -> "+arcs[i].getArrivee().id()+"\t"+arcs[i].getCout()+"\n";
            }
            Text text = new Text(s);
            stackPane.getChildren().addAll(button, text);
            StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(text, Pos.TOP_LEFT);
            Fenetre.rafraichirInterface();
            border.setRight(stackPane);

        });
        menuKruskalAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à déterminer l'arbre recouvrant minimal du graphe.\n" +
                    "Cliquez simplement sur la fonction pour l'exécuter.");

            alert.showAndWait();

        });
        menuKruskal.getItems().addAll(menuKruskalLancer, menuKruskalAide);

//de ici j'ai fais un genre de copier coller et j'ai changer le nom pour afficher dans la fenêtre
        MenuItem menuDijkstraLancer = new MenuItem("Lancer");
        MenuItem menuDijkstraAide = new MenuItem("Aide");
        menuDijkstraLancer.setOnAction(t -> {

            ListView<Integer> listview_int = new ListView<>();
            ObservableList<Integer> items =FXCollections.observableArrayList ();


            for(int i =0; i< graphe.getSommets().size();i++){
                items.add(i+1);
            }

            listview_int.setItems(items);

            listview_int.setPrefWidth(100);
            listview_int.setPrefHeight(30);
            listview_int.setOrientation(Orientation.VERTICAL);


            StackPane stackPane2 = new StackPane();
            stackPane2.getChildren().addAll(listview_int);
            StackPane.setAlignment(listview_int, Pos.TOP_LEFT);
            Fenetre.rafraichirInterface();
            border.setRight(stackPane2);
            listview_int.setOnMouseClicked(f ->{

                Dijkstra.dijkstra(graphe, listview_int.getSelectionModel().getSelectedIndex()+1);
            });


        });
        menuDijkstraAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à trouver le chemin le plus court.'\n' " +
                    "Cliquez sur la fonction puis lorsque la liste des sommets s'affiche, cliquez sur le sommet par lequel vous souhaitez commencer.");

            alert.showAndWait();

        });
        menuDijkstra.getItems().addAll(menuDijkstraLancer, menuDijkstraAide);




        MenuItem menuPruferLancer = new MenuItem("Lancer");
        MenuItem menuPruferAide = new MenuItem("Aide");
        menuPruferLancer.setOnAction(t -> {

            StackPane stackPane = new StackPane();
            Button button = new Button("Fermer");
            button.setOnMouseClicked(f -> {
                border.setRight(null);
            });
            String s = "Suite de Prüfer :\n\n" + Prufer.affichePrufer(graphe);
            Text text3 = new Text(s);
            stackPane.getChildren().addAll(button, text3);
            StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(text3, Pos.TOP_LEFT);
            Fenetre.rafraichirInterface();
            border.setRight(stackPane);

        });
        menuPruferAide.setOnAction(t ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            alert.setTitle("Aide");
            alert.setContentText("Cet algorithme sert à simplifier la lecture de la structure d'un arbre UNIQUEMENT, et retourne donc une séquence unique.\n" +
                    "Cliquez simplement sur la fonction pour l'exécuter.");

            alert.showAndWait();


        });
        menuPrufer.getItems().addAll(menuPruferLancer, menuPruferAide);





        //Création des éléments du menu Console
        MenuItem menuAfficheAdj = new MenuItem("Afficher matrice d'adjacence");
        MenuItem menuAfficheFsAps = new MenuItem("Afficher FS et APS");

        menuAfficheAdj.setOnAction(t -> {
            graphe.afficheMatAdj();
        });

        menuAfficheFsAps.setOnAction(t -> {
            graphe.afficheFsAps();
        });

        menuConsole.getItems().addAll(menuAfficheAdj,menuAfficheFsAps);


        //Ajouts des éléments au menu algorithme
        menuAlgorithmes.getItems().addAll(menuRang, menuOrdonnancement, menuDistance, menuTarjan, menuKruskal, menuDijkstra, menuPrufer);



        // Ajout des éléments au menu
        menu.getItems().addAll(menuCreer, menuOuvrir, menuEnregistrer);
        menu.getItems().add(new MenuItem("Quitter", new ImageView(imageQuitter)));
        menuBar.getMenus().addAll(menu, menuAlgorithmes, menuConsole, menuResetCouleur);

        // Ajout du menu au layout racine
        border.setTop(menuBar);

        // Ajout du layout vbox au layout racine
        setGraphe(new Graphe("Sans nom"), false);
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
                                Double.parseDouble(elements[3]), Integer.parseInt(elements[4]), elements[5],  graphe));
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
                        setGraphe(graphe, false);
                        graphe.setAfficherCoutsArcsEnclenches(elements[1].equals("true"));
                        graphe.setAfficherNomsSommetsEnclenches(elements[2].equals("true"));
                        graphe.setAfficherRangSommetsEnclenches(elements[3].equals("true"));
                        cbRangsSommets.setSelected(graphe.isAfficherRangSommetsEnclenches());
                        cbArcsCouts.setSelected(graphe.isAfficherCoutsArcsEnclenches());
                        cbNomsSommets.setSelected(graphe.isAfficherNomsSommetsEnclenches());
                }
            }
            Graphe.idIncrement = graphe.getSommets().size() + 1;
        }
    }

    /**
     * Du à un bug non connu, cette méthode affiche un message d'information, permettant à l'interface
     * de se mettre à jour
     */
    public static void rafraichirInterface() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patientez...");
        alert.setHeaderText("Mise à jour de l'interface...");
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(200));
        pauseTransition.setOnFinished(t -> {
            alert.close();
        });
        pauseTransition.play();
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue demandant à l'utilisateur de saisir du texte
     * @param titre
     * @return
     */
    public static TextInputDialog getTextInputFromDialog(String titre) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Renseignement d'information");
        dialog.setHeaderText(titre);
        dialog.setContentText("Nom :");
        return dialog;
    }

    /**
     * Lorsque l'utilisateur clique sur Enregistrer le graphe
     */
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
            // Si aucun graphe ouvert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aucun graphe ouvert");
            alert.setHeaderText("Créez ou ouvrez un graphe pour procéder à son enregistrement !");
            alert.showAndWait();
        }
    }

    /**
     * Méthode qui demande à l'utilisateur la destination du fichier de sauvegarde, puis écrit les informations du graphe
     * dessus.
     */
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
                        out.write("S/" + s.id() + "/" + (s.getX()) + "/" + (s.getY())
                                + "/" + s.getRang() + "/" + s.getValeur());
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
                    out.write("O/" + graphe.isAfficherCoutsArcsEnclenches() + "/" + graphe.isAfficherNomsSommetsEnclenches() +
                            "/" + graphe.isAfficherRangSommetsEnclenches());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Fenetre.changementsEffectues = false;
    }

    /**
     * Ajoute le graphe passé en paramètre dans notre scrollPane ainsi que les boutons de configuration du footer
     * (Créer des sommets, des arcs, ...)
     * @param g
     * @param debugArc
     */
    public void setGraphe(Graphe g, boolean debugArc) {
        // Si on détecte qu'un changement dans le graphe a été effectué, on demande à l'utilisateur de l'enregistrer
        if (!debugArc && Fenetre.changementsEffectues) {
            Alert alert = getConfirmationQuitter(g, "Si vous créer un nouveau graphe, celui actuel risque " +
                    "de perdre des informations non enregistrées");
            alert.showAndWait().ifPresent(type -> {
                if (type.getText().equals("Oui")) {
                    enregistrerModifications();
                } else if (type.getText().equals("Non")) {
                    Fenetre.changementsEffectues = false;
                    setGraphe(g, false);
                } else {
                    alert.close();
                }
            });
            return;
        }
        graphe = null;
        Graphe.idIncrement = 1;
        graphe = g;

        // Ajout du graphe dans notre  scrollpane
        scrollPane.setContent(graphe);
        // On lui met un fond gris
        scrollPane.setStyle("-fx-background-color: lightgray, white ;" +
                "    -fx-background-insets: 0, 5 ;");

        // Ajout du scrollpane dans notre fenêtre
        border.setCenter(scrollPane);

        // Définition du footer
        HBox flow = new HBox();
        flow.setPadding(new Insets(10, 0, 10, 10));
        flow.setSpacing(10); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        // Images des deux boutons du footer
        Image imageSommet = new Image(getClass().getResourceAsStream("/assets/icons/pin.png"));
        Image imageArc = new Image(getClass().getResourceAsStream("/assets/icons/link.png"));

        // Boutons du footer
        ToggleGroup group = new ToggleGroup();
        ToggleButton bSommet = new ToggleButton("Créer des sommets", new ImageView(imageSommet));
        ToggleButton bArc = new ToggleButton("Créer des arcs", new ImageView(imageArc));
        bSommet.setToggleGroup(group);
        bArc.setToggleGroup(group);

        // Gestion du clic du bouton "Créer des sommets"
        bSommet.setOnMouseClicked(t -> {
            if(bSommet.isSelected()) {
                graphe.setCursor(Cursor.CROSSHAIR);
            } else {
                graphe.setCursor(Cursor.DEFAULT);
            }
            graphe.setCreationArcsEnclenches(bArc.isSelected());
            graphe.setCreationSommetsEnclenches(bSommet.isSelected());
        });

        // Gestion du clic du bouton "Créer des arcs"
        bArc.setOnMouseClicked(t -> {
            graphe.setCursor(Cursor.DEFAULT);
            graphe.setCreationArcsEnclenches(bArc.isSelected());
            graphe.setCreationSommetsEnclenches(bSommet.isSelected());
        });

        VBox vBox = new VBox();

        cbArcsCouts = new CheckBox("Afficher le coût des arcs");
        cbNomsSommets = new CheckBox("Afficher le nom des sommets plutôt que leur identifiant");
        cbRangsSommets = new CheckBox("Afficher le rang des sommets");

        cbArcsCouts.setSelected(g.isAfficherCoutsArcsEnclenches());
        cbNomsSommets.setSelected(g.isAfficherNomsSommetsEnclenches());

        cbArcsCouts.setOnAction(t -> {
            graphe.setAfficherCoutsArcsEnclenches(cbArcsCouts.isSelected());
        });
        cbNomsSommets.setOnAction(t -> {
            graphe.setAfficherNomsSommetsEnclenches(cbNomsSommets.isSelected());
        });
        cbRangsSommets.setOnAction(t -> {
            graphe.setAfficherRangSommetsEnclenches(cbRangsSommets.isSelected());
        });

        vBox.getChildren().addAll(cbNomsSommets, cbArcsCouts);
        vBox.setSpacing(5);

        flow.getChildren().addAll(bSommet, bArc, vBox, cbRangsSommets);
        border.setBottom(flow);

        Fenetre.changementsEffectues = false;
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
