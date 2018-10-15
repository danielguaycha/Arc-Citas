
package Model;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Doctor extends Persona{
    private String titulo;
    private int especialidad;
    private String tipo;

    public Doctor() {
    }

    public Doctor(int id, String nombres) {
        super(id, nombres);
    }

    public Doctor(String titulo, int especialidad, String cedula, String nombres, String apellidos, String email, String direccion, String estado_civl, String celular, Date fecha_nacimiento, int edad, double peso, String tipo_sangre, String discapacidad, double Talla, String password) {
        super(cedula, nombres, apellidos, email, direccion, estado_civl, celular, fecha_nacimiento, edad, peso, tipo_sangre, discapacidad, Talla,password);
        this.titulo = titulo;
        this.especialidad = especialidad;
        this.tipo = "D";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(int especialidad) {
        this.especialidad = especialidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    

}