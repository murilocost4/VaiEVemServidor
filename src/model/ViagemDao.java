
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
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
    
    // métod que fará INSERT no BANCO
    // devolve boolean para saber se deu certo o insert
    public boolean inserir(Viagem v){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "insert into viagem (origem, destino, data, saida, retorno, status_viagem) values (?,?,?,?,?,?)";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setString(1, v.getOrigem());
            stmt.setString(2, v.getDestino());
            stmt.setString(3, v.getData().toString());
            stmt.setString(4, v.getSaida().format(DateTimeFormatter.ISO_DATE));
            stmt.setString(5, v.getRetorno().format(DateTimeFormatter.ISO_DATE));
            stmt.setInt(6, v.getStatus_viagem());
            //executar o script
            stmt.execute();
            // deu tudo certo.
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
    // executa um UPDATE na TABELA Viagem do banco
    public boolean alterar(Viagem v){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update viagem set destino=?, origem=?, data=?, saida=?, retorno=?, status_viagem=? where trip_id=?";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setString(1, v.getOrigem());
            stmt.setString(2, v.getDestino());
            stmt.setString(3, v.getData().toString());
            stmt.setString(4, v.getSaida().format(DateTimeFormatter.ISO_DATE));
            stmt.setString(5, v.getRetorno().format(DateTimeFormatter.ISO_DATE));
            stmt.setInt(6, v.getStatus_viagem());
            stmt.setInt(7, v.getTrip_id());
            //executar o script
            stmt.execute();
            // deu tudo certo.
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
    //Executa um DELETE na TABELA Viagem do Banco
    public boolean excluir(Viagem v){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from viagem where trip_id=?";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setInt(1, v.getTrip_id());
            //executar o script
            stmt.execute();
            // deu tudo certo.
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
}
