package algorithmes;

import graphe.Graphe;

public class Prufer{

    public static int[] codagePrufer(Graphe graphe)
    {
        int nbSommet = graphe.getSommets().size();                                  //Nombre de sommets total du graphe
        int[] suiteDePrufer = new int[graphe.getSommets().size()-2];                //Suite de Prüfer qui est le résultat du codage
        boolean[] sommetsAParcourir = new boolean[graphe.getSommets().size()];      //Totalité des sommets à parcouir lors du codage

        int nbSommetsAccessibles = 0;                 //Nombre de sommets accessibles à traiter
        int nbDesommetsParcourus = 0;                 //Nombre de sommets parcourus lors du codage
        int idPredecesseur = 0;                       //Identifiant du predecesseur du sommet
        int idSuccesseur = 0;                         //Identifiant du successeur du sommet

        while (nbSommet > 2){           //Tant que le nombre de sommets est supérieur à 2 (condition d'arrêt du codage de Prüfer)
            for(int k = 1; k <= graphe.getSommets().size(); k++){

                for(int i=0 ; i<graphe.getSuccesseursSommet(graphe.getSommet(k)).size();i++){
                    if(sommetsAParcourir[graphe.getSuccesseursSommet(graphe.getSommet(k)).get(i).id()-1] == false){
                        nbSommetsAccessibles++;
                        idSuccesseur = graphe.getSuccesseursSommet(graphe.getSommet(k)).get(i).id();
                    }
                }

                for(int i=0 ; i<graphe.getPredecesseursSommet(graphe.getSommet(k)).size();i++){
                    if(sommetsAParcourir[graphe.getPredecesseursSommet(graphe.getSommet(k)).get(i).id()-1] == false){
                        nbSommetsAccessibles++;
                        idPredecesseur=graphe.getPredecesseursSommet(graphe.getSommet(k)).get(i).id();
                    }
                }

                if(nbDesommetsParcourus < graphe.getSommets().size()-2) {
                    if (nbSommetsAccessibles == 1 && idPredecesseur != 0) {
                        suiteDePrufer[nbDesommetsParcourus] = idPredecesseur;
                        sommetsAParcourir[k - 1] = true;
                        nbSommet--;
                        nbDesommetsParcourus++;

                    } else if (nbSommetsAccessibles == 1 && idSuccesseur != 0) {
                        suiteDePrufer[nbDesommetsParcourus] = idSuccesseur;
                        sommetsAParcourir[k - 1] = true;
                        nbSommet--;
                        nbDesommetsParcourus++;
                    }
                }

                nbSommetsAccessibles = 0;          //
                idPredecesseur = 0;                //Réinisialisation des variables pour la prochaine itération
                idSuccesseur = 0;                  //
            }

        }
        return suiteDePrufer;
    }

    public static String affichePrufer(Graphe graphe){
        int[] suiteDePrufer = codagePrufer(graphe);
        String affichagePrufer = "{ ";
        for(int i=0; i < suiteDePrufer.length-1 ; i++){
            affichagePrufer += suiteDePrufer[i] + ", ";
        }
        affichagePrufer += suiteDePrufer[suiteDePrufer.length-1] + "}";
        return  affichagePrufer;
    }

}
