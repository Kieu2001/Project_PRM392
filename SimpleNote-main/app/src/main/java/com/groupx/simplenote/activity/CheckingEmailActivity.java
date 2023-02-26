package com.groupx.simplenote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;

import com.groupx.simplenote.R;

import java.util.Properties;

public class CheckingEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_email);
        String capcha = generateCaptchaText();
        Thread t = new Thread(new Runnable() { // worker thread hoặc background thread
            @Override
            public void run() {
                while (true) {
                    synchronized (this) {
                        try {
                            wait(3000);
                            Intent intent = new Intent(CheckingEmailActivity.this, ResetPasswordActivity.class);
                            startActivity(intent);
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        });
        t.start();
    }

//    public void buttonSendEmail(View view){
//
//        try {
//            String stringSenderEmail = "SenderEmail963@gmail.com";
//            String stringReceiverEmail = "receiveremail963@gmail.com";
//            String stringPasswordSenderEmail = "Test*123";
//
//            String stringHost = "smtp.gmail.com";
//
//            Properties properties = System.getProperties();
//
//            properties.put("mail.smtp.host", stringHost);
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.auth", "true");
//
//            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
//                }
//            });
//
//            MimeMessage mimeMessage = new MimeMessage(session);
//            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
//
//            mimeMessage.setSubject("Subject: Android App email");
//            mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World");
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(mimeMessage);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//
//        } catch (AddressException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }


    static final char[] chars = {'1', 'A', 'a', 'B', 'b', 'C',
            'c', '2', 'D', 'd', 'E', 'e', 'F', 'f', '3', 'G', 'g', 'H', 'h',
            'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', '4', 'M', 'm', 'N', 'n',
            'O', 'o', '5', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't',
            '6', '7', 'U', 'u', 'V', 'v', 'U', 'u', 'W', 'w', '8', 'X', 'x',
            'Y', 'y', 'Z', 'z', '9'};

    static String generateCaptchaText() {
        String randomStrValue = "";
        final int LENGTH = 6;
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int i = 0; i < LENGTH; i++) {
            index = (int) (Math.random() * (chars.length - 1));
            sb.append(chars[index]);
        }
        return sb.toString();
    }
}