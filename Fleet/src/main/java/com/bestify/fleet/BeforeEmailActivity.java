package com.bestify.fleet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bestify.fleet.R;

public class BeforeEmailActivity extends AppCompatActivity {
    String activeUser = "";
    String currentMessage = "";
    String activeEmail = "";


    public void sendEmail (View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { activeEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Potrącenie");
        intent.putExtra(Intent.EXTRA_TEXT, currentMessage + "\nPotwierdzono przez " + activeEmail);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    public void backToHome (View view){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_email);
        TextView textView = (TextView) findViewById(R.id.textView5);

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        currentMessage = intent.getStringExtra("message");
        activeEmail = intent.getStringExtra("email");
        textView.setText(currentMessage);
    }
}
