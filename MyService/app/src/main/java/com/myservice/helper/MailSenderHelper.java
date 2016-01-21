package com.myservice.helper;

import android.content.Context;

import com.myservice.R;
import com.myservice.model.Preferences;
import com.myservice.utils.Constants;
import com.myservice.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by AlexGP on 21/01/2016.
 */
public class MailSenderHelper {


    private static MailSenderHelper mMailSenderHelper;

    private MailSenderHelper() {
    }

    public static MailSenderHelper getInstance(){
        if(mMailSenderHelper == null){
            mMailSenderHelper = new MailSenderHelper();
        }

        return mMailSenderHelper;
    }

    private Session createSessionObject(final Context context) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(context.getString(R.string.forgot_email_sender),
                                                  context.getString(R.string.forgot_email_passwsd));
            }
        });
    }

    private Message createMessage(String email, String subject, String messageBody, Context context) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(createSessionObject(context));
        // TODO change for real values
        message.setFrom(new InternetAddress(context.getString(R.string.forgot_email_sender),
                                            context.getString(R.string.app_name)));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    public void sendResetPasswdMail(String email, Context context) throws Throwable {
        String subject = context.getString(R.string.forgot_reset_passwd_email_subject);
        String token  = Utils.generateRandomToken();
        String messageBody = context.getString(R.string.forgot_reset_passwd_email_msg) + " " + token;
        Preferences.getPreferences(context).editPreference(Constants.PREF_RESET_PASSWD_TOKEN,token);
        Message msg = createMessage(email,subject, messageBody, context);
        Transport.send(msg);
    }

    public void sendNewPasswdMail(String email, String newPasswd, Context context) throws Throwable{
        String subject = context.getString(R.string.forgot_new_passwd_email_msg);
        String messageBody = context.getString(R.string.forgot_new_passwd_email_subject) + " " + newPasswd;
        Message msg = createMessage(email,subject,messageBody, context);
        Transport.send(msg);
    }
}