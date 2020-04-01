package algorithmes;

import graphe.Graphe;
import graphe.Sommet;

import java.util.ArrayList;

public class Rang {

    public static void setRang(Graphe graphe) {
        int k = 0;
        ArrayList<Sommet> R = new ArrayList<>(graphe.getSommets());
        ArrayList<Sommet> Y = new ArrayList<>(graphe.getSommets());
        for (Sommet sommet : Y) {
            ArrayList<Sommet> successeurs = graphe.getSuccesseursSommet(sommet);
            R.removeAll(successeurs);
        }

        while(R.size() != 0) {
            for (Sommet sommet : R) {
                sommet.setRang(k);
                graphe.getSommet(sommet.id()).setRang(k);
            }
            k++;
            Y.removeAll(R);
            R = new ArrayList<>(Y);
            for (Sommet s : Y) {
                ArrayList<Sommet> successeurs = graphe.getSuccesseursSommet(s);
                R.removeAll(successeurs);
            }
        }

        if (Y.size() != 0) {
            for (Sommet s : graphe.getSommets()) {
                s.setRang(-1);
            }
        }
    }
}
