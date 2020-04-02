package algorithmes;

import graphe.Arc;
import graphe.Graphe;

import java.util.ArrayList;

public class Kruskal {

    private static void trier(Graphe graphe) {
        double p;
        for(int i = 0; i < graphe.getArcs().size() - 1; i++) {
            for(int j = i + 1; j < graphe.getArcs().size(); j++) {
                if(((graphe.getArcs().get(j).getCout() < graphe.getArcs().get(i).getCout())
                        || ((graphe.getArcs().get(i).getCout() == graphe.getArcs().get(j).getCout())
                        && (graphe.getArcs().get(j).getDepart().id() < graphe.getArcs().get(i).getArrivee().id()))
                        || ((graphe.getArcs().get(j).getCout() == graphe.getArcs().get(i).getCout())
                        && (graphe.getArcs().get(j).getArrivee().id() < graphe.getArcs().get(i).getDepart().id())))) {
                    p = graphe.getArcs().get(j).getCout();
                    graphe.getArcs().get(j).setCout(graphe.getArcs().get(i).getCout());
                    graphe.getArcs().get(i).setCout(graphe.getArcs().get(j).getCout());
                }
            }
        }
    }

    public static Arc[] kruskal(Graphe graphe) {
        trier(graphe);
        int n = graphe.getSommets().size();
        int[] prem = new int[n + 1];
        int[] pilch = new int[n + 1];
        int[] cfc = new int[n + 1];
        int[] nbElem = new int[n + 1];
        for(int i = 1; i <= n; i++) {
            prem[i] = i;
            pilch[i] = 0;
            cfc[i] = i;
            nbElem[i] = 1;
        }
        Arc[] arcs = new Arc[graphe.getSommets().size()];
        Arc ar;
        int x;
        int y;
        int i = 0, j = 0;
        while(j < graphe.getSommets().size() - 1) {
            ar = graphe.getArcs().get(i);
            x = cfc[ar.getDepart().id()];
            y = cfc[ar.getArrivee().id()];
            if(x != y) {
                arcs[j++] = graphe.getArcs().get(i);
                //code de la fusion
                if(nbElem[i] < nbElem[j]) {
                    int aux = i;
                    i = j;
                    j = aux;
                }
                int s = prem[j];
                cfc[s] = i;
                while (pilch[s] != 0) {
                    s = pilch[s];
                    cfc[s] = i;
                }
                pilch[s] = prem[i];
                prem[i] = prem[j];
                nbElem[i] += nbElem[j];
                //fin fusion
            }
            i++;
        }
        return arcs;
    }

}
