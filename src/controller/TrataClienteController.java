package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.UsuarioDao;
import modelDominio.Usuario;

public class TrataClienteController extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int idUnico;
    
    public TrataClienteController(Socket socket, int idUnico) {
        this.socket = socket;
        this.idUnico = idUnico;
        try {
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        String comando;
        System.out.println("Esperando comandos do cliente: " + idUnico);
        try {
            comando = (String) in.readObject();
            // enquanto o comando for DIFERENTE de "FIM"
            while (!comando.equalsIgnoreCase("fim")) {
                System.out.println("Cliente " + idUnico + " enviou comando: " + comando);

                // daqui em diante iremos tratar todos os comandos 
                // em blocos de IF/else
                if (comando.equalsIgnoreCase("UsuarioEfetuarLogin")){
                    out.writeObject("ok");
                    Usuario user = (Usuario) in.readObject();
                    System.out.println("Usuário recebido do cliente: "+user);
                    // Proximo passo é consultar no banco de dados para verificar
                    // se este usuário existe.
                    // por enquanto estamos devolvendo NULO pois ainda não temos
                    // a consulta no Banco
                    UsuarioDao usdao = new UsuarioDao();
                    Usuario userLogin = usdao.efetuarLogin(user);
                    System.out.println(userLogin);
                    out.writeObject(userLogin);
                } else if (comando.equalsIgnoreCase("")) {
                    
                }
                
                // releitura do proximo comando
                comando = (String) in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // fechando conexão com o cliente
        try {
            System.out.println("Cliente "+idUnico+" finalizou a conexão");
            in.close();
            out.close();
            socket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
