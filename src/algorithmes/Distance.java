package algorithmes;

import graphe.Graphe;
import graphe.Sommet;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Distance {
    public static int[][] getMatDistance(Graphe graphe) {
        int[][] fsAps = graphe.getFsAps();
        int[] aps = fsAps[1];

        int n = aps[0];
        int[][] matDist = new int[n+1][];
        matDist[0] = new int[1];
        matDist[0][0] = n;
        for(int i = 1; i <= n; i++) {
            matDist[i] = calculDist(graphe, i);
        }
        return matDist;
    }

     private static  int[] calculDist(Graphe graphe, int s) {
         int[][] fsAps = graphe.getFsAps();
         int[] fs = fsAps[0];
         int[] aps = fsAps[1];
         int n = aps[0];
         int[] dist = new int[n+1];
         dist[0] = n;
         for(int i=1; i <= n; i++)
             dist[i] = -1;
         dist[s] = 0;
         int d = 0;
         int[] fa = new int[n];
         fa[0] = s;
         int t = -1, q = 0, p = 0;
         while(t < q) {
             d++;
             for(int i = t + 1; i <= q; i++) {
                 int u = fa[i];
                 int v;
                 for(int k = aps[u]; (v = fs[k]) != 0; k++) {
                     if(dist[v] == -1){
                         dist[v] = d;
                         fa[++p] = v;
                     }
                 }
             }
             t = q;
             q = p;
         }
         return dist;
     }

    public static void distance(Graphe graphe) {
        graphe.retablirAffichage();

        List<String> listSommets = new ArrayList<>();
        for (Sommet sommet : graphe.getSommets()) {
            listSommets.add(sommet.id() + "");
        }

        ChoiceDialog dialog = new ChoiceDialog(listSommets.get(0), listSommets);
        dialog.setTitle("Choisir un sommet");
        dialog.setContentText("Sélectionnez un sommet de départ pour calculer la \ndistance par rapport aux autres sommets");
       dialog.showAndWait().ifPresent(sommet -> {
           int s = Integer.parseInt(sommet.toString());
           graphe.getSommet(s).getCercle().setFill(Color.LIGHTGREEN);
           int[] dist = calculDist(graphe, s);

           for (int i = 1; i <= graphe.getSommets().size(); i++) {
               graphe.getSommet(i).setDureeAffichage(dist[i]);
           }
       });
    }
}
