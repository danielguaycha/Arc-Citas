/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Config;
import Model.DbConexion;
import Model.Doctor;
import Model.Email;
import Model.ListaCita;
import Model.Paciente;
import View.panels.p_reserv;
import static View.panels.p_reserv.r_btn_save;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Daniel
 */
public class Control_reserva {
    
    private String sql;
    public  DefaultListModel model = null;
    DbConexion db= new DbConexion();
    Vector<ListaCita> lista;
    Vector<Doctor> listaDoctor;
    Connection con;
    Control_paciente cp;
    Control_doctores cd;
    Control_especialidades ce;
    Config conf;
    int id_doc;
    int id_cita;
    Vector<Integer> ids;
    
    public Control_reserva() {
        con =  db.conectar();
        cp= new Control_paciente();
        cd = new Control_doctores();
        ce = new Control_especialidades();
        conf = new Config();
        cargar_especializaciones();
        control_calendario();
    }
   
    
    /*
    |                                                     |
    |  Operaciones - CRUD                                 |
    |                                                     |
    */
    
    
    /**
     * Recibe datos valida e inserta en base de datos
     * @param id_especial
     * @param fecha
     * @param hora
     * @param costo
     * @param id_paciente
     * @param cedula
     * @return mensaje
     */
    public String reservar(int id_especial, Date fecha, String hora, double costo,int id_paciente, String cedula){
       String msg="";
       
       //.- Validando
       if(tiene_cita(cedula,fecha)){return "Alert;Este usuario ya accedio a un turno, Intentelo con una fecha diferente.";}
       if(cedula.equals("")){return "Alert;Necesitas escojer un paciente";}
       if(hora==null || hora.equals("")){return "Alert;Datos imcompletos, Necesitas ecojer una hora";}
       if(id_doc<=0){return "Alert;Necesitas ecojer un Doctor";}
       if(id_especial<=0){return "Alert;Necesitas escojer una especialidad";}
       if(fecha ==null){return "Alert;Necesitas escojer una fecha";}
        
        //sql ="INSERT INTO `citas`(`id_doctor`, `id_especializacion`, `fecha_cita`, `costo`, `id_paciente`, `cedula`,`numero_cita`,`id_sync`) "
          //      + "VALUES ('"+id_doc+"','"+id_especial+"','"+fecha+"','"+hora+"',"+costo+","+id_paciente+",'"+cedula+"', '"+get_number_cita(hora)+"',"+funciones.id_sync()+")";
        sql="INSERT INTO `citas`(`id_doctor`, `id_especializacion`, `fecha_cita`, `costo`, `pago`, `id_paciente`, `fecha_registro`, `id_sync`, `cedula`, `numero_cita`) "
                + "VALUES ("+id_doc+","+id_especial+",'"+fecha+" "+hora+"',"+costo+",0,"+id_paciente+",NOW(),'"+funciones.id_sync()+"','"+cedula+"', "+get_number_cita(hora)+")";
        
        
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            int n2=pst.executeUpdate();
            if(n2>0)
            {
                String mail = cp.get_email_by_id(id_paciente);
                
                if(!mail.equals("")){
                    Email m = new Email(mail, 
                                      cp.get_name_by_id(id_paciente), 
                                      this.get_number_cita(hora)+"", 
                                      hora, fecha+"", cd.get_name_by_id(id_doc));
                Control_email ce = new Control_email();
                    try {
                        ce.sendHtmlEmail(m);
                        msg ="Ok;Registro procesado con exito!";
                    } catch (MessagingException ex) {
                       // Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(ex.getMessage());
                        msg="Error;No se pudo enviar el correo electronico";
                    }
                  p_reserv.txtCedula.setText("");
                  limpiarCampos();
                }else
                     msg ="Ok;Registro procesado con exito!";
            }else{
                msg="Error;Error al Registrar ";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }
    
    
    
    
    /**
     * Recibe datos valida e hace un update en bd
     * @param id_especialidad
     * @param fecha
     * @param hora
     * @param costo
     * @param id_paciente
     * @param cedula
     * @return mensaje
     */
    public String actualizar_cita(int id_especialidad, Date fecha, String hora, double costo, int id_paciente, String cedula) {
       if(hora==null || hora.equals("")){return "Necesitas ecojer una hora";}
       if(id_doc<=0){return "Necesitas ecojer un Doctor";}
       if(id_especialidad<=0){return "Necesitas escojer una espcialidad";}
       if(fecha ==null){return "necesitas escojer una fecha";}
       
       sql ="UPDATE `citas` SET `id_doctor`="+id_doc+",`id_especializacion`="+id_especialidad+",`fecha_cita`='"+fecha+"',`hora`='"+hora+"',`costo`="+costo+",`numero_cita`='"+get_number_cita(hora)+"' WHERE `id_cita`="+id_cita;
       if(db.actualizarRegistro(sql)>0){
           limpiarCampos();
           p_reserv.txtCedula.setText("");
           return "Ok;Datos actualizados con exito";
           
       }else
           return "Error;Error al actualizar los registros";
    }
  
    
    
    
    /**
     * Elimina una cita Recibiendo como parametra una fila seleccionada (tbCitas)
     * @param row
     * @return mensaje
     */
    public String eliminar_cita(int row){        
        int id_citas = Integer.parseInt(p_reserv.r_tbCitas.getValueAt(row, 0).toString());
        sql ="UPDATE `citas` SET `status`=0 WHERE `id_cita`="+id_citas;
        int rs = db.eliminar(sql);
        if(rs>0){
            DefaultTableModel dtm = (DefaultTableModel) p_reserv.r_tbCitas.getModel(); 
            dtm.removeRow(row); 
            return "Ok;Registro Eliminado con exito";
        }else
            return "Error;Error al eliminar el registro";
       
        
    }
    
 
    /*
    |                                                     |
    |  Cargado de datos :D y Validacion                   |
    |                                                     |
    */
    
    
    
    /**
     * Cargar datos de las citas en la tabla (tbCitas) -> p_reservaciones
     */
    public void table_data_citas(){
        DefaultTableModel modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ids = new Vector<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String []dato = new String[7];    
            modelo.addColumn("#");
            modelo.addColumn("DOCTOR");
            modelo.addColumn("AREA");
            modelo.addColumn("FECHA");
            modelo.addColumn("COSTO");
            modelo.addColumn("PACIENTE");
            
            sql="SELECT personas.id_personas, personas.cedula, personas.tipo,personas.status ,(Select CONCAT(nombres,' ',apellidos)From personas where id_personas = citas.id_doctor )as nombre, "
            + "citas.* FROM `citas` JOIN personas ON (personas.cedula LIKE '"+p_reserv.r_txtSearch.getText()+"%' "
            + "and personas.id_personas=citas.id_paciente and citas.status=1 and personas.tipo='P' and personas.status=1) order by citas.fecha_cita limit 0,10";
            
            ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                dato[0]=rs.getInt("id_cita")+"";
                dato[1]=rs.getString("nombre");
                dato[2]=ce.get_especialidad(rs.getInt("id_especializacion"));
                dato[3]=rs.getString("fecha_cita");
                dato[4]=rs.getDouble("costo")+"";
                dato[5]=rs.getString("cedula");
                modelo.addRow(dato);
                ids.add(rs.getInt("id_doctor"));
            }
            p_reserv.r_tbCitas.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    

    
    
    /**
     * Permite Cargar datos a patir del evento key (TABULADOR _ ENTER)
     * @param cedula 
     */
    public void loadData(String cedula){
        if(!cedula.equals("")){
            if(cp.exist(cedula)){
                int id=0;
                ArrayList<Paciente> paciente = null;
                sql ="SELECT `id_personas`,`nombres`,`apellidos`,`email`,`direccion`,`estado_civil`,`celular` FROM `personas` WHERE `status`=1 and `tipo`='P' and cedula ='"+cedula+"' limit 0,1";
                ResultSet rs = db.consulta(sql);
                try {
                        r_btn_save.setEnabled(false);
                        while(rs.next()){
                          id = rs.getInt("id_personas");
                           paciente = new ArrayList<>();
                           paciente.add(new Paciente("",
                                    rs.getString("nombres"),
                                    rs.getString("Apellidos"),
                                    rs.getString("email"), 
                                    rs.getString("direccion"),
                                    rs.getString("estado_civil"), 
                                    rs.getString("celular"),""));
                          
                        }
                        p_reserv.r_txtNombre.setText(paciente.get(0).getNombres());
                        p_reserv.r_txtApellido.setText(paciente.get(0).getApellidos());
                        p_reserv.r_txtEmail.setText(paciente.get(0).getEmail());
                        p_reserv.r_txtDir.setText(paciente.get(0).getDireccion());
                        p_reserv.r_selectEstad_cib.setSelectedItem(paciente.get(0).getEstado_civl());
                        p_reserv.r_txtCelular.setText(paciente.get(0).getCelular());
                        p_reserv.r_txtID.setText(""+id);
                        p_reserv.r_selecEspecialidad.requestFocus();
                        enabledCampos(false);
                } catch (SQLException ex) {
                    Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                if(funciones.validadorDeCedula(cedula))
                {
                    r_btn_save.setEnabled(true);
                    limpiarCampos();
                    enabledCampos(true);
                }else
                    notifyController.setNotify("Cedula incorrecta! Verifique", "Alert");
            }
        }
   
    }
    
    
    
    /**
     * Permite cargar especialidades disponibles
     */
   //.-
    public void cargar_especializaciones(){
        ResultSet rs = db.consulta("SELECT * FROM `especializacion`");
        try {
            while(rs.next()){
                p_reserv.r_selecEspecialidad.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_doctores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**
     * Permite cargar los Doc. Disponbles segun especialidad
     * @param id_especializacion 
     */
    public void load_doc_by_area(int id_especializacion){
        listaDoctor = new Vector<>();
        int id_doctor;  
        String nombre;
        p_reserv.r_selecDoctor.removeAllItems();
        p_reserv.r_selecDoctor.addItem("<Seleccione>");
        //sql ="SELECT `id_doctor` FROM `doctor` WHERE `id_especializacion`="+id_especializacion+" ";
        sql ="SELECT doctor.id_doctor FROM `doctor`Join personas on "
        + "(doctor.id_doctor=personas.id_personas and personas.status=1 and doctor.id_especializacion ="+id_especializacion+" )";
        ResultSet rs= db.consulta(sql);
        try {
            while(rs.next()){
                id_doctor = rs.getInt("id_doctor");
                nombre=cd.get_name_by_id(rs.getInt("id_doctor"));
                listaDoctor.add(new Doctor(id_doctor, nombre));
                if(comprobar_horario(id_doctor))
                     p_reserv.r_selecDoctor.addItem(nombre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    /**
     * Permite cargar todas las horas desde la hora_de_incio hasta la hora_fin
     * dependiendo de la fecha en la lista (lista). No se valida ninguna hora en esta funcion 
     * @param f
     * @throws SQLException 
     */
    public void cargar_horas_by_fecha(String f) throws SQLException{
        p_reserv.r_list_hour_reserv.removeAll();//limpia la lista eb cada fecha
        String name_doctor = p_reserv.r_selecDoctor.getSelectedItem().toString();
        id_doc = _get_id_from_list(name_doctor);
       
        if(id_doc>0)
        {
            lista= new Vector<>();
            int numero_cita = 1;
            Date h_ini = null;
            Date h_fin=null;
            Date duracion = null;
            String descanso="";
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss"); 

            sql ="SELECT `hora_inicio`,`hora_fin`,`duracion_consulta`,`descanso` FROM `horarios` WHERE `id_doctor`="+id_doc+" AND `disponible`=1";
            ResultSet rs = db.consulta(sql);
           
            //Obteniendo los valores de la bd
            while(rs.next()){
                h_ini = rs.getTimestamp("hora_inicio");
                h_fin = rs.getTimestamp("hora_fin");
                duracion = rs.getTimestamp("duracion_consulta");
                descanso =rs.getString("descanso");
            }
            //Preparando para hacer las operaciones entre las horas
            Calendar calendar = Calendar.getInstance();//hora in
            
            //combirtiendo el intervalo (Date) a 00:00 y -> a minutos
            int d = horas_to_min(sd.format(duracion));
            int in = horas_to_min(sd.format(h_ini));
            int out = horas_to_min(sd.format(h_fin));

            //inicializando los valores del calendario
            calendar.setTime(h_ini);
            
            //adicionando la hora de inicio a la lista
            lista.add(new ListaCita(sd.format(calendar.getTime()),numero_cita));
           
            //mientras la hora de inicio sea < que la hora de fin
            do{
                if(in+d>=out)// si (hora de entrada - duracion) = hora de salida frena
                {
                    break;
                }
                else
                {
                    numero_cita++;// para llenar la lista con el numero de cita #
                    calendar.add(Calendar.MINUTE, d);//sumado la duracion (d)
                    lista.add(new ListaCita(sd.format(calendar.getTime()),numero_cita));
                }
                in+=d;
            }while(in<out);

            //Una vez que los datos son add a la lista se los comprobara
            //para validar horas de descanso y horas ya apartadas en citas
            comprobarDatosLista(f,descanso);
        }
    }
    
    
    /**
     * Valida las horas que ya estan apartadas en la base de datos
     * Valida las horas de descanso desde las 13 hasta las 14 p.ej no se mostraran
     * Valida la hora en la que esta siendo seleccionado el turno
     * @param fecha
     * @param descanso 
     */
    public void comprobarDatosLista(String fecha, String descanso){
        model= new DefaultListModel();
        Vector<String> deletes =new Vector<>();
        int h_descanso_ini = horas_to_min(conf.h_descanso_ini);
        int h_descanso_end = horas_to_min(conf.h_descanso_fin);
        sql ="SELECT TIME_FORMAT(`fecha_cita`, '%h:%i:%s') as hora FROM `citas` WHERE `id_doctor`="+id_doc+" AND DATE_FORMAT(`fecha_cita`,'%Y-%m-%d') ='"+fecha+"'  ORDER BY TIME_FORMAT(fecha_cita, '%h:%i:%s')";
        ResultSet rs = db.consulta(sql);
        
        try {
            while(rs.next()){
               //comprobamos si las horas que tenmos en la bd estaen la lista (lista)
               //entonces la ponemos en status false para que no sea mostrada.
               exist_in_list(rs.getString("hora"));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        //si las horas de descando esta dentro del intervalo de horas de inicio y fin
        //entonces se validara de diferente manera
        if(descanso.equals("0"))
        {
            for (ListaCita v : lista) {
                //si la hora actual es >= hora del inicio_descanso y es < fin_descanso 
                //entonces esas horas las ponemos en status false para que no se puedan ver
                if((horas_to_min(v.getHora())>=h_descanso_ini && horas_to_min(v.getHora())<h_descanso_end)){
                    v.setStatus(false);
                }
            }
            //ahora solo las que esten en status true seran visibles para el usuario
            //Pero antes se debe validar las hora en la que se tomara el turno
            //es decir si la fecha es 2016-2-15 11:00:00  no podre visualizar turnos
            // desde 2016-2-15 8:00:00 hasta desde 2016-2-15 11:00:00
            for (ListaCita ls : lista) {
                if(ls.getStatus()==true){
                    if(mostar_hora(ls.getHora(), fecha))
                        model.addElement(ls.getHora());
                }
            }
            if(model.size()<=0){
                notifyController.setNotify("No hay citas disponibles para hoy !", "Error");
                p_reserv.r_list_hour_reserv.setModel(model);
                p_reserv.r_list_hour_reserv.setEnabled(false);
            }else{
                p_reserv.r_list_hour_reserv.setEnabled(true);
                p_reserv.r_list_hour_reserv.setModel(model);
                //p_reserv.r_list_hour_reserv.setListData(retorno);
            } 
        }
        else
        {
            for (ListaCita ls : lista) {
                if(ls.getStatus()==true){
                  if(mostar_hora(ls.getHora(), fecha))
                   model.addElement(ls.getHora());
                }
            }
            if(model.size()<=0){
                 notifyController.setNotify("No hay citas disponibles para hoy !", "Error");
                p_reserv.r_list_hour_reserv.setModel(model);
                p_reserv.r_list_hour_reserv.setEnabled(false);
            }else{
                p_reserv.r_list_hour_reserv.setEnabled(true);
                p_reserv.r_list_hour_reserv.setModel(model);
            }
        }
    }
    
    
    
  
    /*
    |                                                     |
    | Funciones de comprobacion de datos                  |
    |                                                     |
    */
    
    
     
    //.- Comprobar si el doctor ha conf. un horario
    public boolean comprobar_horario(int id_doctor){
        String sql2 ="SELECT `id_horario` FROM `horarios` WHERE `disponible`= 1 AND `id_doctor`="+id_doctor;
        ResultSet rs2=db.consulta(sql2);
        int id=-1;
        
        try {
            while(rs2.next()){
               id = rs2.getInt("id_horario");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(id ==-1){
            return false;
        }else
            return true;
    }
    
    //.- Comprueba si la hora de la base de datos eciste en la lista
    public boolean exist_in_list(String hour){
        for (ListaCita l : lista) {
            if(l.getHora().equals(hour)){
                l.setStatus(false);
                return true;
            }
        }
        return false;
    }
    
    //.-Comprueba las horas que se mostraran segun la hora actual
    public boolean mostar_hora(String hora, String fecha){
        Date f = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        
        String fecha_act = format2.format(f);
        
        String hora_actual = format.format(f);
        int min_actual =  horas_to_min(hora_actual);
        int min_recibid = horas_to_min(hora);
        
        if(min_recibid<min_actual && fecha.equals(fecha_act)){
            return false;
        }else
            return true;
    }
    
    //.-Comprueba si el usuario ya tomo una cita sea que este eliminada o no
    public boolean tiene_cita(String cedula, Date fecha){
        sql="SELECT `id_cita` FROM `citas` WHERE `cedula`='"+cedula+"' and DATE_FORMAT(`fecha_cita`,'%Y-%m-%d')='"+fecha+"'";
        int id=-1;
        ResultSet rs = db.consulta(sql);
        try {
            while(rs.next()){
                id = rs.getInt("id_cita");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(id>0)return true;
        else return false;
    }
    
    /*
    |                                                     |
    | Funciones de Interfaz                               |
    |                                                     |
    */
    
    //.-Llena los campos desde la tabla de edicion  
    public void llenar_campos_edicion(int row){
         
        p_reserv.r_PanelReservas.setSelectedIndex(0);
        
        id_cita = Integer.parseInt(p_reserv.r_tbCitas.getValueAt(row, 0).toString());
        String cedula=  p_reserv.r_tbCitas.getValueAt(row, 5).toString();
        p_reserv.txtCedula.setText(cedula);
        
        loadData(cedula);
        
        id_doc = ids.get(row);
        int id_esp =ce.get_id_by_nombre(p_reserv.r_tbCitas.getValueAt(row, 2).toString());
        p_reserv.r_selecEspecialidad.setSelectedIndex(id_esp);
        
        
        load_doc_by_area(id_esp);
        String []vec_fech =p_reserv.r_tbCitas.getValueAt(row, 3).toString().split(" ");
        String date = vec_fech[0];
       
        p_reserv.r_dateReserv.setDate(FormatoDate(date));
        try {
          cargar_horas_by_fecha(date);
        } catch (Exception ex) {
            Logger.getLogger(Control_reserva.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        p_reserv.r_lbl_hora.setText("H Anterior.");
        p_reserv.r_txt_hora.setText(vec_fech[1]);
        p_reserv.r_selecDoctor.setSelectedItem(p_reserv.r_tbCitas.getValueAt(row, 1).toString());
        
        p_reserv.r_txtcosto.setText(p_reserv.r_tbCitas.getValueAt(row, 4).toString());
      
        p_reserv.r_selecDoctor.setEnabled(true);
        p_reserv.txtCedula.setEnabled(false);
        p_reserv.r_btnEdit.setEnabled(true);
        p_reserv.r_btnReservar.setEnabled(false);
        //p_reserv.r_dateReserv.setMinSelectableDate(null);
        
    }
    
    //.-Control de la interfaz - Calendario
    public void control_calendario(){
        p_reserv.r_dateReserv.setMinSelectableDate(new Date());
        p_reserv.r_dateReserv.getCalendarButton().setEnabled(false);
        p_reserv.r_dateReserv.setDate(new Date());
        p_reserv.r_dateReserv.getJCalendar().setTodayButtonVisible(false);
    }
    
    //.-Limpia los campos de las reservaciones
    public void limpiarCampos(){
      //  p_reserv.txtCedula.setText("");
        p_reserv.r_txtNombre.setText("");
        p_reserv.r_txtApellido.setText("");
        p_reserv.r_txtEmail.setText("");
        p_reserv.r_txtDir.setText("");
        p_reserv.r_selectEstad_cib.setSelectedIndex(0);
        p_reserv.r_txtCelular.setText("");
        p_reserv.r_txtID.setText("");
        p_reserv.r_selecEspecialidad.setSelectedIndex(0);
        p_reserv.r_selecDoctor.setSelectedIndex(0);
        p_reserv.r_dateReserv.setDate(new Date());
        p_reserv.r_list_hour_reserv.removeAll();
        p_reserv.r_txtcosto.setText("00.00");
        p_reserv.r_txt_hora.setText("");
        p_reserv.r_lbl_hora.setText("Hora");
        p_reserv.r_btnReservar.setEnabled(true);
        p_reserv.r_btnEdit.setEnabled(false);
        if(model!=null){
            model.removeAllElements();
            p_reserv.r_list_hour_reserv.setModel(model);
        }
    }
    
    //.-Activa o desactiva los campos
    public void enabledCampos(boolean val){
        p_reserv.r_txtNombre.setEnabled(val);
        p_reserv.r_txtApellido.setEnabled(val);
        p_reserv.r_txtEmail.setEnabled(val);
        p_reserv.r_txtDir.setEnabled(val);
        p_reserv.r_selectEstad_cib.setEnabled(val);
        p_reserv.r_txtCelular.setEnabled(val);
        p_reserv.r_btnEdit.setEnabled(val);
        p_reserv.r_txtPass.setEnabled(val);
    }
    
    
    
    /*
    |                                                     |
    | Funciones de getter                                 |
    |                                                     |
    */
    
    //.-Usada para saber el id de los doctores seleccionados
    private int _get_id_from_list(String nombre){
        for (Doctor d : listaDoctor) {
            if(d.getNombres().equals(nombre)){
               return d.getId();
            }
        }
        return -1;
    }
    
    //.-Segun la hora se obtiene el numero de la cita necesario para el email
    public int get_number_cita(String hora){
        for (ListaCita l : lista) {
            if(l.getHora().equals(hora)){
                return l.getNumero_cita();
            }
        }
        return -1;
    }
    
  
     /*
    |                                                     |
    | Funciones de control de fecha-hora                  |
    |                                                     |
    */
    
    
    public int horas_to_min(String hora){
        String []v= hora.split(":");
        int h = Integer.parseInt(v[0]);
        int min= Integer.parseInt(v[1]);
        return (h*60)+min; 
    }
    
    public  Date FormatoDate (String fecha){
     SimpleDateFormat c=new SimpleDateFormat("yyyy-MM-dd");
     String strFecha = fecha;
     Date f=null;
        try {
            f=c.parse(strFecha);
        } catch (ParseException ex) {
           
        } 
      return f;
  }

    
}
