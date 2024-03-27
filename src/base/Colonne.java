package base;

import com.fasterxml.jackson.databind.JsonNode;

import util.Util;

public class Colonne {
    String nom;
    String type;
    boolean is_primary;
    boolean is_foreign;
    String foreign_table_name;
    String foreign_column_name;
    boolean is_etat = false;


    
    public Colonne() {
    }


    public Colonne(String nom, String type, boolean is_primary, boolean is_foreign, String foreign_table, String foreign_column) {
        setNom(nom);
        setType(type);
        setIs_primary(is_primary);
        setIs_foreign(is_foreign);
        setForeign_table_name(foreign_table);
        setForeign_column_name(foreign_column);
    }


    public String getNomMap(String json)throws Exception{
        if (this.is_primary) return "id";
        if (this.is_etat) return "etat";
        JsonNode jsonNode = Util.getNode(json).get("object");
        JsonNode map = jsonNode.get(getNom());
        if (map == null) return getNom();
        return map.asText();
    }


    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



    public boolean isIs_foreign() {
        return is_foreign;
    }


    public void setIs_foreign(boolean is_foreign) {
        this.is_foreign = is_foreign;
    }


    public boolean isIs_primary() {
        return is_primary;
    }


    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }


    public String getForeign_table_name() {
        return foreign_table_name;
    }


    public void setForeign_table_name(String foreign_table_name) {
        this.foreign_table_name = foreign_table_name;
    }


    public String getForeign_column_name() {
        return foreign_column_name;
    }


    public void setForeign_column_name(String foreign_column_name) {
        this.foreign_column_name = foreign_column_name;
    }


    public boolean isIs_etat() {
        return is_etat;
    }


    public void setIs_etat(boolean is_etat) {
        this.is_etat = is_etat;
    }


    

    

    



}
