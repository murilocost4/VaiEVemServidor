
package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import modelDominio.Usuario;
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
    String queryStatusPassageiro = "SELECT * from status_passageiro " +
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
                            rsStatus.getInt("viagem_trip_id"),
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
    
   public ArrayList<Viagem> getViagemCondutor(int codCondutor) {
    ArrayList<Viagem> listaViagens = new ArrayList<>();

    String queryViagem = "SELECT * FROM viagem WHERE condutor = ?";
    String queryStatusPassageiro = "SELECT passenger_trip_id, viagem_trip_id, passageiro, status, hora_atualizacao, nome " +
                                   "FROM status_passageiro sp " +
                                   "JOIN usuario u ON user_id = passageiro " +
                                   "WHERE viagem_trip_id = ?";

    try (PreparedStatement psViagem = con.prepareStatement(queryViagem);
         PreparedStatement psStatusPassageiro = con.prepareStatement(queryStatusPassageiro)) {
        
        // Configurando o parâmetro do condutor
        psViagem.setInt(1, codCondutor);
        
        try (ResultSet rsViagem = psViagem.executeQuery()) {
            while (rsViagem.next()) {
                ArrayList<StatusPassageiro> statusPassageiroList = new ArrayList<>();
                
                // Configurando o parâmetro para buscar status dos passageiros
                psStatusPassageiro.setInt(1, rsViagem.getInt("trip_id"));
                try (ResultSet rsStatus = psStatusPassageiro.executeQuery()) {
                    while (rsStatus.next()) {
                        StatusPassageiro sp = new StatusPassageiro(
                                rsStatus.getInt("passenger_trip_id"),
                                rsStatus.getInt("viagem_trip_id"),
                                new Passageiro(
                                        rsStatus.getInt("passageiro"), 
                                        rsStatus.getString("nome")
                                ),
                                rsStatus.getInt("status"),
                                rsStatus.getTimestamp("hora_atualizacao")
                        );
                        statusPassageiroList.add(sp);
                    }
                }

                // Criando o objeto Viagem
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
        }

    } catch (Exception e) {
        e.printStackTrace();
        return null; // Retorna null em caso de erro
    }
    
    return listaViagens; // Retorna a lista de viagens
}

   public ArrayList<Viagem> getViagensPorUsuario(Usuario usuario) {
    ArrayList<Viagem> listaViagensFiltradas = new ArrayList<>();
    
    String queryViagem = "SELECT * FROM viagem v " +
                         "JOIN status_passageiro sp ON v.trip_id = sp.viagem_trip_id " +
                         "WHERE sp.passageiro = ?";
    
    try (PreparedStatement psViagem = con.prepareStatement(queryViagem)) {
        psViagem.setInt(1, usuario.getCodUsuario());
        
        try (ResultSet rsViagem = psViagem.executeQuery()) {
            while (rsViagem.next()) {
                ArrayList<StatusPassageiro> statusPassageiroList = new ArrayList<>();
                
                // Criando o objeto StatusPassageiro com informações da query
                StatusPassageiro sp = new StatusPassageiro(
                        rsViagem.getInt("passenger_trip_id"),
                        rsViagem.getInt("viagem_trip_id"),
                        new Passageiro(rsViagem.getInt("passageiro"), usuario.getNomeUsuario()),
                        rsViagem.getInt("status"),
                        rsViagem.getTimestamp("hora_atualizacao")
                );
                statusPassageiroList.add(sp);

                // Criando o objeto Viagem com informações da query
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

                listaViagensFiltradas.add(viagem);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return listaViagensFiltradas;
}


    
    // métod que fará INSERT no BANCO
    // devolve boolean para saber se deu certo o insert
    public int inserir(Viagem v){
        PreparedStatement stmtViagem = null;
        boolean result = false;
        long id = 0;
        try {
            String sqlViagem = "insert into viagem (origem, destino, data, saida, retorno, status_viagem, condutor) values (?,?,?,?,?,?,?)";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmtViagem = con.prepareStatement(sqlViagem, Statement.RETURN_GENERATED_KEYS);
            //substituir os ?
            stmtViagem.setString(1, v.getOrigem());
            stmtViagem.setString(2, v.getDestino());
            stmtViagem.setString (3,v.getData());
            stmtViagem.setString(4, v.getSaida());
            stmtViagem.setString(5, v.getRetorno());
            stmtViagem.setInt(6, v.getStatus_viagem());
            stmtViagem.setInt(7, v.getCodCondutor());          
            //executar o script
            int affectedRows = stmtViagem.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmtViagem.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                    System.out.println("ID Gerado: "+id);
                }
                
            }
            
            // deu tudo certo.
            result = true;
            return (int) id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        
    }
    }
    
    // executa um UPDATE na TABELA Viagem do banco
    public boolean alterar(Viagem v){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update viagem set origem=?, destino=?, data=?, saida=?, retorno=?, status_viagem=?, condutor=? where trip_id=?";
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setString(1, v.getOrigem());
            stmt.setString(2, v.getDestino());
            stmt.setString (3, v.getData());
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
    
    public boolean excluirDoCondutor(Usuario usr) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean result = true;

    try {
        // Comando SQL para verificar se existem viagens associadas ao condutor
        String sql = "SELECT COUNT(*) FROM viagem WHERE condutor = ?";
        stmt = con.prepareStatement(sql);

        // Substituir o placeholder `?` pelo valor de codCondutor
        stmt.setInt(1, usr.getCodUsuario());

        // Executar a consulta
        rs = stmt.executeQuery();

        // Verificar se existe pelo menos uma viagem associada
        if (rs.next() && rs.getInt(1) > 0) {
            result = false; // Existe uma viagem com o condutor especificado
        }
    } catch (SQLException e) {
        System.err.println("Erro ao verificar viagens do condutor com código " + usr.getCodUsuario() + ": " + e.getMessage());
        e.printStackTrace();
    } finally {
        // Garantir que recursos sejam fechados corretamente
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    return result;
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
            String sql = "UPDATE viagem SET status_viagem = 1 WHERE trip_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, v.getTrip_id());
            int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected > 0) {
            result = true;  // A atualização foi bem-sucedida
        }
        System.out.println("SQL executado: " + sql);
        System.out.println("Trip ID recebido: " + v.getTrip_id());
        System.out.println("Linhas afetadas: " + rowsAffected);

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
            String sql = "UPDATE viagem SET status_viagem = 2 WHERE trip_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, v.getTrip_id());
            int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected > 0) {
            result = true;  // A atualização foi bem-sucedida
        }
        System.out.println("SQL executado: " + sql);
        System.out.println("Trip ID recebido: " + v.getTrip_id());
        System.out.println("Linhas afetadas: " + rowsAffected);

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
    
    public Viagem acompanharViagem(int idViagem) {
        // SQL para recuperar as informações da viagem
        String sql = "SELECT * " +
                     "FROM viagem WHERE trip_id = ?";
        String queryStatusPassageiro = "SELECT passenger_trip_id, viagem_trip_id, passageiro, status, hora_atualizacao, nome " +
                                   "FROM status_passageiro sp " +
                                   "JOIN usuario u ON user_id = passageiro " +
                                   "WHERE viagem_trip_id = ?";

        // Variáveis para conexão e resultado
        PreparedStatement stmtViagem = null;
        PreparedStatement stmtStatusPassageiro = null;
        ResultSet resultSet = null;
        Viagem viagem = null;

        try {
            // Obter conexão com o banco de dados

            // Preparar a consulta
            stmtViagem = con.prepareStatement(sql);
            stmtStatusPassageiro = con.prepareStatement(queryStatusPassageiro);
            stmtViagem.setInt(1, idViagem); // Passar o id da viagem como parâmetro
            stmtStatusPassageiro.setInt(1, idViagem); // Passar o id da viagem como parâmetro

            // Executar a consulta
            resultSet = stmtViagem.executeQuery();
            
            ArrayList<StatusPassageiro> statusPassageiroList = new ArrayList<>();

            stmtStatusPassageiro.setInt(1, idViagem);
            try (ResultSet rsStatus = stmtStatusPassageiro.executeQuery()) {
                while (rsStatus.next()) {
                    StatusPassageiro sp = new StatusPassageiro(
                            rsStatus.getInt("passenger_trip_id"),
                            rsStatus.getInt("viagem_trip_id"),
                            new Passageiro(rsStatus.getInt("passageiro"), rsStatus.getString("nome")),
                            rsStatus.getInt("status"),
                            rsStatus.getTimestamp("hora_atualizacao")
                    );
                    statusPassageiroList.add(sp);
                }
            }

            // Verificar se encontrou dados para a viagem

                viagem = new Viagem(
                    resultSet.getInt("trip_id"),
                    resultSet.getString("origem"),
                    resultSet.getString("destino"),
                    resultSet.getString("data"),
                    resultSet.getString("saida"),
                    resultSet.getString("retorno"),
                    resultSet.getInt("status_viagem"),
                    resultSet.getInt("condutor"),
                    statusPassageiroList
                );

                
        } catch (SQLException e) {
            // Lidar com erros de SQL
            e.printStackTrace();
        } finally {
            // Fechar recursos
            try {
                stmtViagem.close();
                stmtStatusPassageiro.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

        return viagem;
    }
    
}
