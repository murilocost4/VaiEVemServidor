package model;

import factory.Conector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelDominio.Admin;
import modelDominio.Condutor;
import modelDominio.StatusPassageiro;
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
    }
    
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
}


