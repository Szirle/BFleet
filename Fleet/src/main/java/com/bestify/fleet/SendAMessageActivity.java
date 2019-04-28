package com.bestify.fleet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SendAMessageActivity extends AppCompatActivity {

    String activeUser = "";
    String activeEmail = "";
    String szablonName = "";
    String szablonContent = "";

    ArrayList<String> messages = new ArrayList<>();



    public void sendChat(View view) {

        EditText chatEditText = (EditText) findViewById(R.id.chatEditText);

        ParseObject message = new ParseObject("Message");

        final String messageContent = chatEditText.getText().toString();

        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("recipient", activeUser);
        message.put("message", messageContent);
        message.put("Confirmed", "Not");

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    messages.add(messageContent);
                    Intent intent = new Intent(getApplicationContext(), WaitingToConfirmActivity.class);
                    intent.putExtra("username", activeUser);
                    intent.putExtra("message",messageContent);
                    intent.putExtra("email", activeEmail);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_a_message);
        EditText chatEditText = (EditText) findViewById(R.id.chatEditText);
        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        activeEmail = intent.getStringExtra("email");
        szablonContent = intent.getStringExtra("message");
        szablonName = intent.getStringExtra("name");
        chatEditText.setText(szablonContent, TextView.BufferType.EDITABLE);
        setTitle("Wiadomość do " + activeEmail);

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");

        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", activeUser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");

        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender", activeUser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {
//                        messages.clear();
                        for (ParseObject message : objects) {
                            String messageContent = message.getString("message");
                            messages.add(messageContent);
                        }
                    }
                }
            }
        });
    }
}
