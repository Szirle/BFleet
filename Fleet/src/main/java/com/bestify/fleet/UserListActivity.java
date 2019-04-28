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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    

    ArrayList<String> users = new ArrayList<>();
    ArrayList<String> userses = new ArrayList<>();
    ArrayList<String> listViewList = new ArrayList<>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("Wybierz użytkownika");
        
        users.clear();
        ListView sendersListView = (ListView)findViewById(R.id.userssListView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listViewList);
        sendersListView.setAdapter(arrayAdapter);
        sendersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), PickAMessageActivity.class);
                intent.putExtra("username", users.get(i));
                intent.putExtra("email", userses.get(i));
                startActivity(intent);
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            users.add(user.getUsername());
                            String userForForLoop  = user.getString("EmailS");
                            userses.add(userForForLoop);
                            listViewList.add(user.getUsername() + " " + userForForLoop);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
