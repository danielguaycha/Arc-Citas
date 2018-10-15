
package Controller;

import Model.DbConexion;
import View.panels.p_today;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel
 */
public class Control_filtrado {
    DbConexion db;
    DefaultTableModel modelo;
    Control_doctores cd;
    Control_paciente cp;
    String sql;
    String []datos;
    public Control_filtrado() {
        db = new DbConexion();
        cd = new Control_doctores();
        cp = new Control_paciente();
        
    }
    
    public void filtro_by_paciente(){
        System.out.println("dasda: "+Control_login.id_user);
    modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Paciente");
        modelo.addColumn("Doctor");
        modelo.addColumn("Fecha/Hora");
        modelo.addColumn("Estado");
        sql="SELECT A.fecha_cita, A.status, (Select concat(nombres,' ',apellidos) from personas where id_personas = A.id_paciente)as paciente, (select concat(nombres,' ',apellidos) from personas where id_personas =A.id_doctor)as doctor\n" +
        "FROM citas A, personas B Where (A.status=B.status and A.id_doctor = B.id_personas and  A.fecha_cita=NOW()) order by B.apellidos limit 0,15";
        ResultSet rs =db.consulta(sql);
        try {
            while(rs.next()){
                
                datos[0]=rs.getString("paciente");
                datos[1]=rs.getString("doctor");
                datos[2]=rs.getString("fecha_cita");
                datos[3]=rs.getString("status");
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_filtrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        p_today.t_tbFiltrado.setModel(modelo);
    }
    
    public void filtro_by_doctor(){
        modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Doctor");
        modelo.addColumn("Paciente");
        modelo.addColumn("Fecha/Hora");
        modelo.addColumn("Estado");
        sql="SELECT A.fecha_cita, A.status, (Select concat(nombres,' ',apellidos) from personas where id_personas = A.id_paciente)as paciente, (select concat(nombres,' ',apellidos) from personas where id_personas =A.id_doctor)as doctor\n" +
        "FROM citas A, personas B Where (A.status=B.status and A.id_doctor = B.id_personas and  A.fecha_cita=NOW()) order by B.apellidos limit 0,15";
        ResultSet rs =db.consulta(sql);
        try {
            while(rs.next()){
                
                datos[0]=rs.getString("doctor");
                datos[1]=rs.getString("paciente");
                datos[2]=rs.getString("fecha_cita");
                datos[3]=rs.getString("status");
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_filtrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        p_today.t_tbFiltrado.setModel(modelo);
    
    }
    
    public void filtro_by_horarios(){
        modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Fecha/hora");
        modelo.addColumn("Doctor");
        modelo.addColumn("Paciente");
        modelo.addColumn("Estado");
        datos = new String [5];
        sql="SELECT A.fecha_cita, A.status, (Select concat(nombres,' ',apellidos) from personas where id_personas = A.id_paciente)as paciente, "
        + "(select concat(nombres,' ',apellidos) from personas where id_personas =A.id_doctor)as doctor\n" +
        "FROM citas A, personas B Where (A.status=B.status and A.id_doctor = B.id_personas and A.fecha_cita=NOW()) order by A.fecha_cita limit 0,15";
        
        ResultSet rs =db.consulta(sql);
        try {
            while(rs.next()){
                datos[0]=rs.getString("fecha_cita");
                datos[1]=rs.getString("doctor");
                datos[2]=rs.getString("paciente");
                datos[3]=rs.getString("status");
                modelo.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_filtrado.class.getName()).log(Level.SEVERE, null, ex);
        }
        p_today.t_tbFiltrado.setModel(modelo);
    }
    
  
}
