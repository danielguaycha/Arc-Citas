/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Config;
import Model.Email;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Control_email {
    
    
  public void sendHtmlEmail(Email email) throws AddressException,
    MessagingException{
       
       final String userName = Config.email;
       final String password = Config.passEmail;
       
       String toAddress = email.getFrom();
       String subject = email.getSubject();
       String message = email.getMensaje();
       
       String host = "smtp.gmail.com";
       String port = "587";
        
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        try {
            msg.setSubject(new String(subject.getBytes("utf-8"), "utf-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Control_email.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg.setSentDate(new Date());
        // set plain text message
        msg.addHeader( "Content-Type", "text/html; charset=UTF-8" );
        msg.setContent(message, "text/html; charset=UTF-8");
 
        // sends the e-mail
        Transport.send(msg);
 
    }
}
