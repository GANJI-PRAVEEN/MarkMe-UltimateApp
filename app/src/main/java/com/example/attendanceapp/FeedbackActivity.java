package com.example.attendanceapp;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etName, etEmail, etSubject, etMessage;
    private RatingBar ratingBar;

    // TODO: put YOUR Gmail here
    private static final String FEEDBACK_EMAIL = "ganjipraveen444@gmail.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        ratingBar = findViewById(R.id.ratingBar);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> sendFeedbackEmail());
    }

    private void sendFeedbackEmail() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();
        int rating = Math.round(ratingBar.getRating());

        if (subject.isEmpty()) {
            etSubject.setError("Subject required");
            etSubject.requestFocus();
            return;
        }
        if (message.isEmpty()) {
            etMessage.setError("Please enter your message");
            etMessage.requestFocus();
            return;
        }
        if(name.isEmpty()){
            etName.setError("Please enter your name");
            etName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        else{
//            new SimpleMail().sendEmail(FEEDBACK_EMAIL, email, subject, name, message, new SimpleMail.MailcallBack() {
//                @Override
//                public void onSuccess() {
//                    runOnUiThread(()->
//                        Toast.makeText(getApplicationContext(), "Feedback sent successfully", Toast.LENGTH_SHORT).show()
//                    );
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Log.e("MailErro",e.toString());
//                    runOnUiThread(()->
//                        Toast.makeText(getApplicationContext(), "Sorry Unable to send feedback, Please try later"+e.toString(), Toast.LENGTH_SHORT).show()
//                    );
//                }
//            });
            redirectToGmail(FEEDBACK_EMAIL,email,subject,message,name);
//            Toast.makeText(this, "Feedback Sent Successfully", Toast.LENGTH_SHORT).show();
        }

        // App/device info to help you debug user issues
//        String appVersion = "unknown";
//        try {
//            appVersion = getPackageManager()
//                    .getPackageInfo(getPackageName(), 0).versionName;
//        } catch (Exception ignored) {}
//
//        String deviceInfo = "Device: " + Build.MANUFACTURER + " " + Build.MODEL
//                + "\nAndroid: " + Build.VERSION.RELEASE
//                + "\nApp version: " + appVersion;
//
//        String body =
//                "Name: " + (name.isEmpty() ? "—" : name) +
//                        "\nEmail: " + (email.isEmpty() ? "—" : email) +
//                        "\nRating: " + rating + "/5" +
//                        "\n\nMessage:\n" + message +
//                        "\n\n---\n" + deviceInfo;
//
//        // Use ACTION_SENDTO with mailto: so only email apps are shown
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("mailto:" + FEEDBACK_EMAIL));
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL});
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, body);
//
//        try {
//            startActivity(Intent.createChooser(intent, "Send feedback with"));
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No email app found", Toast.LENGTH_LONG).show();
//        }
    }
    public void redirectToGmail(String FEEDBACK_EMAIL,String userEmail,String subject,String msg,String name){
        Toast.makeText(this, "redirecting to gmail..", Toast.LENGTH_SHORT).show();
        // Call this method when user clicks feedback
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL}); // your feedback inbox
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // Include user's email inside the message body
        String finalMessage = "From: " + userEmail + "\n\n" + msg;
        intent.putExtra(Intent.EXTRA_TEXT, finalMessage);

        try {
            startActivity(Intent.createChooser(intent, "Send feedback via..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }

}
