package graphe;

public class Sommet {
    public final int id;
    public final int valeurInt;
    public final String valeurString;

    public Sommet(int id, int valeurInt, String valeurString) {
        this.id = id;
        this.valeurInt = valeurInt;
        this.valeurString = valeurString;
    }

    public int getId() {
        return id;
    }

    public int getValeurInt() {
        return valeurInt;
    }

    public String getValeurString() {
        return valeurString;
    }
}
