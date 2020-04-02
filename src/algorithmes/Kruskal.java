package algorithmes;

import graphe.Arc;
import graphe.Graphe;

import java.util.ArrayList;

public class Kruskal {

    public static Arc[] kruskal(Graphe graphe) {

        //tri
        int p;
        for(int i = 0; i < graphe.getArcs().size() - 1; i++) {
            for(int j = i + 1; j < graphe.getArcs().size(); j++) {
                if(((graphe.getArcs().get(j).getCout() < graphe.getArcs().get(i).getCout())
                        || ((graphe.getArcs().get(i).getCout() == graphe.getArcs().get(j).getCout())
                        && (graphe.getArcs().get(j).getDepart().id() < graphe.getArcs().get(i).getArrivee().id()))
                        || ((graphe.getArcs().get(j).getCout() == graphe.getArcs().get(i).getCout())
                        && (graphe.getArcs().get(j).getArrivee().id() < graphe.getArcs().get(i).getDepart().id())))) {

                    p = graphe.getArcs().get(j).getCout();
                    graphe.getArcs().get(j).setCout(graphe.getArcs().get(i).getCout());
                    graphe.getArcs().get(i).setCout(p);

                }
            }
        }
        //fin du tri

        int n = graphe.getSommets().size();
        System.out.println("Size = "+n);
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
        Arc[] arcs = new Arc[graphe.getSommets().size()-1];
        Arc ar;
        int x;
        int y;
        int i = 0, j = 0;
        while(j < n - 1) {
            ar = graphe.getArcs().get(i);
            x = cfc[ar.getDepart().id()];
            y = cfc[ar.getArrivee().id()];
            if(x != y) {
                arcs[j++] = graphe.getArcs().get(i);

                //code de la fusion
                if(nbElem[x] < nbElem[y]) {
                    int aux = x;
                    x = y;
                    y = aux;
                }
                int s = prem[y];
                cfc[s] = x;
                while (pilch[s] != 0) {
                    s = pilch[s];
                    cfc[s] = x;
                }
                pilch[s] = prem[x];
                prem[x] = prem[y];
                nbElem[x] += nbElem[y];
                //fin fusion

            }
            i++;
        }
        return arcs;
    }

}
