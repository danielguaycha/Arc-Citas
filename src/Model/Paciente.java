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
public class Paciente extends Persona {
    private String tipo;

    public Paciente() {
        this.tipo ="P";
    }

    public Paciente(String cedula, String nombres, String apellidos, String email, String direccion, String estado_civl, String celular, String password) {
        super(cedula, nombres, apellidos, email, direccion, estado_civl, celular, password);
        this.setFecha_nacimiento(new java.sql.Date(new Date().getTime()));
        this.setTipo_sangre("");
        this.setDiscapacidad("");
        this.setTalla(0.0);
        this.setPeso(0.0);
        this.setTipo("P");
    }
    
    public Paciente(String cedula, String nombres, String apellidos, String email, String direccion, String estado_civl, String celular, Date fecha_nacimiento, int edad, double peso, String tipo_sangre, String discapacidad, double Talla,String password) {
        super(cedula, nombres, apellidos, email, direccion, estado_civl, celular, fecha_nacimiento, edad, peso, tipo_sangre, discapacidad, Talla, password);
        this.tipo = "P";
    }
    
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
