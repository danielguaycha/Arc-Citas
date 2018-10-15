
package Model;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class DbConexion {
    
    Connection con = null;
    public Connection conectar(){
    
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/arc","root","");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "<html>Informacion<br>Falta la base de Datos Dsystem</html>","DSYSTEM",JOptionPane.INFORMATION_MESSAGE);
        }
        return con;
    }
    
    public ResultSet consulta(String sql){
 
        ResultSet rs= null;
       try {
             Statement st = conectar().createStatement();
             rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }   
        return rs;
    }
    
    public int actualizarRegistro(String sql){
        int bandera=-1;
            try {
                PreparedStatement post=conectar().prepareStatement(sql);
                int data = post.executeUpdate();
                if(data > 0 ){
                    bandera=1;
                } 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
                System.out.println(ex.getMessage());
            }
            
            return bandera;
    }
    
    public int eliminar(String sql) {
        int bandera=-1;
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            int a=pst.executeUpdate();
             System.out.println("valor "+a);
            if(a>=0){
            bandera=1;
            }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DbConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bandera;
    }
    
    public String get_id_(String sql, String id_name){
        String codigo="";
        try {
            Statement post = conectar().createStatement(); 
            ResultSet result = post.executeQuery (sql);
            if (result.next()==true) {
            codigo = ""+ (result.getInt(id_name)+1);
            }else{
            codigo ="1";}
   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " Error ---"+ex.getMessage());
        }
    return codigo;
    }
        
    }
    
    
    
