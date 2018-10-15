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
public class Email {
    
    private String from;
    private String subject;
    private String mensaje;
    private String paciente;
    private String n_turno;
    private String hora;
    private String fecha;
    private String doctor;
    private String tiempo_espera;

    public Email() {
    }
  
    public Email(String from, String subject, String mensaje, String paciente, String n_turno, String hora, String fecha, String doctor, String tiempo_espera) {
        this.from = from;
        this.subject = subject;
        this.mensaje = mensaje;
        this.paciente = paciente;
        this.n_turno = n_turno;
        this.hora = hora;
        this.fecha = fecha;
        this.doctor = doctor;
        this.tiempo_espera = tiempo_espera;
    }

    public Email(String from,String paciente, String n_turno, String hora, String fecha, String doctor) {
        this.from = from;
        this.subject = "Turno de cita Medica - "+((Model.Config.nombre_hostpital.equals(""))?"Arcit":Model.Config.nombre_hostpital);
        this.paciente = paciente;
        this.n_turno = n_turno;
        this.hora = hora;
        this.fecha = fecha;
        this.doctor = doctor;
        this.tiempo_espera ="10 minutos";
        this.mensaje ="<body>\n" +
            "<div style=\"width:600px; padding:10px; margin:0 auto; font-size:17px; font-family:Gotham, 'Helvetica Neue', Helvetica, Arial, sans-serif;\">\n" +
            "<center>\n" +
            "  <p style=\"text-align:justify;\">Usted ha apartado un turno en Arcit, a continuacion tendra la informacion \n" +
            "   necesaria para acudir al centro medico.</p><p style=\"text-align:justify;\"><strong>Nota: </strong>Le recordamos que si desea cancelar el turno lo haga con 2 dias de anticipación...<br><br>Para mas infomacion comuniquese a los telefonos : "+Config.telefono+"</p><br>\n" +
            "   <p style=\"color:#A3A3A3; font-size:16px; text-align:left;\"><small><strong>Dirección: </strong>"+Config.direccion+"</small></p>\n" +
            "</center>\n" +
            "</div>\n" +
            "<hr>\n" +
            "<div style=\"border:2px solid #31426A; overflow:hidden; width:600px; padding:10px; font-family:'Gill Sans', 'Gill Sans MT', 'Myriad Pro', 'DejaVu Sans Condensed', Helvetica, Arial, sans-serif ;line-height:2px; border-radius:12px; margin:0 auto;\">\n" +
            "    <center>\n" +
            "      <h2 style=\"letter-spacing:20px;\">CITA  MEDICA</h2>\n" +
            "  	  <p><img src=\"https://lh3.googleusercontent.com/jbdZ1x2NJJkkfMKcOgbUkfZh1M-SOF3DVe4u-uK38zWcOAwRtx6ONj0unEOAAO5f4oxIxLGCxX0j96MN6w3MbrjWsbTwfHA-b4V2_T8yqNWwV4sl3GzhLNarJwHeAUS4X5eTUuzV3iwA0DzbKAF5k8WfE8vwYRdH0vtAhuSTHUIAfE9R4l5l-ubDPcJOhSGJ6bcJSumE8fIhwGWK-u6oE_E2SA7dD2D0Sq5On1y0gvsaazVE2TMS4aZ4HOiDobMNVLbVFnZPxAZtSjQXFmg-Bh0KUK25pwsgaL5BJ2DzQsU5NZHJ3XbAycj0S4O8l-neYvzqovRDMfVtoJF8rluk3Gv4ZxUQG4gYjXZ3v5HWncKXv3Jb-5a45f-pA0lw5XWGUSJ2n34xidX73tyiL_Gv_bXNIJaj_SFuKdgFqnOmoRVvfpZy4ucnaATKExmhSkdhzAmkNz1KhRbrVN9mCzeGEVfw3wvbSoMK41Jpi-J_g4hehsVEAPnGsSAXndEnGcW4xmxcPYEeCo6sB8BLNnRTOaRd7w_NfYvEzNAp66c6GK1-eATtOuFd4RL68PkK7nncGpfM=w128-h141-no\"></p>\n" +
            "    </center>\n" +
            "<center><h3 style=\"text-transform:uppercase; font-weight:100;\">Gracias por usar nuestros servicios</h3>\n" +
            "   <p>Usted sera atendido con el turno número:</p>\n" +
            "   <center><h1 style=\"font-size:86px;\">"+this.n_turno+"</h1></center>\n" +
            "   <div style=\"border:1px solid #ccc; text-align:left; line-height:1.3; border-radius:12px;\">\n" +
            "   	<ul>\n" +
            "      <center><li style=\"list-style:none;\"><h4>"+Config.nombre_hostpital+"</h4></li></center>\n" +
                       "<li style=\"list-style:none;\"><strong>Paciente:</strong> <span>"+this.paciente+"</span> </li>"+
            "    	<li style=\"list-style:none;\"><strong>Hora:</strong> <span>"+this.hora+"</span> </li>\n" +
            "    	<li style=\"list-style:none;\"><strong>Fecha:</strong> <span>"+this.fecha+"</span> </li>\n" +
            "    	<li style=\"list-style:none;\"><strong>Doctor:</strong> <span>"+this.doctor+"</span> </li>\n" +
            "    	<li style=\"list-style:none;\"><strong>Tiempo Estimado de espera: :</strong> <span>"+this.tiempo_espera+"</span> </li>\n" +
            "    </ul>  \n" +
            "   </div>\n" +
            "  </center>\n" +
            " <center><small><p>Youtteam Inc - Derechos Reservados por el autor</p></small></center>\n" +
            "</div>\n" +
            "</body>\n" +
            "";
    }   

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getN_turno() {
        return n_turno;
    }

    public void setN_turno(String n_turno) {
        this.n_turno = n_turno;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getTiempo_espera() {
        return tiempo_espera;
    }

    public void setTiempo_espera(String tiempo_espera) {
        this.tiempo_espera = tiempo_espera;
    }
    
    
}
