
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelDominio.Passageiro;
import modelDominio.StatusPassageiro;
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
                ResultSet res = stmt.executeQuery("select * from status_passageiro \n" +
                                                "join usuario on (usuario.user_id=status_passageiro.passageiro)\n" +
                                                "where viagem_trip_id="+result.getInt("trip_id"));
                ArrayList<StatusPassageiro> statusPassageiro = new ArrayList<>();
                
                while(res.next()) {
                    statusPassageiro.add(new StatusPassageiro(res.getInt("passenger_trip_id"), 
                                                              new Viagem(result.getInt("trip_id")),
                                                              new Passageiro(res.getInt("passageiro"),res.getString("nome")),
                                                              res.getInt("status"),
                                                              res.getTimestamp("hora_atualizacao")));
                }
                
                // criar uma Viagem baseado no registro que estou percorrendo...
                Viagem v = new Viagem(result.getInt("trip_id"),
                                      result.getString("origem"),
                                      result.getString("destino"),
                                      result.getString("data"),
                                      result.getString("saida"),
                                      result.getString("retorno"),
                                      result.getInt("status_viagem"),
                                      result.getInt("condutor"),
                                      statusPassageiro);
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
            String sql = "insert into viagem (origem, destino, data, saida, retorno, status_viagem, condutor) values (?,?,?,?,?,?,?)";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setString(1, v.getOrigem());
            stmt.setString(2, v.getDestino());
            stmt.setString(3, v.getData());
            stmt.setString(4, v.getSaida());
            stmt.setString(5, v.getRetorno());
            stmt.setInt(6, v.getStatus_viagem());
            stmt.setInt(7, v.getCodCondutor());
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
            String sql = "update viagem set destino=?, origem=?, data=?, saida=?, retorno=?, status_viagem=?, condutor=? where trip_id=?";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setString(1, v.getOrigem());
            stmt.setString(2, v.getDestino());
            stmt.setString(3, v.getData());
            stmt.setString(4, v.getSaida());
            stmt.setString(5, v.getRetorno());
            stmt.setInt(6, v.getStatus_viagem());
            stmt.setInt(7, v.getCodCondutor());
            stmt.setInt(8, v.getTrip_id());
            
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
    
    public boolean iniciar(Viagem v) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update viagem set status_viagem=2 where trip_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, v.getTrip_id());
            stmt.execute();
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
    
    public boolean finalizar(Viagem v) {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update viagem set status_viagem=3 where trip_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, v.getTrip_id());
            stmt.execute();
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
    
    public Map<String, Object> acompanharViagem(int idViagem) {
        // SQL para recuperar as informações da viagem
        String sql = "SELECT origem, destino, data, saida, retorno, status_viagem, condutor " +
                     "FROM viagem WHERE trip_id = ?";

        // Variáveis para conexão e resultado
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String, Object> dadosViagem = new HashMap<>();

        try {
            // Obter conexão com o banco de dados

            // Preparar a consulta
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, idViagem); // Passar o id da viagem como parâmetro

            // Executar a consulta
            resultSet = preparedStatement.executeQuery();

            // Verificar se encontrou dados para a viagem
            if (resultSet.next()) {
                dadosViagem.put("origem", resultSet.getString("origem"));
                dadosViagem.put("destino", resultSet.getString("destino"));
                dadosViagem.put("data", resultSet.getString("data"));
                dadosViagem.put("saida", resultSet.getString("saida"));
                dadosViagem.put("retorno", resultSet.getString("retorno"));
                dadosViagem.put("status_viagem", resultSet.getInt("status_viagem"));
                dadosViagem.put("codCondutor", resultSet.getInt("condutor"));
            } else {
                // Caso não encontre a viagem
                dadosViagem.put("erro", "Viagem não encontrada.");
            }
        } catch (SQLException e) {
            // Lidar com erros de SQL
            e.printStackTrace();
            dadosViagem.put("erro", "Erro ao acessar os dados da viagem.");
        } finally {
            // Fechar recursos
            try {
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

        return dadosViagem;
    }
    
}
