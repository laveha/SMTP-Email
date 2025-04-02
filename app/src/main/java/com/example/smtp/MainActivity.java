package com.example.smtp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MainActivity extends AppCompatActivity {

    private EditText emailTo, emailSubject, emailMessage;
    private Button sendButton;
    private String senderEmail = "isip_i.i.kovykov@mpt.ru";
    private String senderPassword = "@RussianImpireforever0079";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTo = findViewById(R.id.emailTo);
        emailSubject = findViewById(R.id.emailSubject);
        emailMessage = findViewById(R.id.emailMessage);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = emailTo.getText().toString().trim();
                String subject = emailSubject.getText().toString().trim();
                String message = emailMessage.getText().toString().trim();

                if (!recipient.isEmpty() && !subject.isEmpty() && !message.isEmpty()) {
                    new SendEmailTask().execute(recipient, subject, message);
                } else {
                    Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SendEmailTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String recipient = params[0];
            String subject = params[1];
            String messageText = params[2];

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);
                message.setText(messageText);

                Transport.send(message);
                return "Email отправлен успешно";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Ошибка при отправке email";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
