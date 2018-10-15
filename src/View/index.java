/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;


import Controller.notifyController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author DJASC
 */
public class index {
     public static final String title="ARC® 2016 -  v.1.0.0.0";  
     public static notifyController notify_t=new notifyController();
     public static home home;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
        }catch (Exception e){
           JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            try {
                UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
            } catch (Exception ex) {
                Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        // TODO code application logic here
        login login=new login();
        login.setVisible(true);
        //home home=new home();
        //home.setVisible(true);
    }
    
    
   
}
