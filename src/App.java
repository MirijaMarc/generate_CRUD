
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

import base.Database;
import util.Util;
public class App {
    public static void main(String[] args) throws Exception {
        Connection c = null;
        try (Scanner scanner = new Scanner(System.in)) {
            c = Database.getConnection();
            ArrayList<String> tables = Database.getNomsTables(c);
            System.out.println("Liste des tables disponibles: ");
            for (int i = 0; i < tables.size(); i++) {
                System.out.println( i+1 +"> " + tables.get(i) );
            }
            System.out.print("Votre choix: ");
            String index = scanner.nextLine();
            String tableChoisie = tables.get(Integer.parseInt(index)-1);
            String pathMap = "./mapping/%s.json".formatted(tableChoisie); 
            Util.generateRepository(tableChoisie);
            Util.generateDTO(c, tableChoisie, pathMap);
            Util.generateController(c,tableChoisie, pathMap);
            Util.generateView(c, tableChoisie, pathMap);
            Util.generateEntity(c, tableChoisie, pathMap);
            Util.generateUtil();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        // try {
        //     c = Database.getConnection();
        //     System.out.println(Database.getColonnes(c, "pilotes"));

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // finally{
        //     c.close();
        // }
      



    }
}
