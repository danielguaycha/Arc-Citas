package Controller;

import static View.home.escritorio;
import View.index;
import View.panels.p_home;
import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class funciones{
     
    public static void centerWindows(JFrame form){
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int height = pantalla.height;
        int width = pantalla.width;
        form.setSize(width/2, height/2);		
        form.setLocationRelativeTo(null);		
        form.setVisible(true);
        Shape forma = new RoundRectangle2D.Double(0, 0, form.getBounds().width, form.getBounds().height, 300, 300); 
    
    }
    
    public static void setPadding(JTextField Input){
        Input.setBorder(BorderFactory.createCompoundBorder(
        Input.getBorder(),BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
   
    
    /*=========== PARA INPUTS */
     // TODO add your handling code here:
    public static void setPlaceholder(JTextField txt, String title, int type){
        if(type==0){
            if(txt.getText().equals(title)){
                txt.setCaretPosition(0);
                txt.setForeground(new java.awt.Color(153,153,153));
            }if(txt.getText().equals("")){
                txt.setText(title);
                txt.setCaretPosition(0);
                txt.setForeground(new java.awt.Color(153,153,153)); 
            }             
        }else if(type==1){
            if(txt.getText().equals(title)){
                txt.setText("");
                txt.setForeground(new java.awt.Color(51,51,51));
            }  
        }        
    }
    
    public static void hoverClose(JLabel b){
        b.setOpaque(true);
        b.setBackground(Color.gray);
        b.setForeground(new Color(240,240,240));
    }
    
    public static void outClose(JLabel b){
        b.setOpaque(false);
        b.setForeground(new Color(102,102,102));
    }
    
    public static void setPanel(JPanel p){
        p.setBounds ( -1,0,651,558 );
        escritorio.removeAll();
        escritorio.add(p);
        escritorio.revalidate();
        escritorio.repaint();
        
    }
    
    public static String parse_date_to_Sql(Date date){
        Format formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(date);
    }
    
    //==========================================================================
    //================   VALIDACIONES  =========================================
    
    public static boolean validadorDeCedula(String cedula) {
     boolean cedulaCorrecta = false;
    
     try {
       if (cedula.length() == 10) // ConstantesApp.LongitudCedula
         {
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
            if (tercerDigito < 6) {
            // Coeficientes de validación cédula
            // El decimo digito se lo considera dígito verificador
                int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                int verificador = Integer.parseInt(cedula.substring(9,10));
                int suma = 0;
                int digito = 0;
                for (int i = 0; i < (cedula.length() - 1); i++) 
                        {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                        }
 
                  if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                            cedulaCorrecta = true; }
                  
                 else if ((10 - (suma % 10)) == verificador) {
                         cedulaCorrecta = true;} 
                 else {
                        cedulaCorrecta = false;
                        }
                 } 
  
            else {  cedulaCorrecta = false; }
         } 
           
         else {
                   cedulaCorrecta=false;
              }   
     } 
        
        catch (NumberFormatException nfe) {
         cedulaCorrecta = false;  } 
        catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
                            }
 
         if (!cedulaCorrecta) {
                System.out.println("La Cédula ingresada es Incorrecta"); }
                return cedulaCorrecta;
     } 

    public static void validaNum (java.awt.event.KeyEvent evt){
    
    char caracter = evt.getKeyChar();
        if(!(Character.isDigit(caracter)) && (caracter != KeyEvent.VK_BACK_SPACE)){
            evt.consume();
        }
    }
    
    public static void ValidaTexto(java.awt.event.KeyEvent evt){
    
            char caracter = evt.getKeyChar();
        if((Character.isDigit(caracter)) && (caracter != KeyEvent.VK_BACK_SPACE)){
            evt.consume();
        }
    
    }
    
    public static void ValidaEnteroDecimal(java.awt.event.KeyEvent evt, JTextField jTexfiel) {
     int k = evt.getKeyChar();
        if (k>=46 && k <=57 ){
            if(k==46){
                String dato = jTexfiel.getText();
               
                for (int i = 0; i <dato.length() ; i++) {
                    if(dato.contains("."))
                        evt.setKeyChar((char)KeyEvent.VK_CLEAR);
                }
            }
        
        if(k==47){
        evt.setKeyChar((char)KeyEvent.VK_CLEAR);
        }
        }
        else {evt.setKeyChar((char)KeyEvent.VK_CLEAR);
        evt.consume();
        }
 }
     public static void configTheme( int config_Theme){
        try{
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[config_Theme].getClassName());
            }catch (Exception e){
               JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                config_Theme=1;
                try {
                    UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[config_Theme].getClassName());
                } catch (Exception ex) {
                    Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
                }
        }    
    }
     
     public static String id_sync(){
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz1234567890";
	String token = "";
	for(int i=0;i<60;i++) {
                int aletorio=(int) Math.floor((Math.random()*str.length()-1)+1);
		token += str.substring(aletorio, aletorio+1);
	}
	token+="-arc-SERV";
        return token;
     }
     
    public static int edad(Date fecha) {     //fecha_nac debe tener el formato dd/MM/yyyy
   
    Date fechaActual = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    String hoy = formato.format(fechaActual);
    String fecha_nac = formato.format(fecha);
    String[] dat1 = fecha_nac.split("-");
    String[] dat2 = hoy.split("-");
    int anos = Integer.parseInt(dat2[0]) - Integer.parseInt(dat1[0]);
    int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
    if (mes < 0) {
      anos = anos - 1;
    } else if (mes == 0) {
      int dia = Integer.parseInt(dat2[0]) - Integer.parseInt(dat1[0]);
      if (dia > 0) {
        anos = anos - 1;
      }
    }
    return anos;
  }
 }