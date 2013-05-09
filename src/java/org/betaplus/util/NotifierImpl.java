/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.betaplus.datatypes.Notification;
import org.betaplus.datatypes.User;

/**
 *
 * @author StephenJohnRussell
 */
public class NotifierImpl implements Notifier {

    private LinkedList<Notification> notes;

    /**
     *
     */
    public NotifierImpl() {
        notes = new LinkedList<Notification>();
    }

    @Override
    public boolean sendNotification(LinkedList<User> to) {
        if (!notes.isEmpty()) {
            try {
                String host = "smtp.gmail.com";
                String from = "betaplustestemail@gmail.com";
                String pass = "nottesting123";
                Properties props = System.getProperties();
                props.put("mail.smtp.starttls.enable", "true"); // added this line
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.user", from);
                props.put("mail.smtp.password", pass);
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");

                for (User adressTo : to) {
                    Session session = Session.getDefaultInstance(props, null);
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(adressTo.getUserEmail()));
                    message.setSubject("Updates Available in WebsiteMonitor");
                    String msg = "Hi " + adressTo.getUserName() + "\n"
                            + "\tYou have new updates for the following\n";
                    for (Notification n : notes) {
                        msg = msg + n.toString() + "\n";
                    }
                    message.setText(msg);
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, pass);
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                }
                return true;
            } catch (AddressException ex) {
                Logger.getLogger(NotifierImpl.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (MessagingException ex) {
                Logger.getLogger(NotifierImpl.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean addNotification(Notification note) {
        return notes.add(note);
    }

    @Override
    public boolean clearNotifications() {
        notes = new LinkedList<Notification>();
        return true;
    }
}
