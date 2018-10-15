/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 *
 * @author Daniel
 */
public class Config {
    public String h_descanso_ini;
    public String h_descanso_fin;
    public static String email;
    public static String passEmail;
    public static String nombre_hostpital;
    public static String telefono;
    public static String direccion;
    public String userRoot;
    public String passUserRoot;
    public Config(){
       h_descanso_ini="12:00:00";
       h_descanso_fin="14:00:00";
       email ="danielguaycha@gmail.com";
       passEmail = "dg851995";
       nombre_hostpital="Hospital Abc";
       telefono ="0994957362 - 07034234";
       direccion ="Calle 9 de octubre entre Paez y Rocafuerte";
       userRoot ="admin";
       passUserRoot="admin";
    }
    
}
