package base;

import java.util.ArrayList;

public class Table {
    String nom;
    ArrayList<Colonne> colonnes;



    public Table() {
    }


    public Table(String nom, ArrayList<Colonne> colonnes) {
        this.nom = nom;
        this.colonnes = colonnes;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public ArrayList<Colonne> getColonnes() {
        return colonnes;
    }
    public void setColonnes(ArrayList<Colonne> colonnes) {
        this.colonnes = colonnes;
    }


    

}
