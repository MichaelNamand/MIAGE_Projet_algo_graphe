package algorithmes;

import graphe.Graphe;
import graphe.Sommet;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Dijkstra {


    public static void dijkstra(Graphe graphe, int sommets) {
        int[] tableauCout = new int[graphe.getSommets().size()];        //tableau des coût
        int[] tableauSommetPredecesseur = new int[graphe.getSommets().size()];     // tabeleau qui indique le sommet successeur
        int cout = 0;
        int coutArc = 0;
        int idSommet = 0;
        Sommet sommetSuccesseur = null;
        ArrayList<Integer> listSommetSuccesseur = new ArrayList<>(); //list des sommet visité

        /*
         * initalisation des couleur des sommet et des tableau
         *-1 tableau des coût
         * -1 pour le tableau des sommets
         */
        for (int i = 0; i < graphe.getSommets().size(); i++) {
            tableauCout[i] = -1;
            tableauSommetPredecesseur[i] = -1;
            graphe.getSommet(i + 1).getCercle().setFill(Color.LIGHTBLUE);
        }

        //intialisation des couleurs des arc
        for (int i = 0; i < graphe.getArcs().size(); i++) {
            graphe.getArcs().get(i).setColor(Color.BLACK);
        }

        /*
         * marquer à la indice du sommet 0 pour commencé
         * Y ajouter un la couleur serai vert pour marquer le commencement
         * Ajouter le id du sommet à la liste
         */
        tableauCout[sommets - 1] = 0;
        tableauSommetPredecesseur[sommets - 1] = 0;
        listSommetSuccesseur.add(graphe.getSommet(sommets).id());
        graphe.getSommet(sommets).getCercle().setFill(Color.GREEN);

        //tant que la liste n'est pas vide
        while (listSommetSuccesseur.size() != 0) {

            for (int k = listSommetSuccesseur.size() - 1; k >= 0; k--) {
                idSommet = listSommetSuccesseur.get(k);     //met le id du dernier sommet de la liste dans idSommet
                listSommetSuccesseur.remove(k);     // puis on l'enléve de la liste


                for (int l = 0; l < graphe.getSuccesseursSommet(graphe.getSommet(idSommet)).size(); l++) {
                    cout = tableauCout[idSommet - 1];       // coût du sommet ou on se trouve
                    sommetSuccesseur = graphe.getSuccesseursSommet(graphe.getSommet(idSommet)).get(l);     // acceder au sommet successeur de la liste
                    coutArc = graphe.getArcFromSommets(graphe.getSommet(idSommet), sommetSuccesseur).getCout();     // acceder au coût de l'arc
                    cout += coutArc; //aditioner pour avoir le coût du chemin

                    /*
                     * si le coût indiquer dans la case du tableau et plus élévé que le cout
                     * on reinitialise la couleur du sommet et l'arc
                     * et on change les coûts et on met se nouveau predecesseur dans la case
                     */
                    if (tableauCout[sommetSuccesseur.id() - 1] > cout) {

                        // reinitialiser
                        tableauCout[sommetSuccesseur.id() - 1] = cout;
                        sommetSuccesseur.getCercle().setFill(Color.LIGHTBLUE);
                        graphe.getArcFromSommets(graphe.getSommet(tableauSommetPredecesseur[sommetSuccesseur.id() - 1]), sommetSuccesseur).setColor(Color.BLACK);

                        //nouveau sommet
                        tableauSommetPredecesseur[sommetSuccesseur.id() - 1] = graphe.getSommet(idSommet).id();
                        sommetSuccesseur.getCercle().setFill(Color.RED);
                        graphe.getArcFromSommets(graphe.getSommet(idSommet), sommetSuccesseur).setColor(Color.RED);


                    } //si le coût est égale a -1 attribut le cout et les sommet dans la case, car il n'a jamais était visité
                    else if (tableauCout[sommetSuccesseur.id() - 1] == -1) {
                        tableauCout[sommetSuccesseur.id() - 1] = cout;
                        tableauSommetPredecesseur[sommetSuccesseur.id() - 1] = graphe.getSommet(idSommet).id();
                        sommetSuccesseur.getCercle().setFill(Color.RED);
                        graphe.getArcFromSommets(graphe.getSommet(idSommet), sommetSuccesseur).setColor(Color.RED);

                    }

                }

                /*
                 * Si la liste est vide alors on test si il y a encore des successeur
                 * Si en plus la liste rest vide et la boucle se termine
                 */
                if (listSommetSuccesseur.size() == 0) {
                    for (int m = 0; m < graphe.getSuccesseursSommet(graphe.getSommet(idSommet)).size(); m++) {
                        listSommetSuccesseur.add(graphe.getSuccesseursSommet(graphe.getSommet(idSommet)).get(m).id());
                    }
                }
            }

        }

    }


}
