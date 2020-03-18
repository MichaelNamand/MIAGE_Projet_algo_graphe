package graphe;

public class Sommet {
    public final int id;
    public final String valeur;

    public Sommet(int id, String valeur) {
        this.id = id;
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public String getValeur() {
        return valeur;
    }
}
