package view;

import controller.TrataClienteController;
import factory.Conector;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import model.ViagemDao;

public class Principal {
    public static void main(String[] args) {
        try{
            // testando conexão com o banco de dados
            if (Conector.getConnection() != null){
                System.out.println("Conectado com sucesso no banco");
                // para testar o select da marca
            }
            
            // iniciando o servidor socket
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor inicializado! Aguardando conexões...");
            int idUnico = 0; // variável que conta os clientes conectados
            while(true){
                // looping para receber multiplas conexões
                Socket cliente = servidor.accept();
                System.out.println("Um novo cliente se conectou: "+cliente);
                idUnico++; 
                System.out.println("Iniciando um nova thread para o cliente: "+idUnico);
                TrataClienteController trataCliente = new TrataClienteController(cliente,idUnico);
                trataCliente.start();
            }
            
        }catch(IOException e){
            // imprime toda a pilha de erro, todo o erro que aconteceu 
            e.printStackTrace();
        }
    }
}
