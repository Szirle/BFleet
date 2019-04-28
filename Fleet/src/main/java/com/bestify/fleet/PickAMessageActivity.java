package com.bestify.fleet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PickAMessageActivity extends AppCompatActivity {


    String reciver = "";
    String email = "";

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> contents = new ArrayList<>();


    public void messageChosen(View view){
        Intent intent = new Intent(getApplicationContext(), SendAMessageActivity.class);
        intent.putExtra("username", reciver);
        intent.putExtra("email", email);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_amessage);

        Intent intent = getIntent();
        reciver = intent.getStringExtra("username");
        email = intent.getStringExtra("email");

        setTitle("Wybierz wiadomość");
        ListView sendersListView = (ListView)findViewById(R.id.szablonListView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
        sendersListView.setAdapter(arrayAdapter);
        sendersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SendAMessageActivity.class);
                intent.putExtra("message", contents.get(i));
                intent.putExtra("name", names.get(i));
                intent.putExtra("username", reciver);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Szablon");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null && objects.size() > 0) {
                        for (ParseObject szablon : objects) {
                            names.add(szablon.getString("name"));
                            contents.add(szablon.getString("Content"));
                        }
                        arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
