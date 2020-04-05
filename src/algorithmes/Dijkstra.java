package algorithmes;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;

import java.util.SortedMap;

public class Dijkstra {


    //int s, int[] fs, int[] aps, int[][] couts, int[] d, int[] pred

    public static boolean[] dijkstra(Graphe graphe, int sommets) {
       boolean[] visite = new boolean[graphe.getSommets().size()];      //tabeleau pour savoir si le sommet a était visité
       int[] etiquette = new int[graphe.getSommets().size()];           //le cout du chemin le plus rapide a partir du sommet choisis
       int tmps_score=0;
       int score = 0;
       boolean parcour__fini = false;
       int tmp_sommet= sommets;
       int score_comparaison = 0;
       Sommet sommet_second ;
        Sommet sommet_sauvegarder = null;



       //initialisation du tableau de visiste, tous non visité
        //initialisation du tableau de etiquette, tous na -1 quand in sont pas d'accés du sommet choisis
       for(int i = 1; i <= graphe.getSommets().size();i++){
           visite[i-1]=false;
           etiquette[i-1]=-1;
       }

       //initialiser les cases deja visité
       etiquette[sommets-1] = score;
       visite[sommets-1]= true;


       // parcourir les successeur jusqu a que ca soit fini
       while(parcour__fini==false){

           //si il y a des successeur
           if(graphe.getSuccesseursSommet(graphe.getSommet(tmp_sommet)).size()!=0){
               for(int l = 1; l<=graphe.getSuccesseursSommet(graphe.getSommet(tmp_sommet)).size();l++){
                   //sommet du successeur
                   sommet_second =graphe.getSuccesseursSommet(graphe.getSommet(tmp_sommet)).get(l-1);
                   //cout de l'arc
                   tmps_score = graphe.getArcFromSommets(graphe.getSommet(tmp_sommet), sommet_second).getCout();

                   //si sommet du départ egal second sommet (point de départ)
                   if(sommet_second.id()== sommets || visite[sommet_second.id()-1] ) {

                       //si c'est le dernier des succeseurs
                       if(l == graphe.getSuccesseursSommet(graphe.getSommet(tmp_sommet)).size()){
                           parcour__fini=true; // finir la recherche, c'est le dernier des dernier
                       }
                       //sinon on ne fait rien

                   } else {// si c'est pas egal au sommet

                       //si c'est pas le premier de la boucle
                       if (l != 1) {
                           //Si l'initialisation est suprieur
                           if (score_comparaison > score + tmps_score && sommet_second.id() != sommets) {
                              //on re-initialise
                               score_comparaison = score + tmps_score;
                               sommet_sauvegarder = sommet_second;
                           }

                       } else {//initialisé la comparaison
                           score_comparaison = score + tmps_score;
                           sommet_sauvegarder = sommet_second;
                       }

                   }

               }//fin for

               //si on est pas a la fin
               if(parcour__fini==false) {
                   score = score_comparaison;
                   etiquette[sommet_sauvegarder.id()-1] = score;
                   visite[sommet_sauvegarder.id()-1] = true;

                   tmp_sommet = sommet_sauvegarder.id();
               }


           } else {
                parcour__fini=true; //fini de remplire de tableau
           }

       }


        return visite;
       //graphe.getArcs().get(1).getDepart();

    }





}
