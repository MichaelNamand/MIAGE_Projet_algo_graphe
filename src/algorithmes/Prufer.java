package algorithmes;

import graphe.Graphe;

public class Prufer{

    public static int[] codagePrufer(Graphe graphe)
    {
        int[][] matAdj = graphe.getMatAdj();
        int m = matAdj[0][0];
        int[] tabPrufer = new int[m - 1];
        for (int i = 1; i <= m; i++) {
            matAdj[i][0] = 0;
            for (int j = 1; j <= m; j++) {
                if(matAdj[i][j] == 1) {
                    matAdj[i][0]++;
                }
            }
        }
        tabPrufer[0] = m - 2;
        for (int k = 1; k <= m - 2; k++) {
            int i = 1;
            for (i = 1; i <= m; i++) {
                if (matAdj[i][0] == 1) {
                    matAdj[i][0] = 0;
                    break;
                }
            }
            for (int j = 1; j <= m; j++) {
                if(matAdj[i][j] == 1) {
                    tabPrufer[k] = j;
                    matAdj[i][j] = 0;
                    matAdj[j][i] = 0;
                    matAdj[j][0]--;
                    break;
                }
            }
        }
        return tabPrufer;
    }

    public static String affichePrufer(Graphe graphe){
        int[] tabPrufer = codagePrufer(graphe);
        String suitePrufer = "{ ";
        for(int i=0; i < tabPrufer.length-1 ; i++){
            suitePrufer += tabPrufer[i] + ", ";
        }
        suitePrufer += tabPrufer[tabPrufer.length-1] + "}";
        return  suitePrufer;
    }

}
