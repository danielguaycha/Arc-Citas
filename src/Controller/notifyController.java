/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.home;
import static View.home.arc_title;
import static View.home.barra_notify;

import static View.index.*;
import static View.index.title;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;


/**
 *
 * @author DJASC
 */
public class notifyController {
    private static int seg = 0, ds = 0;//unidades de medida
    private static boolean cerrar=false;
     private static boolean visibleTime = false;
    private static boolean pause=false;
    
    private static Thread hilo = new Thread() {//declaramos el hilo
        @Override
        public void run() {
            try {
                while (true) {//ciclo infinito
                    if(!pause){
                        if(!cerrar)
                            seg+=1;
                        else
                            seg-=1;
                        //y aumenta un segundo
                       if(seg<42 || (seg>0 && cerrar)){  
                        barra_notify.setBounds(0, 0, 850, seg);
                          //home.getContentPane().getComponent(2).setBounds(0, 0, 850, seg);
                        //  home.getContentPane().add(barra_notify, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, seg));
                        }
                        if (seg == 400) {//si los segundos son iguales a 59
                            cerrar=true;
                            seg=42;
                        }
                        if(seg<1 && cerrar){
                            seg = 0;//segundo vuelve a empezar en cero
                            cerrar=false;
                            notifyClose();  
                        }
                    }
                    hilo.sleep(10);//que duerma una decima de segundo
                }
            } catch (InterruptedException ie){};
        }
    };
    public static void setNotify(String Title, String Type_Error){
        if (!visibleTime) {//si no esta suspendido o pausado
               switch(Type_Error){
                case "Error":
                    home.barra_notify.setBackground(new Color(192,69,62));
                    sound("error");
                    break;
                 case "Alert":
                    barra_notify.setBackground(new Color(232,170,37));
                    sound("notify");
                    break;
                case "Ok":
                    barra_notify.setBackground(new Color(100,178,90));
                    sound("notify");
                    break;

            }
            arc_title.setText(Title);     
            barra_notify.setBounds(0, 0, 850, 0);
            //home.getContentPane().getComponent(2).setBounds(0, 0, 850, 0);
            pause = false;
            visibleTime=true;
            try{
            hilo.start();//el hilo empieza
            }catch(Exception ex){};
        }
    }
    
    public static void sound(String sound){
        try{
            Clip sonido = AudioSystem.getClip();
            sonido.open(AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir")+"\\media\\"+sound+".wav")));
            sonido.start();
        } catch (IOException ex) {
            Logger.getLogger(funciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(LineUnavailableException ex){
        }
        catch(UnsupportedAudioFileException ex){
        }
    }
    
    
   
    private static void notifyClose(){
        visibleTime = false;//el hilo esta suspendido
        ds =0;
        seg = 0;//todas las unidades en cero
        pause = true;//se suspende el hilo.. (NO utilizamos hilo.stop() porque si lo usamos, el hilo se "muere")  
        barra_notify.setBackground(new Color(50,60,75));
        barra_notify.setVisible(true);
//        home.getContentPane().add(barra_notify, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 42));
        arc_title.setText(title);
   }
}
