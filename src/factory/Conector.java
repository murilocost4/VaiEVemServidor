package factory;

import java.sql.*;

public class Conector {
    
    private static Connection con;
    
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String banco = "vaievem";
            String usuario = "root";
            String senha = "";
            
            con = DriverManager.getConnection(url+banco,usuario,senha);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            // se der erro ao conector, irei devolver a conexão nula
            return null; 
        }
    }
}
