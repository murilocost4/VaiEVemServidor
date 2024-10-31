package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        
    }
}
