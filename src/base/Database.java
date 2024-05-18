package base;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import util.Configuration;
import util.Constante;
import util.Util;

public class Database {



    public static void main(String[] args) {
        String h = "abc";
        System.out.println(h.substring(0, h.length()-2));
    }

    public static Connection getConnection() throws Exception{
        Connection con = null;
        Class.forName("org.postgresql.Driver");
        JsonNode jsonNode = Configuration.getNodeDatabase();
        String server = jsonNode.get("database").get("server").asText();
        String port = jsonNode.get("database").get("port").asText(); 
        String username = jsonNode.get("database").get("username").asText(); 
        String password = jsonNode.get("database").get("password").asText(); 
        String database = jsonNode.get("database").get("database").asText(); 
        con = DriverManager.getConnection("jdbc:postgresql://"+server+":"+port+"/"+ database ,username,password);
        return con;
    }



    public static String getColonnes(Connection c, String nomTable)throws Exception{
        Table table = getDetails(c, nomTable);
        String temp="";
        for (Colonne colonne : table.getColonnes()) {
            if (!(colonne.is_etat || colonne.is_primary)){
                temp = temp + colonne.nom;
                temp = temp + ",";
            }
        }
        temp = temp.substring(0, temp.length()-1);
        String rep = "(rep)";
        return rep.replace("rep", temp);
    }



    public static String generateFieldsController(Connection c, String nomTable)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String template = Constante.FIELD_CONT;
            if (colonne.is_foreign){
                template = template.replace("[NomModeleUpper]", Util.toFirstUpper(Util.singulier(colonne.foreign_table_name)));
                template = template.replace("[NomModeleLower]", Util.singulier(colonne.foreign_table_name));
                builder.append(template);
            }
        }
        return builder.toString();
    }


    public static String generateForeigns(Connection c, String nomTable)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String template = """
                            mv.addObject("[data]", [nom]Repository.findAll());
                    """;
            if (colonne.is_foreign){
                template = template.replace("[data]", colonne.foreign_table_name);
                template = template.replace("[nom]", Util.singulier(colonne.foreign_table_name));
                builder.append(template);
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    
    public static String generateEnteteTable(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String content = "";
            if (!colonne.isIs_etat()){
                content = """
                    <th>[value]</th>
                        """;
                content =content.replace("[value]", colonne.getNomMap(json));
            }
            builder.append(content);
            builder.append("\n");
        }
        builder.append("<th>Actions</th>");
        return builder.toString();

    } 


    public static String generateRequeteSearch(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        String debut = "SELECT * from "+ nomTable + " WHERE ";
        builder.append(debut);
        int i = 0;
        for (Colonne colonne : table.getColonnes()) {
            String content = "";
            if (!colonne.isIs_etat() && !colonne.is_primary){
                content = "CAST([name] as varchar) ILIKE %:search% OR ";
                if (i == table.getColonnes().size()-2) content = "CAST([name] as varchar) ILIKE %:search%";
                content =content.replace("[name]", colonne.getNom());
            }
            builder.append(content);
            builder.append("\n");
            i++;
        }
        return builder.toString();

    } 

    public static String generateOptionsTrier(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {

            String content = "";
            if (!colonne.isIs_etat()){
                content = """
                    <option value="[value]" th:selected="${ sortBy == '[value]'}">[name]</option>
                        """;
                
                content =content.replace("[value]", colonne.getNom());
                content =content.replace("[name]", colonne.getNomMap(json));
            }
            builder.append(content);
            builder.append("\n");
        }
        return builder.toString();

    } 

    public static String generateLigneTable(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String content = "";
            if (!colonne.isIs_etat()){
                if (colonne.is_foreign){
                    content = """
                        <td th:text="${element.[value].nom}"></td>
                            """;
                    content =content.replace("[value]", colonne.getNomMap(json));
                }else{
                    content = """
                        <td th:text="${element.[value]}"></td>
                            """;
                    content =content.replace("[value]", colonne.getNomMap(json));
                }

            }
            builder.append(content);
            builder.append("\n");
        }
        return builder.toString();

    } 

    public static String generateInputModifier(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String content = "";
            switch (colonne.getType()) {
                case "String":
                    content = Util.getFileContent(Constante.InputTextTemplatePathUpdate);
                    content = content.replace("[label]", Util.toFirstUpper(colonne.getNom()));
                    content = content.replace("[name]", colonne.getNomMap(json));
                    content = content.replace("[value]", colonne.getNomMap(json));
                    break;
                case "LocalDate":
                    content = Util.getFileContent(Constante.InputDateTemplatePathUpdate);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "LocalDateTime":
                    content = Util.getFileContent(Constante.InputDateTimeTemplatePathUpdate);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "double":
                    content = Util.getFileContent(Constante.InputNumberTemplatePathUpdate);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "int":
                    content = Util.getFileContent(Constante.InputNumberTemplatePathUpdate);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
            }
            if (colonne.is_foreign){
                content = Util.getFileContent(Constante.InputSelectTemplatePathUpdate);
                content = content.replace("[label]", colonne.foreign_table_name);
                content = content.replace("[name]", colonne.getNomMap(json));
                content = content.replace("[data]", Util.pluriel(colonne.foreign_table_name));
            }
            else if(colonne.is_etat || colonne.is_primary) content ="";
            builder.append(content);
            builder.append("\n");
        }
        return builder.toString();

    } 


    public static String generateInputAjouter(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String content = "";
            switch (colonne.getType()) {
                case "String":
                    content = Util.getFileContent(Constante.InputTextTemplatePath);
                    content = content.replace("[label]", Util.toFirstUpper(colonne.getNom()));
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "LocalDate":
                    content = Util.getFileContent(Constante.InputDateTemplatePath);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "LocalDateTime":
                    content = Util.getFileContent(Constante.InputDateTimeTemplatePath);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "double":
                    content = Util.getFileContent(Constante.InputNumberTemplatePath);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
                case "int":
                    content = Util.getFileContent(Constante.InputNumberTemplatePath);
                    content = content.replace("[label]", colonne.getNom());
                    content = content.replace("[name]", colonne.getNomMap(json));
                    break;
            }
            if (colonne.is_foreign){
                content = Util.getFileContent(Constante.InputSelectTemplatePath);
                content = content.replace("[label]", colonne.foreign_table_name);
                content = content.replace("[name]", colonne.getNomMap(json));
                content = content.replace("[data]", Util.pluriel(colonne.foreign_table_name));
            }
            if (colonne.is_primary || colonne.is_etat) content = "";
            builder.append(content);
            builder.append("\n");
        }
        return builder.toString();

    } 


    public static String getSetters(Connection c, String nomTable, String json)throws Exception{
        JsonNode nodeConversion = Configuration.getNodeConversion();
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        String tab = "        ";
        String dto = "DTO.";
        String entity = "entity.";
        String set = "set";
        String get = "get";
        String findById = "Repository.findById(%s).get()";
        String nom, setter, getter;
        for (Colonne colonne : table.getColonnes()) {
            nom = Util.toFirstUpper(colonne.getNomMap(json));
            if (!colonne.is_primary &&  !colonne.is_etat){
                setter = set.concat(nom).concat("([getter])");
                if (nom.contains("Date")){
                    String temp = Util.singulier(nomTable).concat(dto).concat(get).concat(nom).concat("()");
                    String conversion = colonne.type +"""
                        .parse([temp], DateTimeFormatter.ofPattern("[pattern]"))""";
                    getter = conversion.replace("[temp]", temp);
                    if (colonne.type.equalsIgnoreCase("LocalDateTime")){
                        getter = getter.replace("[pattern]", nodeConversion.get("LocalDateTime").asText());
                    }else{
                        getter = getter.replace("[pattern]", nodeConversion.get("LocalDate").asText());
                    }
                }else{ 
                    if (colonne.is_foreign){
                        getter = Util.singulier(Util.singulier(colonne.foreign_table_name)).concat(findById).formatted(Util.singulier(nomTable).concat(dto).concat(get).concat(nom).concat("()"));
                    }else{
                        getter = Util.singulier(nomTable).concat(dto).concat(get).concat(nom).concat("()"); 
                    }
                }
                setter = setter.replace("[getter]", getter).concat(";").concat("\n");
                builder.append(tab.concat(entity).concat(setter));   
            }
        }
        return builder.toString();
        
    }
        

    public static String fieldsEntity(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            if(colonne.is_primary){
                String attribut = Constante.ATTRIBUT_PRIMARY.formatted(colonne.getNom(), colonne.getType());
                builder.append(attribut);
            }else{
                if (colonne.is_foreign){
                    String attribut = Constante.ATTRIBUT_JOIN.formatted(colonne.getNom(), Util.toFirstUpper(Util.singulier(colonne.getForeign_table_name())),colonne.getNomMap(json));
                    builder.append(attribut);
                }else{
                    String attribut = Constante.ATTRIBUT.formatted(colonne.getNom(), colonne.getType(), colonne.getNomMap(json));
                    builder.append(attribut);
                }
            } 
        }
        return builder.toString();
    }


    public static String fieldsDTO(Connection c, String nomTable, String json)throws Exception{
        StringBuilder builder = new StringBuilder();
        Table table = getDetails(c, nomTable);
        for (Colonne colonne : table.getColonnes()) {
            String type = colonne.getType();
            if (colonne.getType().contains("LocalDate")) type = "String";
            System.out.println(colonne.getNomMap(json));
            String temp = Constante.FIELD_DTO.formatted(type, colonne.getNomMap(json));
            builder.append(temp); 
        }
        return builder.toString();
    }


    public static Table getDetails(Connection con,String nomTable)throws Exception{
        JsonNode jsonNode = Configuration.getNodeDatabase();
        ArrayList<Colonne> colonnes = new ArrayList<>();    
        Statement stm = con.createStatement();
        String sql = jsonNode.get("getcolumnsQuery").asText().replace("[tableName]", nomTable);
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            Colonne colonne = new Colonne(rs.getString("column_name"),jsonNode.get("types").get(rs.getString("data_type")).asText(), rs.getBoolean("is_primary"), rs.getBoolean("is_foreign"), rs.getString("foreign_table_name"), rs.getString("foreign_column_name"));
            if (colonne.getNom().contains("etat")) colonne.setIs_etat(true);
            colonnes.add(colonne);
        }
        rs.close();
        stm.close();
        return new Table(nomTable, colonnes);
    }

    public static ArrayList<String> getNomsTables(Connection con)throws Exception{
        ArrayList<String> out = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';");
        while (rs.next()) {
            out.add(rs.getString("table_name"));
        }

        rs.close();
        stmt.close();
        return out;
    }


    
}