package model;

import factory.Conector;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelDominio.Admin;
import modelDominio.Condutor;
import modelDominio.Passageiro;
import modelDominio.StatusPassageiro;
import modelDominio.Usuario;
import modelDominio.Viagem;
/**
 *
 * @author mariana
 */
public class StatusPassageiroDao {
    
    private Connection con;
    
    public StatusPassageiroDao() {
        con = Conector.getConnection();
    }
    
    public boolean inserir(StatusPassageiro sp) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean result = false;
    try {
        // Desliga o autocommit
        con.setAutoCommit(false);
        
        // Verifica se o passageiro já está registrado na viagem
        String checkSql = "SELECT COUNT(*) FROM status_passageiro WHERE viagem_trip_id = ? AND passageiro = ?";
        stmt = con.prepareStatement(checkSql);
        stmt.setInt(1, sp.getViagem());
        stmt.setInt(2, sp.getPassageiro().getCodUsuario());
        rs = stmt.executeQuery();
        
        if (rs.next() && rs.getInt(1) > 0) {
            // Passageiro já registrado na viagem
            System.out.println("Passageiro já registrado na viagem.");
            return false;
        }
        
        // Prepara o script de inserção
        String sql = "INSERT INTO status_passageiro (status, hora_atualizacao, viagem_trip_id, passageiro)"
                + " VALUES (?, ?, ?, ?)";
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, sp.getStatus());
        stmt.setTimestamp(2, sp.getHoraAtualizacao());
        stmt.setInt(3, sp.getViagem());
        stmt.setInt(4, sp.getPassageiro().getCodUsuario());

        // Executa o script SQL
        stmt.execute();
        // Efetiva a transação
        con.commit();
        result = true;
    } catch (Exception e) {
        e.printStackTrace();
        try {
            con.rollback(); // Reverte a transação em caso de erro
        } catch (Exception rollbackEx) {
            rollbackEx.printStackTrace();
        }
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return result;
    }

    
    /*public boolean inserir(StatusPassageiro sp) {
        //vai receber o script SQL de INSERT 
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            
            // desliga o autocommit
            con.setAutoCommit(false);
            // o ? será substituído pelo valor
            String sql = "insert into status_passageiro (status,hora_atualizacao,viagem_trip_id,passageiro)"
                    + " values (?,?,?,?) ";
            stmt = con.prepareStatement(sql);
            //substituir os ? do script SQL
            stmt.setInt(1, sp.getStatus());
            stmt.setTimestamp(2, sp.getHoraAtualizacao());
            stmt.setInt(3, sp.getViagem());
            stmt.setInt(4, sp.getPassageiro().getCodUsuario());

            //executar o SCRIPT SQL
            stmt.execute();
            //efetivar a transação
            con.commit();
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
    }*/
    
    public boolean excluirDaViagem(Viagem v){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from status_passageiro where viagem_trip_id=?";
                    
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
    
    public boolean excluirDoPassageiro(Usuario usr){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from status_passageiro where passageiro=?";
                    
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setInt(1, usr.getCodUsuario());
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
    
    public boolean excluir(StatusPassageiro sp){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "delete from status_passageiro where passageiro=? and viagem_trip_id=?";
                    
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setInt(1, sp.getPassageiro().getCodUsuario());
            stmt.setInt(2, sp.getViagem());
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
    
    public boolean selecionaEmbarcou(StatusPassageiro sp){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update status_passageiro set status=1 where passenger_trip_id=?";
                    
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setInt(1, sp.getIdStatusPassageiro());
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
    
    public boolean selecionaAusente(StatusPassageiro sp){
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            String sql = "update status_passageiro set status=2 where passenger_trip_id=?";
                    
            //preparar o sql para ser executado pelo preparedStatement
            // preparar -> deixar apto para substituir os ?
            stmt = con.prepareStatement(sql);
            //substituir os ?
            stmt.setInt(1, sp.getIdStatusPassageiro());
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
    
    public ArrayList<StatusPassageiro> getListaSp(int codViagem) {
        ArrayList<StatusPassageiro> listasp = new ArrayList<StatusPassageiro>();
        
        String query = "select * from status_passageiro where viagem_trip_id = ?";

        try {
            // cria o objeto para rodar o SQL
            PreparedStatement stmt = con.prepareStatement(query);
            // passando a string SQL que faz o SELECT
            stmt.setInt(1, codViagem);
            
            try (ResultSet res = stmt.executeQuery()) {
                while (res.next()) {
            StatusPassageiro sp;

            // Extração de dados comuns
            int id = res.getInt("passenger_trip_id");
            int status = res.getInt("status");
            java.sql.Timestamp hora = res.getTimestamp("hora_atualizacao");
            int viagem_trip_id = res.getInt("viagem_trip_id");
            Passageiro passageiro = new Passageiro(res.getInt("passageiro"));

            // Construção baseada no tipo
           
            sp = new StatusPassageiro(id, viagem_trip_id, passageiro, status, hora);
            

                // adicionando na lista auxiliar
                listasp.add(sp);
            }
            // Pebkorrendo o resultado - res
            res.close();// fechando o resultado
            }
            
            stmt.close();// fechando statment
            con.close(); // fechando conexão com o banco
            return listasp; // retornando a lista de gastomensals
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
        
    }
}


