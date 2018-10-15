/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Cita {
    private int id_cita;
    private int id_doctor;
    private int id_especializacion;
    private Date fecha_cita;
    private String hora;
    private String costo;
    private int id_paciente;

    public Cita() {
    }

    public Cita(int id_doctor, int id_especializacion, Date fecha_cita, String hora, String costo, int id_paciente) {
        this.id_doctor = id_doctor;
        this.id_especializacion = id_especializacion;
        this.fecha_cita = fecha_cita;
        this.hora = hora;
        this.costo = costo;
        this.id_paciente = id_paciente;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public int getId_especializacion() {
        return id_especializacion;
    }

    public void setId_especializacion(int id_especializacion) {
        this.id_especializacion = id_especializacion;
    }

    public Date getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(Date fecha_cita) {
        this.fecha_cita = fecha_cita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }
    
    
    
}
