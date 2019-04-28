package com.bestify.fleet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WaitingToConfirmActivity extends AppCompatActivity {

    String activeUser = "";
    String currentMessage = "";
    String activeEmail = "";
    Integer forCheckingIfNewMessage = 0;
    ArrayList<String> messages = new ArrayList<>();
    boolean checkingFlag = true;

//    public void sendNtyuityuiotification(){
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//    }

    public void sendAnEmail (){
        Intent intent = new Intent(getApplicationContext(), BeforeEmailActivity.class);
        intent.putExtra("username", activeUser);
        intent.putExtra("message", currentMessage);
        intent.putExtra("email", activeEmail);
        startActivity(intent);
    }
    public void checkingForConfirmation(){
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

                        messages.clear();

                        for (ParseObject message : objects) {
                            String messageContent = message.getString("Confirmed");
                            Log.i(messageContent, messageContent);
                            messageContent = messageContent.trim();

                            messages.add(messageContent);
                        }
                        Log.i("SIZE", messages.get(messages.size()-1));
                        if (messages.get(messages.size()-1).equals("Yes")){
                            checkingFlag = false;
                            sendAnEmail();

                        }
                    }
                }
//                Integer x = messages.size();
//                if (!forCheckingIfNewMessage.equals(0)){
//                    if(!forCheckingIfNewMessage.equals(x)){
//                        sendNotification();
//                    }
//                }
//
//                forCheckingIfNewMessage = x;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_to_confirm);
        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        currentMessage = intent.getStringExtra("message");
        activeEmail = intent.getStringExtra("email");
        TextView textView = (TextView)findViewById(R.id.messageTextView);
        setTitle("Potwierdzenie");
        textView.setText(currentMessage);
        final Handler checkingForConfirmationHandler = new Handler();
        final Runnable run =  new Runnable() {

            @Override
            public void run() {
                if (checkingFlag == true) {
                    checkingForConfirmation();
                    checkingForConfirmationHandler.postDelayed(this, 10000);
                }
            }
        };
        checkingForConfirmationHandler.post(run);
    }

}
