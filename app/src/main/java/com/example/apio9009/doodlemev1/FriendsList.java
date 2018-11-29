package com.example.apio9009.doodlemev1;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendsList extends AppCompatActivity {

    ListView FriendsList;
    private EditText add;
    private EditText delete;
    private Button addButton;
    private Button deleteButton;
    ArrayList<String> arrFriend;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        FriendsList = (ListView) findViewById(R.id.List);
        add = (EditText) findViewById(R.id.addFriend);
        delete = (EditText) findViewById(R.id.deleteFriend);
        addButton = (Button) findViewById(R.id.add);
        deleteButton = (Button) findViewById(R.id.delete);
        arrFriend = new ArrayList<>();
        adapter = new ArrayAdapter<String>(FriendsList.this, R.layout.activity_friends_list);




        View.OnClickListener addlistener = new View.OnClickListener() {

            public void onClick(View view) {
                arrFriend.add(add.getText().toString());
                add.setText("");
                adapter.notifyDataSetChanged();


            }
        };

        addButton.setOnClickListener(addlistener);
        deleteButton.setOnClickListener(addlistener);

        FriendsList.setAdapter(adapter);


    }

}
