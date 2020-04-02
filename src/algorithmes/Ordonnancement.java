package algorithmes;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Ordonnancement {
    public static void setOrdonnancement(Graphe graphe) {
        graphe.retablirAffichage();
        Rang.setRang(graphe);
        graphe.getSommet(1).setDureeAffichage(0);
        graphe.getSommet(1).getCercle().setFill(Color.LIGHTGREEN);

        int i = 1, n = graphe.getSommets().size();
        int[] L = new int[n + 1];
        L[i] = 0;
        while (i < n) {
            i++;
            ArrayList<Sommet> predecesseurs = graphe.getPredecesseursSommet(graphe.getSommet(i));
            if (predecesseurs.size() > 0) {
                Sommet sommetPlusLong = predecesseurs.get(0);
                for (Sommet sommet : predecesseurs) {
                    if (L[sommet.id()] > L[sommetPlusLong.id()]) {
                        sommetPlusLong = sommet;
                    }
                }
                Arc arc = graphe.getArcFromSommets(sommetPlusLong, graphe.getSommet(i));
                L[i] = L[sommetPlusLong.id()] + arc.getCout();
                graphe.getSommet(i).setDureeAffichage(L[i]);
            }
        }
        int idSommetMax = 0;
        for (int j = 1; j <= n; j++) if (idSommetMax < L[j]) idSommetMax = j;
        graphe.getSommet(idSommetMax).getCercle().setFill(Color.LIGHTGREEN);
        // Affichage du chemin critique
        int[] nbArcSuivDashedSommets = new int[n + 1];
        for (Sommet sommet : graphe.getSommets()) {
            int nbCritique = 0;
            for (Sommet sommetSuccesseur : graphe.getSuccesseursSommet(sommet)) {
                Arc arc = graphe.getArcFromSommets(sommet, sommetSuccesseur);
                if (L[sommetSuccesseur.id()] == L[sommet.id()] + arc.getCout()) {
                    arc.setColor(Color.RED);
                    nbCritique++;
                } else {
                    arc.setDash();
                    nbArcSuivDashedSommets[sommet.id()]++;
                }
            }
            if (nbCritique == 0 && graphe.getSuccesseursSommet(sommet).size() != 0) {
                for (Sommet sommetPredecesseur : graphe.getPredecesseursSommet(sommet)) {
                    Arc arc = graphe.getArcFromSommets(sommetPredecesseur, sommet);
                    arc.setDash();
                    nbArcSuivDashedSommets[sommetPredecesseur.id()]++;
                }
            }
        }
        for (Sommet sommet : graphe.getSommets()) {
            if (graphe.getSuccesseursSommet(sommet).size() <= nbArcSuivDashedSommets[sommet.id()] && graphe.getSuccesseursSommet(sommet).size() > 0) {
                for (Sommet sommetPredecesseur : graphe.getPredecesseursSommet(sommet)) {
                    Arc arc = graphe.getArcFromSommets(sommetPredecesseur, sommet);
                    arc.setDash();
                }
            }
        }
    }
}
