/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.funciones.setPanel;
import Model.DbConexion;
import Model.Doctor;
import Model.Paciente;
import View.panels.p_doctor;
import View.panels.p_paciente;
import View.panels.p_search;
import static View.panels.p_search.rdDoctores;
import static View.panels.p_search.rdPacientes;
import static View.panels.p_search.txtSearch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Daniel
 */
public class Control_busqueda {
    DbConexion db;
    Control_doctores cd;
    DefaultTableModel modelo;
    String sql;
    ArrayList<Doctor>doctor;
    ArrayList<Paciente>paciente;
            
    public Control_busqueda() {
        db  = new DbConexion();
        doctor = new ArrayList<>();
        paciente = new ArrayList<>();
    }
    
    
    /**
     * Llena la tabla Search -> (tbResult)
     * Dependencia -> Seleccion de RadioButton
     */
    public void mostarDatos(){
        String []dato = new String[7];
        ResultSet rs;
        //Filas no editables
        modelo = new DefaultTableModel(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if(rdDoctores.isSelected()){
            modelo.addColumn("CEDULA");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("APELLIDO");
            modelo.addColumn("TELEFONO");
            modelo.addColumn("ESPECIALIDAD");
            sql="SELECT * from personas where tipo='D' and status=1  and (nombres LIKE '%"+txtSearch.getText()+"%' or apellidos LIKE '%"+txtSearch.getText()+"%'"
            + "  or cedula LIKE '%"+txtSearch.getText()+"%')  ORDER BY nombres ASC Limit 0,10";
        }
        if(rdPacientes.isSelected()){
            modelo.addColumn("CEDULA");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("APELLIDO");
            modelo.addColumn("TELEFONO");
            sql="SELECT * from personas where tipo='P' and status=1  and (nombres LIKE '%"+txtSearch.getText()+"%' or apellidos LIKE '%"+txtSearch.getText()+"%'"
            + "  or cedula LIKE '%"+txtSearch.getText()+"%')  ORDER BY nombres ASC Limit 0,10";
        }
        if(p_search.rdCitas.isSelected()){
            modelo.addColumn("#");
            modelo.addColumn("DOCTOR");
            modelo.addColumn("AREA");
            modelo.addColumn("FECHA");
            modelo.addColumn("COSTO");
            modelo.addColumn("PACIENTE");
            
            sql="SELECT personas.id_personas, personas.cedula, personas.tipo,personas.status ,(Select CONCAT(nombres,' ',apellidos)From personas where id_personas = citas.id_doctor )as nombre, "
            + "citas.* FROM `citas` JOIN personas ON (personas.cedula LIKE '"+txtSearch.getText()+"%' "
            + "and personas.id_personas=citas.id_paciente and citas.status=1 and personas.tipo='P' and personas.status=1) order by citas.fecha_cita limit 0,10";
            
        }
        // fin de condicioones de consulta
        int c = 0;
        rs= db.consulta(sql);  
        
        try {
            while(rs.next()){
                if(rdDoctores.isSelected()){
                    Doctor doc = new Doctor();
                    Control_doctores cdd = new Control_doctores();
                    doc.setId(rs.getInt("id_personas"));
                    doc.setCedula(rs.getString("cedula"));
                    doc.setNombres(rs.getString("nombres"));
                    doc.setApellidos(rs.getString("apellidos"));
                    doc.setEmail(rs.getString("email"));
                    doc.setCelular(rs.getString("celular"));
                    doc.setDireccion(rs.getString("direccion"));
                    doc.setDiscapacidad(rs.getString("discapacidad"));
                    doc.setEstado_civl(rs.getString("estado_civil"));
                    doc.setFecha_nacimiento(rs.getDate("fecha_nace"));
                    //doc.setEdad(rs.getInt("edad"));
                    doc.setPeso(rs.getInt("peso"));
                    doc.setTalla(rs.getDouble("talla"));
                    doc.setPassword(rs.getString("password"));
                    doc.setTipo_sangre(rs.getString("sangre"));
                                   
                    String espec =cdd.get_especialidad(rs.getInt("id_personas"));
                    dato[0]=rs.getString("cedula");
                    dato[1]=rs.getString("nombres");
                    dato[2]=rs.getString("apellidos");
                    dato[3]=rs.getString("celular");
                    dato[4]=espec;
                    
                    doctor.add(c,doc);
                }
                if(rdPacientes.isSelected()){
                    Paciente pc = new Paciente();
                    Control_paciente cpp = new Control_paciente();
                    pc.setId(rs.getInt("id_personas"));
                    pc.setCedula(rs.getString("cedula"));
                    pc.setNombres(rs.getString("nombres"));
                    pc.setApellidos(rs.getString("apellidos"));
                    pc.setEmail(rs.getString("email"));
                    pc.setCelular(rs.getString("celular"));
                    pc.setDireccion(rs.getString("direccion"));
                    pc.setDiscapacidad(rs.getString("discapacidad"));
                    pc.setEstado_civl(rs.getString("estado_civil"));
                    pc.setFecha_nacimiento(rs.getDate("fecha_nace"));
                    //pc.setEdad(rs.getInt("edad"));
                    pc.setPeso(rs.getInt("peso"));
                    pc.setTalla(rs.getDouble("talla"));
                    pc.setTipo_sangre(rs.getString("sangre"));
                    pc.setPassword(rs.getString("password"));
                    
                    dato[0]=rs.getString("cedula");
                    dato[1]=rs.getString("nombres");
                    dato[2]=rs.getString("apellidos");
                    dato[3]=rs.getString("celular");
                    
                    paciente.add(c, pc);
                }
                if(p_search.rdCitas.isSelected()){
                    Control_especialidades ce = new Control_especialidades();
                    dato[0]=rs.getInt("id_cita")+"";
                    dato[1]=rs.getString("nombre");
                    dato[2]=ce.get_especialidad(rs.getInt("id_especializacion"));
                    dato[3]=rs.getString("fecha_cita");
                    dato[4]=rs.getDouble("costo")+"";
                    dato[5]=rs.getString("cedula");
                }
                modelo.addRow(dato);
              c++;
            }
            p_search.tbResult.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(Control_busqueda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Segun (row)->(tabla-) se cargan datos para edicion
     * @param row type Strung 
     */
    public void llenar_campos(int row){
        
        if(rdDoctores.isSelected()){
            System.out.println();
            cd = new Control_doctores();
            setPanel(new p_doctor()); 
            int id_persona = doctor.get(row).getId();
            p_doctor.PanelDoc.setSelectedIndex(1);
            p_doctor.editCed.setText(doctor.get(row).getCedula());
            p_doctor.editNom.setText(doctor.get(row).getNombres());
            p_doctor.editApe.setText(doctor.get(row).getApellidos());
            p_doctor.editEma.setText(doctor.get(row).getEmail());
            p_doctor.editDir.setText(doctor.get(row).getDireccion());
            p_doctor.editSelectEstado.setSelectedItem(doctor.get(row).getEstado_civl());
            p_doctor.editCEl.setText(doctor.get(row).getCelular());
            p_doctor.editDate.setDate(doctor.get(row).getFecha_nacimiento());
            p_doctor.editEdad.setText(funciones.edad(doctor.get(row).getFecha_nacimiento())+"");
            p_doctor.editSangre.setText(doctor.get(row).getTipo_sangre());
            p_doctor.editDiscap.setText(doctor.get(row).getDiscapacidad());
            p_doctor.editPeso.setText(doctor.get(row).getPeso()+"");
            p_doctor.editTalla.setText(doctor.get(row).getTalla()+"");
            p_doctor.editTitu.setText(cd.get_title(id_persona));
            p_doctor.editPass.setText(doctor.get(row).getPassword());
            p_doctor.seleceditEspec.setSelectedIndex(cd.get_id_especialidad(id_persona));
            p_doctor.ID.setText(id_persona+"");
            p_doctor.d_btnActual.setEnabled(true);
            p_doctor.d_btnEliminar.setEnabled(true);
        }
        else if(rdPacientes.isSelected()){
            
            setPanel(new p_paciente()); 
            p_paciente.PanelPaciente.setSelectedIndex(1);
            int id_persona = paciente.get(row).getId();
            p_paciente.ID.setText(id_persona+"");
            p_paciente.p_editNombre.setText(paciente.get(row).getNombres());
            p_paciente.p_editCed.setText(paciente.get(row).getCedula());
            p_paciente.p_editApe.setText(paciente.get(row).getApellidos());
            p_paciente.p_editEmail.setText(paciente.get(row).getEmail());
            p_paciente.p_editDir.setText(paciente.get(row).getDireccion());
            p_paciente.p_editSelec_est_civ.setSelectedItem(paciente.get(row).getEstado_civl());
            p_paciente.p_editCel.setText(paciente.get(row).getCelular());
            p_paciente.p_editDateNaci.setDate(paciente.get(row).getFecha_nacimiento());
            p_paciente.p_editEdad.setText(funciones.edad(paciente.get(row).getFecha_nacimiento())+"");
            p_paciente.p_editPeso.setText(paciente.get(row).getPeso()+"");
            p_paciente.p_editSangre.setText(paciente.get(row).getTipo_sangre());
            p_paciente.p_editDiscap.setText(paciente.get(row).getDiscapacidad());
            p_paciente.p_editTalla.setText(paciente.get(row).getTalla()+"");
            p_paciente.p_editPass.setText(paciente.get(row).getPassword());
            p_paciente.p_btnActualizar.setEnabled(true);
            p_paciente.p_btnEliminar.setEnabled(true);
        }
    }
}
