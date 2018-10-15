
package Controller;

import Model.DbConexion;
import Model.Doctor;
import View.panels.p_doctor;
import static View.panels.p_doctor.d_btnActual;
import static View.panels.p_doctor.d_btnEliminar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Youtteam
 * @Date: 13/2/2015
 * @version 1.0
 */
public class Control_doctores {
    
    public DbConexion db;
    Connection con;
    
    public Control_doctores() {
      db= new DbConexion();
      con =  db.conectar();
    }
    
    
    
    /*
    |                                                     |
    |  Metodos publicos -> accsibles desde las vistas     |
    |                                                     |
    */
    
    
    
    /**
     * Valida todos los datos antes de ser insetados o modificados
     * @param d -> Nombre variable Tipo Doctor
     * @param operacion -> 1 (Nuevo Doctor) 2 (Modificar Doctor)
     * @return String -> Mensaje de Validaciones o de operacion exitosa
     */
           
    public String validar_datos(Doctor d, int operacion){
        String msg = "";
        if(d.getCedula().equals("")){
            msg ="Alert;El campo cedula esta vacio";
        }
        else if(d.getNombres().equals("")){
            msg ="Alert;El campo nombre esta vacio";
        }
        else if(d.getApellidos().equals("")){
            msg ="Alert;El campo apellido esta vacio";
        }
        else if(d.getCelular().equals("")){
            msg ="Alert;El campo celular esta vacio";
        }
        else if(d.getTitulo().equals("")){
            msg ="Alert;El campo titulo esta vacio";
        }
        else if(d.getNombres().equals("") || d.getApellidos().equals("") || d.getCedula().equals("") || d.getCelular().equals("")
           || d.getTitulo().equals(""))
        {
            msg ="Alert;Existen datos obligatorios!";
        }else if(!funciones.validadorDeCedula(d.getCedula())){
            msg="Alert;Cedula incorrecta! Verifique";
        }
        else if(d.getEspecialidad()<=0){
            msg ="Alert;Necesita seleccionar una especialidad";
        }
        else if (!d.getEmail().matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$")){
            msg="Alert;Correo Electronico no valido";
        }
        else
        {
            switch(operacion){
                case 1:
                   msg = _new_doctor(d); 
                break;
                case 2:
                    msg = _modificar_doctor(d);
                break;
            }
            
        }
        return msg;
    }

    
    
    /**
     * Persmite eliminar el Doctor -> d  pasado por parametro
     * Requiere datos como ( id_doctor )
     * @param d -> Nombre variable Tipo Doctor
     * @return String -> mensaje de error o eliminacion correcta
     */
    public String eliminar_doctor(Doctor d){
        String msg;
        String sql ="UPDATE personas, doctor"+
        " SET personas.status = 0" +
        "WHERE (personas.id_personas = "+d.getId()+" AND doctor.id_doctor = personas.id_personas)";
        int rs = db.eliminar(sql);
        if(rs>=1)
            msg="Ok,Se elimino el registro corrrectamente!";
        else 
            msg="Error,No se puedo eliminar los datos";
        return msg;
    }
    
    
    
    /**
     * 
     * @param cedula
     * @return id_persona
     */
    public int get_id_by_cedula(String cedula){
    int id_persona = -1;
            ResultSet rs =db.consulta("SELECT `id_personas` FROM `personas` WHERE `cedula`='"+cedula+"' AND status =1");
       try {
           while(rs.next())
            id_persona = rs.getInt("id_personas");
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id_persona;
    }
    
    
    
    /**
     * Retorna id_especializacion segun id_doctor
     * @param id_doctor
     * @return id_especializacion
     */
    public int get_id_especialidad(int id_doctor){
        int espc = 0;
        String sql = "SELECT `id_especializacion` FROM `doctor` WHERE `id_doctor`="+id_doctor+"";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               espc =  rs.getInt("id_especializacion");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return espc;
    }
    
    
    
    /**
     * Nombre completo segun doctor
     * @param id_doctor
     * @return Nombres+Apellidos
     */
    public String get_name_by_id(int id_doctor){
        String name="";
        String ape="";
        String sql = "SELECT `nombres`, `apellidos` FROM `personas` WHERE `id_personas`="+id_doctor+" and status =1";
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               name =  rs.getString("nombres");
               ape = rs.getString("apellidos");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name+" "+ape;
    }
    
    
    
    /**
     * Nombre especialidad segun el doctor
     * @param id_doctor
     * @return nombre_espcialidad
     */
    public String get_especialidad(int id_doctor){
       Control_especialidades ce = new Control_especialidades();
       String esp  = "";
       int id_especializacion=-1;
       String sql = "SELECT `id_especializacion` FROM `doctor` WHERE `id_doctor`="+id_doctor+"";
       ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               id_especializacion =  rs.getInt("id_especializacion");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
     return ce.get_especialidad(id_especializacion);      
    }
    
    
    
    /**
     * Retorna titulo segun el doctor
     * @param id_doctor
     * @return titulo
     */
    public String get_title(int id_doctor){
      String titulo = "";
      String sql = "SELECT `titulo` FROM `doctor` WHERE `id_doctor`="+id_doctor+"";
      ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
               titulo =  rs.getString("titulo");
            }    
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
      return titulo;
    }
    
    
    
   /**
    * Carga todas las especialidades en los ComboBox
    */   
    public void cargar_especialidades(){
        ResultSet rs = db.consulta("SELECT * FROM `especializacion`");
        try {
            while(rs.next()){
                p_doctor.selecEspecialidad.addItem(rs.getString(2));
                p_doctor.seleceditEspec.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    /*
    *  Limpia los compos del panel doctores
    */
    public void limpiar_campos_edicion(){
        p_doctor.editCed.setText("");
        p_doctor.editNom.setText("");
        p_doctor.editApe.setText("");
        p_doctor.editEma.setText("");
        p_doctor.editDir.setText("");
        p_doctor.editSelectEstado.setSelectedItem(0);
        p_doctor.editCEl.setText("");
        p_doctor.editDate.setDate(null);
        p_doctor.editEdad.setText("0");
        p_doctor.editSangre.setText("");
        p_doctor.editDiscap.setText("");
        p_doctor.editPeso.setText("");
        p_doctor.editTalla.setText("");
        p_doctor.editTitu.setText("");
        p_doctor.seleceditEspec.setSelectedIndex(0);
        p_doctor.ID.setText("");
    }
    
    
    
    /*
    |                                                       |
    |  Metodos privados -> no accsibles dedes las vistas    |
    |                                                       |
    */
    
    
    
    /**
     * Modifica segun el doctor (id_doctor) es requerido
     * @param d
     * @return mensaje de error o correcto
     */
    private String _modificar_doctor(Doctor d){
    String msg="";
        String sql =""
                + "UPDATE personas, doctor " +
                "SET personas.nombres = '"+d.getNombres()+"', personas.apellidos = '"+d.getApellidos()+"',"
                + "personas.cedula='"+d.getCedula()+"', personas.email= '"+d.getEmail()+"', personas.celular='"+d.getCelular()+"', "
                + "personas.direccion='"+d.getDireccion()+"', personas.estado_civil='"+d.getEstado_civl()+"', personas.fecha_nace= '"+new java.sql.Date(d.getFecha_nacimiento().getTime())+"',"
                + "personas.peso="+d.getPeso()+" , personas.sangre= '"+d.getTipo_sangre()+"', personas.discapacidad='"+d.getDiscapacidad()+"',"
                + "personas.talla='"+d.getTalla()+"',"
                + "doctor.titulo='"+d.getTitulo()+"', doctor.id_especializacion="+d.getEspecialidad()+" " +
                "WHERE (personas.id_personas = "+d.getId()+" AND doctor.id_doctor = personas.id_personas)";
        int rs = db.actualizarRegistro(sql);
        if(rs==1)
            msg="Ok;La actualizacion se realizo exitosamente";
        else
            msg="Error;Ocurrio un error al actualizar datos comuniquese con el administrador.";
        limpiar_campos_edicion();
        d_btnActual.setEnabled(false);
        d_btnEliminar.setEnabled(false);
        return msg;
    }
    
    
    
    /**
     * Agrega un nuevo doctor
     * @param d
     * @return mensaje de error o correcto
     */
    private String _new_doctor(Doctor d){
        String msg ="";
        String sql_persona ="INSERT INTO `personas`( `cedula`, `nombres`, "
                + "         `apellidos`, `email`, `celular`, `direccion`, "
                + "         `estado_civil`, `fecha_nace`, `peso`, "
                + "         `sangre`, `discapacidad`, `talla`, `tipo`,`id_sync`,`password`) VALUES("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String sql_doctor = "INSERT INTO `arc`.`doctor` (`id_doctor`, `id_especializacion`, `titulo`) VALUES (?, ?, ?)";
    
         try {
           
            PreparedStatement pst = con.prepareStatement(sql_persona);
            pst.setString(1, d.getCedula());
            pst.setString(2, d.getNombres());
            pst.setString(3, d.getApellidos());
            pst.setString(4, d.getEmail());
            pst.setString(5, d.getCelular());
            pst.setString(6, d.getDireccion());             
            pst.setString(7, d.getEstado_civl());
            pst.setDate(8, new java.sql.Date(d.getFecha_nacimiento().getTime()));
            //pst.setInt(9, d.getEdad());
            pst.setDouble(9, d.getPeso());
            pst.setString(10, d.getTipo_sangre());
            pst.setString(11, d.getDiscapacidad());
            pst.setDouble(12, d.getTalla());
            pst.setString(13, d.getTipo());    
            pst.setString(14, funciones.id_sync()+"-"+d.getCedula());
            pst.setString(15, d.getPassword());    
            int n=pst.executeUpdate();
               if(n>0)
               {
                   PreparedStatement pst2 = con.prepareStatement(sql_doctor);
                   pst2.setInt(1, get_id_by_cedula(d.getCedula()));
                   pst2.setInt(2, d.getEspecialidad());
                   pst2.setString(3, d.getTitulo());

                   int n2=pst2.executeUpdate();
                    if(n2>0)
                    {
                        msg ="Ok;Registro procesado con exito!";
                    }
                    else
                    {
                       msg = "Error;Error #1DOCDB No se pudo registrar los datos";
                    }
               }
               else
               {
                   msg = "Error;Error #001DOCDB intentelo mas tarde!";
               }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " error en la coneccion ----:  "+ex.getMessage());   
        }
        d_btnActual.setEnabled(false);
        d_btnEliminar.setEnabled(false);
       return msg;
    }
}
