
package Controller;

import Model.Config;
import Model.DbConexion;
import View.home;
import View.index;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Control_login {
    
    DbConexion db = new DbConexion();
    public static String nombre;
    public static int id_user;
    Config cf = new Config();
    public int login(String user, String pass) {
        
        if(cf.userRoot.equals(user) && cf.passUserRoot.equals(pass)){
            nombre ="Administrador";
            id_user=0;
            home h = new home();
            h.setVisible(true);
            return 1;
        }else{
            String sql ="SELECT concat(`nombres`,' ',`apellidos`)as nombre,`id_personas` "
                  + "FROM `personas` WHERE `cedula`='"+user+"' and `password`='"+pass+"' and `status`=1";
            ResultSet rs = db.consulta(sql);
            try {
                while(rs.next()){
                    nombre = rs.getString("nombre");
                    id_user = rs.getInt("id_personas");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Control_login.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(id_user>0){
                limitar_controles();
                return 1;
            }else{
                return 0;
            }
        }
    }
    
    public void limitar_controles(){
        home h = new home();
        h.lblDoctores.setVisible(false);
        h.lblPacientes.setVisible(false);
        h.lblReservacion.setVisible(false);
        h.lblHorarios.setVisible(false);
        h.lblConfig.setVisible(false);
        h.Separator.setVisible(false);
        h.setVisible(true);
    }
    
}
