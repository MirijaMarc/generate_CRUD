package util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.Database;

public class Util {

    private static JsonNode nodeMotCle = Configuration.getNodeMotCle();
    private static JsonNode nodeExtension = Configuration.getNodeExtension();


    public static String init(String content)throws Exception{
        return content.replace("[package]", Configuration.getNodePackage().asText());
    }


    public static void generateUtil()throws Exception{
        String pathcsv = Configuration.getNodePackagePath().asText().split("/src")[0] + "/csv";
        String pathUtil = Configuration.getNodePackagePath().asText() + "/util";
        Path utilPath = Paths.get(pathUtil);
        if (Files.exists(utilPath)) return;
        System.out.println("Miditra");
        createFolder(pathUtil);
        createFolder(pathcsv);
        String content = getFileContent(Constante.UtilTemplatePath);
        content = content.replace("[projetPath]", Configuration.getNodePackagePath().asText().split("/src")[0]);
        content = init(content);
        createFile(pathUtil+ "/Util.java", content);
    }


    public static void generateEntity(Connection con , String modele, String json)throws Exception{
        createFolder(Configuration.getNodePackagePath().asText() + "/entity");
        String modeleSingulier = singulier(modele);
        String fileName = toFirstUpper(modeleSingulier).concat(nodeExtension.get("java").asText());
        String content = getFileContent(Constante.EntityTemplatePath);
        content = content.replace(nodeMotCle.get("NomModeleUpper").asText() , toFirstUpper(modeleSingulier));
        content = content.replace(nodeMotCle.get("NomModeleLowerPluriel").asText(), modele);
        content = content.replace(nodeMotCle.get("AttributsEntity").asText(), Database.fieldsEntity(con, modele , json));
        content = content.replace("[imports]", Constante.IMPORT_ENTITY);
        content = init(content);
        createFile(Configuration.getNodePackagePath().asText() + "/entity/" + fileName, content);
    }


    public static void generateView(Connection c, String modele, String json)throws Exception{
        Boolean bool = getNode(json).get("view").asBoolean();
        if (!bool.booleanValue()) return;
        createFolder(Configuration.getPackageViewPath());
        String modeleSingulier = singulier(modele);
        String fileName= modeleSingulier.concat(nodeExtension.get("html").asText());
        String content = getFileContent(Constante.ViewTemplatePath);
        content = content.replace(nodeMotCle.get("NomModeleUpper").asText(), toFirstUpper(modeleSingulier));
        content = content.replace(nodeMotCle.get("NomModeleLower").asText(), modeleSingulier);
        content = content.replace(nodeMotCle.get("NomModeleLowerPluriel").asText(), modele);
        content = content.replace(nodeMotCle.get("NomModeleUpperPluriel").asText(),toFirstUpper(modele));
        content = content.replace(nodeMotCle.get("InputsAjouter").asText(),Database.generateInputAjouter(c, modele, json));
        content = content.replace(nodeMotCle.get("InputsModifier").asText(),Database.generateInputModifier(c, modele, json));
        content = content.replace(nodeMotCle.get("LignesTable").asText(),Database.generateLigneTable(c, modele, json));
        content = content.replace("[EnteteTable]", Database.generateEnteteTable(c, modele, json));
        content = init(content);
        createFile(Configuration.getPackageViewPath() +"/"+ fileName, content);
    }


    public static void generateController(Connection con , String modele, String json)throws Exception{
        createFolder(Configuration.getNodePackagePath().asText() + "/controller");
        String modeleSingulier = singulier(modele);
        String fileName = toFirstUpper(modeleSingulier).concat("Controller").concat(nodeExtension.get("java").asText());
        String content = getFileContent(Constante.ControllerTemplatePath);
        content = content.replace(nodeMotCle.get("NomModeleUpper").asText(), toFirstUpper(modeleSingulier));
        content = content.replace(nodeMotCle.get("NomModeleLower").asText(), modeleSingulier);
        content = content.replace(nodeMotCle.get("NomModeleLowerPluriel").asText(), modele);
        content = content.replace(nodeMotCle.get("Setters").asText(), Database.getSetters(con, modele, json));
        content = content.replace("[foreigns]", Database.generateForeigns(con, modele));
        content = content.replace("[imports]", Constante.IMPORT_CONTROLLER);
        content = content.replace("[fieldsController]", Database.generateFieldsController(con, modele));
        content = content.replace("[colonnes]", Database.getColonnes(con, modele));
        content = init(content);
        createFile(Configuration.getNodePackagePath().asText() + "/controller/" + fileName , content);

    }

    public static void generateDTO(Connection con , String modele, String json)throws Exception{
        createFolder(Configuration.getNodePackagePath().asText() + "/dto");
        String modeleSingulier = singulier(modele);
        String fileName = toFirstUpper(modeleSingulier).concat("DTO").concat(nodeExtension.get("java").asText());
        String content = getFileContent(Constante.DTOTemplatePath);
        content = content.replace(nodeMotCle.get("NomModeleUpper").asText(), toFirstUpper(modeleSingulier));
        content = content.replace(nodeMotCle.get("AttributsDTO").asText(), Database.fieldsDTO(con, modele , json));
        content = content.replace("[imports]", Constante.IMPORT_DTO);
        content = init(content);
        createFile(Configuration.getNodePackagePath().asText() + "/dto/" + fileName, content);
    }


    public static void generateRepository(String modele)throws Exception{
        createFolder(Configuration.getNodePackagePath().asText() + "/repository");
        String modeleSingulier = singulier(modele);
        String fileName = toFirstUpper(modeleSingulier).concat("Repository").concat(nodeExtension.get("java").asText());
        String content = getFileContent(Constante.RepoTemplatePath);
        content = content.replace(nodeMotCle.get("NomModeleUpper").asText(), toFirstUpper(modeleSingulier));
        content = content.replace(nodeMotCle.get("NomModeleLower").asText(), modeleSingulier.toLowerCase());
        content = content.replace(nodeMotCle.get("NomModeleLowerPluriel").asText(), modele);
        content = content.replace("[imports]", Constante.IMPORT_REPO);
        content = init(content);
        createFile(Configuration.getNodePackagePath().asText() + "/repository/" + fileName, content);
    }


    public static JsonNode getNode(String path)throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(path));
    }

    public static String pluriel(String nomTable){
        if (nomTable.substring(nomTable.length()-1, nomTable.length()).equalsIgnoreCase("s")) return nomTable;
        return nomTable+"s";
    }

    public static String singulier(String nomTable){
        if (!nomTable.substring(nomTable.length()-1, nomTable.length()).equalsIgnoreCase("s")) return nomTable;
        return nomTable = nomTable.substring(0, nomTable.length()-1);
    }


    public static String getFileContent(String path)throws Exception{
        return new String(Files.readAllBytes(Paths.get(path)));
    }


    public static String toFirstUpper(String name){
        String first = name.substring(0,1).toUpperCase();
        return first.concat(name.substring(1, name.length()));
    }


    public static void createFile(String path, String content){
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFolder(String path){
        try {
            if(!Files.exists(Paths.get(path))){
                Files.createDirectory(Paths.get(path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
