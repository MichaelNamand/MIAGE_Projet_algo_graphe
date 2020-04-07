package algorithmes;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.ArrayList;
import java.util.SortedMap;

public class Dijkstra {



    public static void dijkstra(Graphe graphe, int sommets) {
        int[] tableau_cout = new int[graphe.getSommets().size()];
        int[] tableau_sommets= new int[graphe.getSommets().size()];
        int score=0;
        int tmp_score=0;
        int tmp_id=0;
        Sommet sommet_successeur=null;
        ArrayList<Integer> tmp = new ArrayList<>();

        for(int i=0; i<graphe.getSommets().size();i++){
            tableau_cout[i]=-1;
            tableau_sommets[i]=-1;
            graphe.getSommet(i+1).getCercle().setFill(Color.LIGHTBLUE);
        }

        for(int i=0; i<graphe.getArcs().size();i++){
            graphe.getArcs().get(i).setColor(Color.BLACK);
        }



        tableau_cout[sommets-1]=0;
        tableau_sommets[sommets-1]=0;
        tmp.add(graphe.getSommet(sommets).id());

        graphe.getSommet(sommets).getCercle().setFill(Color.RED);

        while(tmp.size()!=0){

            for(int k=tmp.size()-1; k>=0;k--) {
                tmp_id=tmp.get(k);
                tmp.remove(k);


                for (int l = 0; l < graphe.getSuccesseursSommet(graphe.getSommet(tmp_id)).size(); l++) {


                    score = tableau_cout[tmp_id - 1];
                    sommet_successeur = graphe.getSuccesseursSommet(graphe.getSommet(tmp_id)).get(l);
                    tmp_score = graphe.getArcFromSommets(graphe.getSommet(tmp_id), sommet_successeur).getCout();
                    score += tmp_score;



                    if (tableau_cout[sommet_successeur.id() - 1] > score ) {

                        tableau_cout[sommet_successeur.id() - 1] = score;
                        sommet_successeur.getCercle().setFill(Color.LIGHTBLUE);
                        graphe.getArcFromSommets(graphe.getSommet(tableau_sommets[sommet_successeur.id()-1]),sommet_successeur).setColor(Color.BLACK);

                        tableau_sommets[sommet_successeur.id() - 1] = graphe.getSommet(tmp_id).id();
                        sommet_successeur.getCercle().setFill(Color.RED);
                        graphe.getArcFromSommets(graphe.getSommet(tmp_id),sommet_successeur).setColor(Color.RED);




                    } else if(tableau_cout[sommet_successeur.id() - 1] == -1){
                        tableau_cout[sommet_successeur.id() - 1] = score;
                        tableau_sommets[sommet_successeur.id() - 1] = graphe.getSommet(tmp_id).id();
                        sommet_successeur.getCercle().setFill(Color.RED);
                        graphe.getArcFromSommets(graphe.getSommet(tmp_id),sommet_successeur).setColor(Color.RED);

                    }

                }

                if(tmp.size()==0){
                    for(int m=0; m<graphe.getSuccesseursSommet(graphe.getSommet(tmp_id)).size(); m++){
                        tmp.add(graphe.getSuccesseursSommet(graphe.getSommet(tmp_id)).get(m).id());
                    }
                }
            }

        }

    }






}
