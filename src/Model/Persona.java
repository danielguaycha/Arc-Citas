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
public class Persona {
    private int id;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String email;
    private String direccion;
    private String estado_civl;
    private String celular;
    private Date fecha_nacimiento;
    private int edad;
    private double peso;
    private String tipo_sangre;
    private String discapacidad;
    private double Talla;
    private String password;
    public Persona() {
    }

    public Persona(int id, String nombres) {
        this.id = id;
        this.nombres = nombres;
    }

    public Persona(String cedula, String nombres, String apellidos, String email, String direccion, String estado_civl, String celular, String password) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.direccion = direccion;
        this.estado_civl = estado_civl;
        this.celular = celular;
        this.password=password;
    }
    
    public Persona(String cedula, String nombres, String apellidos, String email, String direccion, String estado_civl, String celular, Date fecha_nacimiento, int edad, double peso, String tipo_sangre, String discapacidad, double Talla, String password) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.direccion = direccion;
        this.estado_civl = estado_civl;
        this.celular = celular;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad = edad;
        this.peso = peso;
        this.tipo_sangre = tipo_sangre;
        this.discapacidad = discapacidad;
        this.Talla = Talla;
        this.password=password;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado_civl() {
        return estado_civl;
    }

    public void setEstado_civl(String estado_civl) {
        this.estado_civl = estado_civl;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getTipo_sangre() {
        return tipo_sangre;
    }

    public void setTipo_sangre(String tipo_sangre) {
        this.tipo_sangre = tipo_sangre;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public double getTalla() {
        return Talla;
    }

    public void setTalla(double Talla) {
        this.Talla = Talla;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
