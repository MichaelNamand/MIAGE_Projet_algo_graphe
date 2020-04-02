package algorithmes;

import graphe.Arc;
import graphe.Graphe;
import graphe.Sommet;

import java.util.SortedMap;

public class Dijkstra {
    int INT_MAX=10;

    //int s, int[] fs, int[] aps, int[][] couts, int[] d, int[] pred

    void dijkstra(Graphe graphe) {

        //initialisation variable
        int[][] fsAps = graphe.getFsAps();
        int[] fs = fsAps[0];
        int[] aps = fsAps[1];
        int n = aps[0];



            int[][] couts = new int[graphe.getSommets().size()][graphe.getSommets().size()];
            int[] d;
            int[] pred;

            //initialisation tableau de cout
            for (int i = 0; i < graphe.getSommets().size(); i++) {
                for (int j = 0; j < graphe.getSommets().size(); j++) {
                    couts[i][j] = graphe.getArcFromSommets(graphe.getSommet(i), graphe.getSommet(j)).getCout();
                }
            }

        for(int sommet=0; sommet<graphe.getSommets().size();sommet++) {
            d = new int[n + 1]; // Maintient l'information sur la plus petite distance reliant un sommet de S vers X (plus petit pont)
            pred = new int[n + 1];
            boolean[] inS = new boolean[n + 1]; // Renseigne à chaque étape si le sommet i est dans S ou non.
            // Initialisation du processus
            for (int i = 1; i <= n; i++) {
                d[i] = couts[sommet][i];
                inS[i] = false;
                if (d[i] != INT_MAX) {
                    pred[i] = sommet;
                } else {
                    pred[i] = -1;
                }
            }
            inS[sommet] = true;
            int cpt = n - 1;
            while (cpt > 0) {
                int j = dmin(d, inS); // Sommet recherché. Case minimum n'appartenant pas à S
                if (j == -1) {
                    return; // Il ne reste plus de sommet accessible à partir de S
                }
                inS[j] = true;
                int k; // Successeur non traité (absents de S)
                for (int p = aps[j]; (k = fs[p]) != 0; p++) {
                    if (!inS[k]) {
                        // On compare l'ancienne valeure de d pour k
                        // On en tient compte uniquement si d[j] et son cout de j vers k différents de +∞
                        int v; // Valeur du chemin le plus court pour aller vers k passant par j
                        if (couts[j][k] != INT_MAX) {
                            v = d[j] + couts[j][k];
                            if (v < d[k]) {
                                d[k] = v;
                                pred[k] = j;
                            }
                        }
                    }
                }
                cpt--;

            }
            System.out.println("sommet: "+ sommet);
        }

    }

    public int dmin(int[] d,boolean[] inS){
        int i=0;

        while ( i < inS.length && inS[i]){
            i++;
            
        }

        return d[i];
    }

    public static void main(String[] args) {
        Dijkstra di = new Dijkstra();
        Graphe gr = new Graphe("lool");
        Sommet s1= new Sommet(1, "s1", gr);
        Sommet s2= new Sommet(2, "s2", gr);
        Arc a1 = new Arc(10, s1,s2,gr);
        di.dijkstra(gr);


    }

}
