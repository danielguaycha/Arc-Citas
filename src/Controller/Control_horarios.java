
package Controller;

import Model.Config;
import Model.DbConexion;
import Model.FormatoTabla;
import View.panels.p_horarios;
import static View.panels.p_horarios.h_dr;
import static View.panels.p_horarios.h_id;
import static View.panels.p_horarios.h_slideHora;
import static View.panels.p_horarios.h_slide_min;
import static View.panels.p_horarios.h_txtDuracion;
import static View.panels.p_horarios.h_txtHoraIni;
import static View.panels.p_horarios.h_txtbusqueda;
import static View.panels.p_horarios.h_txthoraFin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel
 */
public class Control_horarios {
    String sql;
    DbConexion db;
    Connection con;
    Config conf;
    int id_horario;
    Control_doctores cd;
    
    public Control_horarios() {
        cd = new Control_doctores();
        db = new DbConexion();
      
        conf = new Config();
        con =  db.conectar();
        cargar_horarios("");
    }
    
      
    /*
    |                                                   |
    |  Metodos publicos -> accesibles de las vistas      |
    |                                                   |
    */
    
    
    
    
    /**
     * Antes de modificar o agregar valida los datos
     * @param id_doc
     * @param hora_in
     * @param hora_out
     * @param duracion
     * @param operacion -> (1. Agrega, 2. Modifica)
     * @return String mensaje (error - correcto)
     */
    public String validar_datos(int id_doc, String hora_in, String hora_out, String duracion, int operacion) {
        String msg="Error;Error al procesar la informacion";
        int h_in = _horas_to_min(hora_in);
        int h_out = _horas_to_min(hora_out);
        int dur = _horas_to_min(duracion);
        String descanso="0";
        if(h_in>h_out){
            return "Alert;La hora de entrada es mayor a la hora de salida";
        }
        else if((h_out-h_in)<dur){
            return "Alert;La duracion de mayor que el horario establecido";
        }else if(id_doc==0){
            return "Alert;Necesitas escojer un doctor";
        }
        else{
            // si la hora de entrada es mayor igual al descanso predeterminado
            //no se aplica descanso        
            if(h_in>=_horas_to_min(conf.h_descanso_ini)){descanso="1";}
            switch(operacion){
                //.-Caso 1: Registra el Horario
                case 1:
                     msg =  _guardar_horario(id_doc,hora_in,hora_out,duracion,descanso);
                    break;
                //.-Caso 2: Hace un Update al horario
                case 2:
                    msg = _actulizar_horario(id_doc, hora_in, hora_out, duracion, descanso);
                    break;
            }
           
        }
        return msg;
    }
    
    
    
   /**
    * Da de baja horario segun la fila tomada desde (tbHorario->p_horarios)
    * @param row
     * @param status
    * @return String mensaje
    */
    public String active_and_desactive_horario(int row, String status){
        int id  = Integer.parseInt(p_horarios.h_tbHorario.getValueAt(row, 0).toString());
        sql ="UPDATE `horarios` SET `disponible`="+status+" WHERE `id_horario`="+id;
        int rs = db.eliminar(sql);
        if(rs>0){
            cargar_horarios("");
            return "Ok;Peticion procesada correctamente!";
        }else{
            return "Error;Error al procesar datos.";
        }
    }
    
       
    /**
     * FUNCION INTERFAZ
     * Llena los campos en p_horarios de acuerda a la seleccion
     * @param row 
     */
    public void cargarDatos_edicion(int row){
        p_horarios.Panel_Horario.setSelectedIndex(0);
        _activar_controles(true);
                
        id_horario = Integer.parseInt(p_horarios.h_tbHorario.getValueAt(row, 0).toString());
        int id = cd.get_id_by_cedula(p_horarios.h_tbHorario.getValueAt(row, 1).toString());
        String n = p_horarios.h_tbHorario.getValueAt(row, 2).toString();
        
        p_horarios.h_txtHoraIni.setText(p_horarios.h_tbHorario.getValueAt(row, 3).toString());
        p_horarios.h_txthoraFin.setText(p_horarios.h_tbHorario.getValueAt(row, 4).toString());
        p_horarios.h_txtDuracion.setText(p_horarios.h_tbHorario.getValueAt(row, 5).toString());
        p_horarios.h_id.setText(id+"");
        p_horarios.h_dr.setText(n);
        p_horarios.h_txtbusqueda.setEnabled(false);
        p_horarios.h_txtHoraIni.requestFocus();
        p_horarios.h_btnActualizar.setEnabled(true);
        p_horarios.h_btnGuardar.setEnabled(false);
    }
    
    
    
    /**
     * Carga los horarios con disponibilidad en la base de datos
     * @param cedula -> doctor
     */
    public void cargar_horarios(String cedula){
   
        DefaultTableModel modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };           
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
        String []dato = new String[7];    
            modelo.addColumn("#");
            modelo.addColumn("CEDULA");
            modelo.addColumn("DOCTOR");
            modelo.addColumn("H.INICIO");
            modelo.addColumn("H.SALIDA");
            modelo.addColumn("DURACION");
            modelo.addColumn("ESTADO");
            if(cedula.equals("")){
                sql ="SELECT personas.id_personas, personas.cedula ,"
                + "CONCAT(personas.nombres,' ',personas.apellidos) as nombre,"
                + "horarios.* FROM personas JOIN horarios "
                + "ON personas.id_personas=horarios.id_doctor "
                + "order by horarios.id_horario asc limit 0,20 ";
            }else{
                sql="SELECT personas.id_personas, personas.cedula, "
                + "CONCAT( personas.nombres,  ' ', personas.apellidos ) AS nombre, "
                + "horarios . * FROM personas JOIN horarios ON ( personas.cedula = '"+cedula+"' "
                + "AND personas.id_personas = horarios.id_doctor ) "
                + "ORDER BY horarios.id_horario ASC LIMIT 0 , 20";
            }

            ResultSet rs = db.consulta(sql);
            try {
                while(rs.next()){
                    dato[0]=""+rs.getInt("id_horario");
                    dato[1]=rs.getString("cedula");
                    dato[2]=rs.getString("nombre");
                    dato[3]=sdf.format(rs.getTimestamp("hora_inicio"));
                    dato[4]=sdf.format(rs.getTimestamp("hora_fin"));
                    dato[5]=sdf.format(rs.getTimestamp("duracion_consulta"));
                    dato[6]=(rs.getString("disponible").equals("1"))?"Activo":"Inactivo";
                    modelo.addRow(dato);
                    
                }
                p_horarios.h_tbHorario.setModel(modelo);
                FormatoTabla ft = new FormatoTabla("Activo","Inactivo");
                p_horarios.h_tbHorario.setDefaultRenderer (Object.class, ft );
               
            } catch (SQLException ex) {
                Logger.getLogger(Control_horarios.class.getName()).log(Level.SEVERE, null, ex);
            }
 
    }
    
    
    
    
     /**
     * Si el horario ya esta configurado no se podra agregar de nuevo
     * debe ser editado o eliminado | en caso contrario cargara los datos
     * del doctor
     * @param cedula
     * @return mensaje
     */
    public String validar_horario(String cedula){
        int val = _comprobar_horario(cedula);
       
       if(val<0){
          return ("Alert;No se ha encontrado doctor : ");
       }else if(val>0){
           return("Alert;Horario ya configurado");
       }else{
           _cargar_datos_doctor(cedula);
       }
       return "";
    }
    
    
    
    /*
    |                                                       |
    |  Metodos privados -> no accesibles desde las vistas   |
    |                                                       |
    */
    
    
    
     
    /**
     * Permite cargar datos automaticamente luego de ingresar la cedula
     * @param cedula 
     */
    private String _cargar_datos_doctor(String cedula){
        sql="SELECT `nombres`,`apellidos`,`id_personas` FROM `personas` WHERE `status`=1 AND `tipo`='D' AND `cedula`='"+cedula+"'";
        ResultSet rs = db.consulta(sql);
        String nombres="";
        int id=-1;
        try {
            while(rs.next()){
                id = rs.getInt("id_personas");
                nombres=rs.getString("nombres")+" "+rs.getString("apellidos");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_horarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        p_horarios.h_dr.setText(nombres);
        p_horarios.h_id.setText(id+"");
        _activar_controles(true);
        p_horarios.h_btnGuardar.setEnabled(true);
        return "Ok;Datos cargados con exito";
    }
    
    
    
    /**
     * Guarda el horario despues de pasar por la validacion
     * @param id_doc
     * @param hora_in
     * @param hora_out
     * @param duracion
     * @param descanso -> Establece si toma el descanso ejemplo 12h-13h
     *                    es valido para no asignar turnos en ese intervalo
     *                    valores [0]Toma descanso [1]No toma descanso 
     * @return mesaje
     */
    private String _guardar_horario(int id_doc, String hora_in, String hora_out, String duracion, String descanso) {
      String msg="";
        sql="INSERT INTO `horarios`(`duracion_consulta`, `hora_inicio`, `hora_fin`, `descanso`, `disponible`, `id_doctor`,`id_sync`) "
                + "VALUES ('"+_parseToDateTime(duracion)+"','"+_parseToDateTime(hora_in)+"',"
                + "'"+_parseToDateTime(hora_out)+"','"+descanso+"','1',"+id_doc+",'"+funciones.id_sync()+"')";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            int n2=pst.executeUpdate();
            if(n2>0)
            {
              msg ="Ok;Registro procesado con exito!";
            }else{
              msg="Error;Error al Registrar ";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpiarCampos();
        return msg;
    }
    
    
    
    /**
     * Actualiza los datos del horario despues de pasar por la validacion
     * @param id_doc requerido
     * @param hora_in
     * @param hora_out
     * @param duracion
     * @param descanso
     * @return 
     */
    private String _actulizar_horario(int id_doc, String hora_in, String hora_out, String duracion, String descanso){
        sql="UPDATE `horarios` SET `duracion_consulta`='"+_parseToDateTime(duracion)+
                "',`hora_inicio`='"+_parseToDateTime(hora_in)+"',`hora_fin`='"+_parseToDateTime(hora_out)+""
                + "',`descanso`='"+descanso+"' WHERE `id_horario`="+id_horario;
        int rs = db.actualizarRegistro(sql);
        if(rs>0){
            limpiarCampos();
            _activar_controles(false);
            h_txtbusqueda.setEnabled(true);
            p_horarios.h_btnActualizar.setEnabled(false);
            return "Ok;Registro Actualizado con exito";
            
        }
        else
            return "Error;Error al Actualizar Registro";
    }
    
    
    
    /**
     * 
     * Permite llenar la bd de sugerencias y cargarlas en un JtextField
     * FUNCION DE "AUTOCOMPLETADO"
     */
     
    
    
    /**
     * Funcion de comprobacion, permite saber si existe un horario para el doctor
     * @param cedula -> doctor
     * @return 
     */
    private int _comprobar_horario(String cedula){
        int id_doc = cd.get_id_by_cedula(cedula);
        if(id_doc==-1)
        {
            return -1;
        }
        else{
            int id=0;

            String sql2 ="SELECT `id_horario` FROM `horarios` WHERE `id_doctor`="+id_doc;
            ResultSet rs2=db.consulta(sql2);
            try {
                while(rs2.next()){
                   id = rs2.getInt("id_horario");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(id==0){
                return 0;
            }else
                return id;
        }
    }
    
   
    
    /**
     * FUNCION INTERFAZ, Bloquea y desbloquea controles en p_horarios.
     * @param valor 
     */
    private void _activar_controles(boolean valor){
        p_horarios.h_txtHoraIni.setEnabled(valor);
        p_horarios.h_txthoraFin.setEnabled(valor);
        p_horarios.h_txtDuracion.setEnabled(valor);
        p_horarios.h_slideHora.setEnabled(valor);
        p_horarios.h_slide_min.setEnabled(valor);
    }

    
    
    /**
     * FUNCION FECHA-HORA: pasa las horas formato HH:mm:ss a (INT)minutos
     * @param hora
     * @return int hora => Entero equivalentes
     */
    private int _horas_to_min(String hora){
        String []v= hora.split(":");
        int h = Integer.parseInt(v[0]);
        int min= Integer.parseInt(v[1]);
        return (h*60)+min; 
    }
    
    
    
    /**
     * FUNCION FECHA-HORA: convierte HH:mm:ss a yyyy-MM-dd HH:mm:ss
     * @param hora
     * @return yyyy-MM-dd HH:mm:ss
     */
    private String _parseToDateTime(String hora){
       Date d = new Date();
       String []val = hora.split(":");
       if(val[0].length()==1){
           //String aux = val[0];
           val[0]="0"+val[0];
       }
       SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
      return sd.format(d)+" "+val[0]+":"+val[1]+":"+"00";
   } 
    
    
    /**
     * FUNCION INTERFAZ
     */
    public void limpiarCampos(){
        h_txtbusqueda.setText("");
        h_dr.setText("");
        h_id.setText("");
        h_txtHoraIni.setText("8:00:00");
        h_txthoraFin.setText("16:00:00");
        h_txtDuracion.setText("00:00:30");
        h_slideHora.setValue(0);
        h_slide_min.setValue(0);
    }
    
    
}
