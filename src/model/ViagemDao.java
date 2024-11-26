
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
    public ArrayList<Viagem> getViagemLista() {
    ArrayList<Viagem> listaViagens = new ArrayList<>();
    
    String queryViagem = "SELECT * FROM viagem";
    String queryStatusPassageiro = "SELECT passenger_trip_id, viagem_trip_id, passageiro, status, hora_atualizacao, nome " +
                                   "FROM status_passageiro sp " +
                                   "JOIN usuario u ON user_id = passageiro " +
                                   "WHERE viagem_trip_id = ?";

    try (Statement stmtViagem = con.createStatement();
         PreparedStatement psStatusPassageiro = con.prepareStatement(queryStatusPassageiro);
         ResultSet rsViagem = stmtViagem.executeQuery(queryViagem)) {

        while (rsViagem.next()) {
            ArrayList<StatusPassageiro> statusPassageiroList = new ArrayList<>();

            psStatusPassageiro.setInt(1, rsViagem.getInt("trip_id"));
            try (ResultSet rsStatus = psStatusPassageiro.executeQuery()) {
                while (rsStatus.next()) {
                    StatusPassageiro sp = new StatusPassageiro(
                            rsStatus.getInt("passenger_trip_id"),
                            new Viagem(rsStatus.getInt("viagem_trip_id")),
                            new Passageiro(rsStatus.getInt("passageiro"), rsStatus.getString("nome")),
                            rsStatus.getInt("status"),
                            rsStatus.getTimestamp("hora_atualizacao")
                    );
                    statusPassageiroList.add(sp);
                }
            }

            Viagem viagem = new Viagem(
                    rsViagem.getInt("trip_id"),
                    rsViagem.getString("origem"),
                    rsViagem.getString("destino"),
                    rsViagem.getString("data"),
                    rsViagem.getString("saida"),
                    rsViagem.getString("retorno"),
                    rsViagem.getInt("status_viagem"),
                    rsViagem.getInt("condutor"),
                    statusPassageiroList
            );

            listaViagens.add(viagem);
        }
            //fechando stmts e conexões
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
        PreparedStatement stmtViagem = null;
        boolean result = false;
        try {
            con.setAutoCommit(false);
            String sqlViagem = "insert into viagem (origem, destino, data, saida, retorno, status_viagem, condutor) values (?,?,?,?,?,?,?)";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmtViagem = con.prepareStatement(sqlViagem);
            //substituir os ?
            stmtViagem.setString(1, v.getOrigem());
            stmtViagem.setString(2, v.getDestino());
            stmtViagem.setString(3, v.getData());
            stmtViagem.setString(4, v.getSaida());
            stmtViagem.setString(5, v.getRetorno());
            stmtViagem.setInt(6, v.getStatus_viagem());
            stmtViagem.setInt(7, v.getCodCondutor());          
            //executar o script
            stmtViagem.executeUpdate();
            
            // deu tudo certo.
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmtViagem.close();
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
