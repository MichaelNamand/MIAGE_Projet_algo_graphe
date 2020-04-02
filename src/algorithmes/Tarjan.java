package algorithmes;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;
import javafx.scene.paint.Color;

import java.util.Random;

public class Tarjan {

    public static Graphe tarjan(Graphe graphe) {
        int[][] dist = Distance.getMatDistance(graphe);
        int n = dist[0][0];
        int[] prem = new int[n + 1];
        int[] pilch = new int[n + 1];
        int[] cfc = new int[n + 1];
        int nc = 0;
        int ind;
        for (int s = 1; s <= n; ++s)
            cfc[s] = -1;
        for (int s = 1; s <= n; ++s) {
            if (cfc[s] == -1) {
                prem[++nc] = s;
                cfc[s] = nc;
                ind = s;
                for (int t = s + 1; t <= n; ++t) {
                    if (dist[s][t] > 0 && dist[t][s] > 0) {
                        cfc[t] = nc;
                        pilch[ind] = t;
                        ind = t;
                    }
                }
                pilch[ind] = 0;
            }
        }
        prem[0] = nc;


        // Colore les sommets selon leur composante
        Random rand = new Random();
        for (int i = 1; i < graphe.getSommets().size() + 1; i++) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            for (int j = 1; j < graphe.getSommets().size() + 1; j++) {
                if (cfc[i] == cfc[j]) {
                    graphe.getSommet(j).getCercle().setFill(Color.color(r, g, b));
                }
            }
        }
        Graphe grapheReduit = new Graphe("");
        for (int i = 1; i < graphe.getSommets().size() + 1; i++) {
            if (pilch[i] == 0) {
                Sommet sommet = new Sommet(cfc[i], "C" + cfc[i], grapheReduit);
                sommet.getCercle().setFill(graphe.getSommet(i).getCercle().getFill());
                grapheReduit.ajouterSommet(sommet);
            }
        }

        for (Sommet sommet : graphe.getSommets()) {
            for (Sommet successeur : graphe.getSuccesseursSommet(sommet)) {
                if (cfc[successeur.id()] != cfc[sommet.id()]) {
                    grapheReduit.ajouterArc(new Arc(1, grapheReduit.getSommet(cfc[sommet.id()]),
                            grapheReduit.getSommet(cfc[successeur.id()]), grapheReduit));
                }
            }
        }
        return grapheReduit;

    }

}
