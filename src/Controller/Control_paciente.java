
package Controller;

import Model.DbConexion;
import Model.Paciente;
import View.panels.p_paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class Control_paciente {
    
    public DbConexion db;
    Connection con;
    String sql;
    int init = 1;// numero desde el que inician las historias
    
    public Control_paciente() {
      db= new DbConexion();
      con =  db.conectar();
    }
    
    
    /*
    |                                                     |
    |  Metodos publicos -> accsibles desde las vistas     |
    |                                                     |
    */
    
    
    
    /**
     * Valida datos antes de la insersion o modificacion
     * @param p
     * @param operacion
     * @return 
     */
    public String validar_datos(Paciente p, int operacion){
       String msg ="";
       if(p.getNombres().equals("") || p.getApellidos().equals("") || p.getCedula().equals("")
               || p.getCelular().equals("")){
           return "Alert;Existen campos obligatorios";
       }
       else if(!funciones.validadorDeCedula(p.getCedula())){
           msg="Alert;Cedula incorrecta! Verifique";
       }
       else if (!p.getEmail().matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$")){
           msg="Alert;Correo Electronico no valido";
       }
       else
       {
           switch(operacion){
               case 1:
                   if(!exist(p.getCedula()))
                       msg = _new_paciente(p);
                   else msg ="Alert;Este paciente ya se encuentra registrado";
                   break;
               case 2:
                   msg = _edit_paciente(p);
                   break;
           }
       }
       
        return msg;
    }
    
    
    
    /**
     * Elimina Paciente dependiendo del id_paciente
     * @param p
     * @return 
     */
    public String delete_paciente(Paciente p){
        String msg ="";
        sql =  "UPDATE personas, paciente"+
        " SET personas.status = 0 " +
        "WHERE (personas.id_personas = "+p.getId()+" AND paciente.id_paciente = personas.id_personas)";
         int rs = db.eliminar(sql);
        if(rs>=1)
            msg="Ok;Se elimino el registro corrrectamente!";
        else 
            msg="Error;No se pudo eliminar los datos, intentelo mas tarde";
        limpiarCampos_edicion();
        return msg;
    }
    
    
    
    /**
     * Funcion Getter
     * @param cedula
     * @return id_personas as int
     */
    public int get_id_by_cedula(String cedula){
        int id = -1;
        try {
            sql ="SELECT `id_personas` FROM `personas` WHERE `cedula` ='"+cedula+"' and `status`=1 and `tipo`='P'";
            ResultSet rs = db.consulta(sql);
            while(rs.next()){
                id = rs.getInt("id_personas");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_paciente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    
    
    /**
     * Obtiene el email segun el id_paciente
     * @param id_paciente
     * @return 
     */
    public String get_email_by_id(int id_paciente){
        sql ="SELECT `email` FROM `personas` WHERE `id_personas`= "+id_paciente+" AND `tipo`='P' AND `status`=1";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                return rs.getString("email");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_paciente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    
    
    public String get_name_by_id(int id_paciente){
        sql ="SELECT CONCAT(nombres,' ',apellidos) as nombre FROM `personas` WHERE `id_personas`= "+id_paciente+" AND `tipo`='P' AND `status`=1";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                return rs.getString("nombre");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_paciente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    /**
     * FUNCION DE COMPRABACION: comprueba si el paciente ya esta registrado
     * @param cedula
     * @return true or false
     */
    public boolean exist(String cedula){
        sql ="SELECT `cedula` FROM `personas` WHERE `status`=1 and `tipo`='P'";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                if(cedula.equals(rs.getString("cedula"))){
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_paciente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
   
    
    
    public void limpiarCampos_edicion(){
            p_paciente.ID.setText("");
            p_paciente.p_editNombre.setText("");
            p_paciente.p_editCed.setText("");
            p_paciente.p_editApe.setText("");
            p_paciente.p_editEmail.setText("");
            p_paciente.p_editDir.setText("");
            p_paciente.p_editSelec_est_civ.setSelectedItem(0);
            p_paciente.p_editCel.setText("");
            p_paciente.p_editDateNaci.setDate(null);
            p_paciente.p_editEdad.setText("0");
            p_paciente.p_editPeso.setText("00.00");
            p_paciente.p_editSangre.setText("");
            p_paciente.p_editDiscap.setText("Ninguna");
            p_paciente.p_editTalla.setText("00.00");   
            p_paciente.p_btnActualizar.setEnabled(false);
            p_paciente.p_btnEliminar.setEnabled(false);
            p_paciente.p_editPass.setText("");
    }
    
     /*
    |                                                     |
    |  Metodos publicos -> accsibles desde las vistas     |
    |                                                     |
    */
    
    
    
    /**
     * FUNCION DE REGISTRO
     * @param p
     * @return mensaje
     */
    private String _new_paciente(Paciente p){
        String msg ="";
        String sql_persona ="INSERT INTO `personas`( `cedula`, `nombres`, "
                + "         `apellidos`, `email`, `celular`, `direccion`, "
                + "         `estado_civil`, `fecha_nace`, `peso`, "
                + "         `sangre`, `discapacidad`, `talla`, `tipo`,`id_sync`,`password`) VALUES("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String sqlpaciente = "INSERT INTO `arc`.`paciente` (`id_paciente`,`avatar`) VALUES (?,?)";
    
         try {
           
            PreparedStatement pst = con.prepareStatement(sql_persona);
            pst.setString(1, p.getCedula());
            pst.setString(2, p.getNombres());
            pst.setString(3, p.getApellidos());
            pst.setString(4, p.getEmail());
            pst.setString(5, p.getCelular());
            pst.setString(6, p.getDireccion());             
            pst.setString(7, p.getEstado_civl());
            pst.setDate(8, new java.sql.Date(p.getFecha_nacimiento().getTime()));
            pst.setDouble(9, p.getPeso());
            pst.setString(10, p.getTipo_sangre());
            pst.setString(11, p.getDiscapacidad());
            pst.setDouble(12, p.getTalla());
            pst.setString(13, p.getTipo());         
            pst.setString(14, funciones.id_sync()); 
            pst.setString(15, p.getPassword());    
            int n=pst.executeUpdate();
            if(n>0){
                PreparedStatement pst2 = con.prepareStatement(sqlpaciente);
                pst2.setInt(1, get_id_by_cedula(p.getCedula()));
                pst2.setString(2, "");
                if(pst2.executeUpdate()>0){
                    msg="Ok;Registro realizado con exito";
                }
                else{
                    msg="Error PTDB0001, No se completo el registro";
                }
            }else{
                msg="Error;Error PTDB0002, no se guardaron los datos";
            }
         }catch(SQLException ex){
             JOptionPane.showMessageDialog(null, "Error en el sistema, comuniquese con el administrador del sistema: "+ex.getMessage());
              Logger.getLogger(p_paciente.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        return msg;
    }
    
    
    
    /**
     * FUNCION EDICION
     * @param p
     * @return mensaje
     */
    private String _edit_paciente(Paciente p){
        String msg;
        sql =""
                + "UPDATE personas" +
                " SET nombres = '"+p.getNombres()+"', apellidos = '"+p.getApellidos()+"',"
                + "cedula='"+p.getCedula()+"', email= '"+p.getEmail()+"', celular='"+p.getCelular()+"', "
                + "direccion='"+p.getDireccion()+"', estado_civil='"+p.getEstado_civl()+"', fecha_nace= '"+new java.sql.Date(p.getFecha_nacimiento().getTime())+"',"
                + "peso="+p.getPeso()+" , sangre= '"+p.getTipo_sangre()+"', discapacidad='"+p.getDiscapacidad()+"',"
                + "talla="+p.getTalla()+", password ='"+p.getPassword()+"' WHERE id_personas = "+p.getId()+"";
        int rs = db.actualizarRegistro(sql);
        if(rs==1)
            msg="Ok;La actualizacion se realizo exitosamente";
        else
            
            msg="Error;Ocurrio un error al actualizar datos comuniquese con el administrador.";
        limpiarCampos_edicion();
        return msg;
    }
    
    
    
    /**
     * GENERA UN NUMERO DE HISTORIA CLINICO
     * @return String historia
     */
    private String _histotial_number(){
        int count=-1;
        String aux ="";
        sql = "SELECT count(*) as number FROM paciente";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                count = rs.getInt("number");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_paciente.class.getName()).log(Level.SEVERE, null, ex);
        }
        String cad = String.valueOf(this.init);
        for (int i = 0; i < 4-cad.length(); i++) {
            aux +="0";
        }
        
        return aux+(count+this.init);
    }
    
    

    
    
}
