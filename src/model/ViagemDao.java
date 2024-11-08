
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.Viagem;

/**
 *
 * @author murilocost4
 */
public class ViagemDao {
    private Connection con;
    
    // construtor que conecta no banco
    public ViagemDao(){
        con = Conector.getConnection();
    }
    
    // metodo que irá selecionar todas as viagens
    // select * from Viagem
    public ArrayList<Viagem> getViagemLista(){
       Statement stmt = null; // usar para rodar o script SQL
       // variaável para guardar Viagem selecionadas 
       ArrayList<Viagem> listaViagens = new ArrayList<>();
        try {
            // criando o objeto stmt para rodar o script
            stmt = con.createStatement();
            // executar o script
            ResultSet result = stmt.executeQuery("select * from viagem");
            // percorrer o result pegando todos os registros de Viagem
            // enquanto tiver resultado disponível fica dentro do looping
            while(result.next()){
                // criar uma Viagem baseado no registro que estou percorrendo...
                Viagem v = new Viagem(result.getInt("trip_id"),
                                      result.getString("origem"),
                                      result.getString("destino"),
                                      result.getDate("data"),
                                      result.getTime("saida").toLocalTime(),
                                      result.getTime("retorno").toLocalTime(),
                                      result.getInt("status_viagem")); 
                // imprimindo a viagem
                System.out.println(v);
                // adicionando a viagem na lista
                listaViagens.add(v);
            }
            //fechando stmts e conexões
            result.close();
            stmt.close();
            con.close();
            return listaViagens;
        } catch (Exception e) {
            e.printStackTrace();
            // se deu erro não tem nada para devolver por isso return null
            return null;
        }
    }
}
