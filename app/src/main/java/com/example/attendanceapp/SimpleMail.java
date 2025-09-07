package com.example.attendanceapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SimpleMail {
    public interface MailcallBack{
        public void onSuccess();
        public void onFailure(Exception e);
    }

    /** CHANGE ACCORDINGLY **/
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static  String SMTP_AUTH_USER = "ganjipraveen444@gmail.com"; // your Gmail
    private static final String SMTP_AUTH_PWD  = "plie fjqb tomh bfwm"; // 16-char App password

    private static Message message;
    public static boolean sendEmail(String to, String fromEmail,String subject,String nameOfStudent, String msg,MailcallBack callBack) {
        if(!fromEmail.isEmpty()){
            SMTP_AUTH_USER = fromEmail;
        }
        boolean sent =false;
        String from = SMTP_AUTH_USER;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                    }
                });

        try {
            Log.e("ToEmail",to);
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(message);
                    if(callBack!=null) callBack.onSuccess();
                } catch (Exception e) {
                    if(callBack!=null) callBack.onFailure(e);
//                    Toast.makeText(context, "Sorrry unable to send Email", Toast.LENGTH_SHORT).show();
                    Log.e("Sorry","Unabe to send");
                    Log.e("ErrorAagayi",e.toString());
                }
            });


            thread.start();

        } catch (Exception e) {
            callBack.onFailure(e);
            Log.e("Sorry Unavel to send",e.toString());
            throw new RuntimeException(e);
        }
        return true;
    }
}
