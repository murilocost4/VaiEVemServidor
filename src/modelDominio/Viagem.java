/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDominio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author murilocost4
 */
public class Viagem implements Serializable{

    private static final long serialVersionUID = 123456789L;
    
    private int trip_id;
    private String origem;
    private String destino;
    private Date data; 
    private LocalTime saida;
    private LocalTime retorno;
    private int status_viagem;
    
    // usado por selects e updates.
    public Viagem(int trip_id, String origem, String destino, Date data, LocalTime saida, LocalTime retorno, int status_viagem) {
        this.trip_id = trip_id;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.saida = saida;
        this.retorno = retorno;
        this.status_viagem = status_viagem;
    }

 
    // INSERTS
    public Viagem(String origem, String destino, Date data, LocalTime saida, LocalTime retorno, int status_viagem) {
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.saida = saida;
        this.retorno = retorno;
        this.status_viagem = status_viagem;
    }
    
    
    // usado para DELETE
    public Viagem(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
    }

    public LocalTime getRetorno() {
        return retorno;
    }

    public void setRetorno(LocalTime retorno) {
        this.retorno = retorno;
    }

    public int getStatus_viagem() {
        return status_viagem;
    }

    public void setStatus_viagem(int status_viagem) {
        this.status_viagem = status_viagem;
    }
    
    @Override
    public String toString() {
        return "Viagem{" + "trip_id=" + trip_id + ", origem=" + origem +  ", destino=" + destino + '}';
    }
}
