/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

public class ListaCita {
    private String hora;
    private boolean status;
    private int numero_cita;
    public ListaCita() {
        this.status=true;
    }

     public ListaCita(String hora, int numero) {
        this.hora = hora;
        this.numero_cita = numero;
        this.status = true;
    }
     
    public ListaCita(String hora) {
        this.hora = hora;
        this.status = true;
    }

    public int getNumero_cita() {
        return numero_cita;
    }

    public void setNumero_cita(int numero_cita) {
        this.numero_cita = numero_cita;
    }
    
    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}
