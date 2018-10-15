/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DbConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Control_especialidades {
    DbConexion db;

    public Control_especialidades() {
        db = new DbConexion();
    }
  
    public String get_especialidad(int id){
      String name = "";
      String sql = "SELECT `nombre_esp` FROM `especializacion` WHERE `id_especializacion` = "+id+" ";
       ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               name =  rs.getString("nombre_esp");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
      return name;
    }
    
    public int get_id_by_nombre(String nombre){
      int id = 0;
      String sql = "SELECT `id_especializacion` FROM `especializacion` WHERE `nombre_esp`='"+nombre+"'";
       ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               id =  rs.getInt("id_especializacion");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
      return id;
    }
  
}
