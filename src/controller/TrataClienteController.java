package controller;
/*aaa*/

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import model.StatusPassageiroDao;
import model.UsuarioDao;
import model.ViagemDao;
import modelDominio.Condutor;
import modelDominio.Passageiro;
import modelDominio.StatusPassageiro;
import modelDominio.Usuario;
import modelDominio.Viagem;

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
                    System.out.println("Usuário recebido do cliente: "+user.toString());
                    // Proximo passo é consultar no banco de dados para verificar
                    // se este usuário existe.
                    // por enquanto estamos devolvendo NULO pois ainda não temos
                    // a consulta no Banco
                    UsuarioDao usdao = new UsuarioDao();
                    Usuario userLogin = usdao.efetuarLogin(user);
                    System.out.println(userLogin);
                    out.writeObject(userLogin);
                }else if (comando.equalsIgnoreCase("CondutorLista")){
                    // esse comando irá retornar todos os registros
                    // que existem na tabela Condutor
                    // criar objeto de ViagemDao
                    UsuarioDao usDao = new UsuarioDao();
                    // chama método getViagemLista() e guarda resultado em listaViagens
                    ArrayList<Condutor> listaCondutor = usDao.getListaCondutor();
                    //devolve a lista para o cliente
                    out.writeObject(listaCondutor);
                }else if (comando.equalsIgnoreCase("PassageiroLista")){
                    // esse comando irá retornar todos os registros
                    // que existem na tabela Condutor
                    // criar objeto de ViagemDao
                    UsuarioDao usDao = new UsuarioDao();
                    // chama método getViagemLista() e guarda resultado em listaViagens
                    ArrayList<Passageiro> listaPassageiro = usDao.getListaPassageiro();
                    //devolve a lista para o cliente
                    out.writeObject(listaPassageiro);
                }else if (comando.equalsIgnoreCase("ViagemLista")){
                    // esse comando irá retornar todos os registros
                    // que existem na tabela Viagem
                    // criar objeto de ViagemDao
                    ViagemDao vDao = new ViagemDao();
                    // chama método getViagemLista() e guarda resultado em listaViagens
                    ArrayList<Viagem> listaViagens = vDao.getViagemLista();
                    //devolve a lista para o cliente
                    out.writeObject(listaViagens);
                }else if (comando.equalsIgnoreCase("ViagemCondutorLista")){
                    // esse comando irá retornar todos os registros
                    // que existem na tabela Viagem
                    // criar objeto de ViagemDao
                    ViagemDao vDao = new ViagemDao();
                    int codCondutor = (int) in.read();
                    // chama método getViagemLista() e guarda resultado em listaViagens
                    ArrayList<Viagem> listaViagens = vDao.getViagemCondutor(codCondutor);
                    //devolve a lista para o cliente
                    out.writeObject(listaViagens);
                }else if (comando.equalsIgnoreCase("ViagemInserir")){
                    // comando parar inserir em Viagem
                    out.writeObject("ok"); // envia ok para clinte
                    Viagem v = (Viagem) in.readObject(); // recebendo Viagem 
                    // criando o ViagemDao para chamar o comando inserir.
                    ViagemDao vDao = new ViagemDao();
                    // chamando método inserir e devolvendo o boolean para o cliente
                    out.writeObject(vDao.inserir(v));
                }else if (comando.equalsIgnoreCase("ViagemAlterar")){
                    // comando parar alterar em Viagem
                    out.writeObject("ok"); // envia ok para clinte
                    Viagem v = (Viagem) in.readObject(); // recebendo Viagem do cliente
                    // criando o ViagemDao para chamar o comando Alterar.
                    ViagemDao vDao = new ViagemDao();
                    // chamando método alterar e devolvendo o boolean para o cliente
                    out.writeObject(vDao.alterar(v));
                }else if (comando.equalsIgnoreCase("ViagemExcluir")){
                    // comando parar excluir em Viagem
                    out.writeObject("ok"); // envia ok para clinte
                    Viagem v = (Viagem) in.readObject(); // recebendo Viagem do cliente
                    // criando o ViagemDao para chamar o comando excluir.
                    ViagemDao vDao = new ViagemDao();
                    // chamando método excluir e devolvendo o boolean para o cliente
                    out.writeObject(vDao.excluir(v));
                }else if (comando.equalsIgnoreCase("UsuarioAlterar")){
                    out.writeObject("ok"); 
                    // esperando o objeto usuáriolistamarca vir do cliente
                    Usuario usr = (Usuario) in.readObject();
                    System.out.println(usr);
                    // criando um Dao para armazenar no Banco
                    UsuarioDao usrdao = new UsuarioDao();
                    out.writeObject(usrdao.alterar(usr));
                }else if (comando.equalsIgnoreCase("UsuarioInserir")){
                    out.writeObject("ok"); 
                    // esperando o objeto usuário vir do cliente
                    Usuario usr = (Usuario) in.readObject();
                    // criando um Dao para armazenar no Banco
                    UsuarioDao usrdao = new UsuarioDao();
                    out.writeObject(usrdao.inserir(usr));
                }else if (comando.equalsIgnoreCase("UsuarioExcluir")){
                    out.writeObject("ok"); 
                    // esperando o objeto usuário vir do cliente
                    Usuario usr = (Usuario) in.readObject();
                    // criando um Dao para armazenar no Banco
                    UsuarioDao usrdao = new UsuarioDao();
                    out.writeObject(usrdao.excluir(usr));
                }else if (comando.equalsIgnoreCase("UsuarioLista")){
                    out.writeObject("ok"); 
                    // no pedido da lista não precisa mais nenhum parâmetro
                    // criando um Dao para armazenar no Banco
                    UsuarioDao usrdao = new UsuarioDao();
                    ArrayList<Usuario> listausr = usrdao.getListaUsuarios();
                    out.writeObject(listausr);
                } else if (comando.equalsIgnoreCase("viagemIniciar")) {
                    out.writeObject("ok");
                    Viagem v = (Viagem) in.readObject();
                    ViagemDao vDao = new ViagemDao();
                    out.writeObject(vDao.iniciar(v));
                } else if (comando.equalsIgnoreCase("viagemFinalizar")) {
                    out.writeObject("ok");
                    Viagem v = (Viagem) in.readObject();
                    ViagemDao vDao = new ViagemDao();
                    out.writeObject(vDao.finalizar(v));
                } else if (comando.equalsIgnoreCase("acompanharViagem")) {
                    out.writeObject("ok");
                    Viagem v = (Viagem) in.readObject();
                    ViagemDao vDao = new ViagemDao();
                    out.writeObject(vDao.acompanharViagem(idUnico));
                    
                }else if (comando.equalsIgnoreCase("statusPassageiroInserir")) {
                    out.writeObject("ok");
                    StatusPassageiro sp = (StatusPassageiro) in.readObject();
                    StatusPassageiroDao spDao = new StatusPassageiroDao();
                    out.writeObject(spDao.inserir(sp));
                
                }else if (comando.equalsIgnoreCase("excluirPassageiros")) {
                    out.writeObject("ok");
                    Viagem v = (Viagem) in.readObject();
                    StatusPassageiroDao spDao = new StatusPassageiroDao();
                    out.writeObject(spDao.excluirDaViagem(v));
                
                }else if (comando.equalsIgnoreCase("excluirStatusPassageiro")) {
                    out.writeObject("ok");
                    StatusPassageiro sp = (StatusPassageiro) in.readObject();
                    StatusPassageiroDao spDao = new StatusPassageiroDao();
                    out.writeObject(spDao.excluir(sp));
                
                }else if (comando.equalsIgnoreCase("acompanharViagem")) {
                    out.writeObject("ok");
                    int idViagem = (int) in.readObject();
                    ViagemDao vDao = new ViagemDao();
                    out.writeObject(vDao.acompanharViagem(idViagem));
                
                }else if (comando.equalsIgnoreCase("selecionaEmbarcou")) {
                    out.writeObject("ok");
                    StatusPassageiro sp = (StatusPassageiro) in.readObject();
                    StatusPassageiroDao spDao = new StatusPassageiroDao();
                    out.writeObject(spDao.selecionaEmbarcou(sp));
                
                }else if (comando.equalsIgnoreCase("selecionaAusente")) {
                    out.writeObject("ok");
                    StatusPassageiro sp = (StatusPassageiro) in.readObject();
                    StatusPassageiroDao spDao = new StatusPassageiroDao();
                    out.writeObject(spDao.selecionaAusente(sp));
                
                }else{
                    // comando inválido e não reconhecido
                    out.writeObject("nok"); 
                }
                
                // lendo o próximo comando
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

