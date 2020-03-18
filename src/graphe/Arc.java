package graphe;

public class Arc {
    public final int cout;
    public final Sommet depart;
    public final Sommet arrivee;

    public Arc(int cout, Sommet depart, Sommet arrivee) {
        this.cout = cout;
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public int getCout() {
        return cout;
    }

    public Sommet getDepart() {
        return depart;
    }

    public Sommet getArrivee() {
        return arrivee;
    }
}
