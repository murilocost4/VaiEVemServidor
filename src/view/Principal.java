package view;

import factory.Conector;
import java.io.IOException;

public class Principal {
    public static void main(String[] args) {
        try {
            if (Conector.getConnection() != null) {
                System.out.println("Conectado no banco!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
