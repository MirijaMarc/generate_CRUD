package util;


import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Configuration {


    private static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getNodeMotCle(){
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(new File(Constante.ConfigurationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode.get("MotCle");
    }


    public static JsonNode getNodeExtension(){
        JsonNode jsonNode= null;
        try {
            jsonNode = objectMapper.readTree(new File(Constante.ConfigurationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode.get("Extension");
    }

    public static JsonNode getNodeConversion(){
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(new File(Constante.ConfigurationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode.get("Conversion");
    }

    public static JsonNode getNodeDatabase(){
        JsonNode jsonNode =null;
        try {
            jsonNode = objectMapper.readTree(new File(Constante.ConfigurationDatabase));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode;
    }


    public static JsonNode getNodePackage(){
        JsonNode jsonNode = null;
        try {
            jsonNode =  objectMapper.readTree(new File(Constante.ConfigurationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode.get("packageName");
    }

    public static JsonNode getNodePackagePath(){
        JsonNode jsonNode = null;
        try {
            jsonNode =  objectMapper.readTree(new File(Constante.ConfigurationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonNode.get("packagePath");
    }

    public static String getPackageViewPath()throws Exception{
        JsonNode jsonNode = getNodePackagePath();
        String path= jsonNode.asText();
        String[] splits = path.split("main");
        return splits[0]+ "main/resources/templates/pages";
    }


}
