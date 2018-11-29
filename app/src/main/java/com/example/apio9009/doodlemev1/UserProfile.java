package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {


    private EditText name;
    private TextView User;
    private TextView Birth;
    private Button logoutButton;
    private Button FriendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name =(EditText)findViewById(R.id.Nickname);
        logoutButton = (Button)findViewById(R.id.Logout);
        FriendsButton = (Button)findViewById(R.id.Friends);
        User =(TextView)findViewById(R.id.Username);
        Birth =(TextView)findViewById(R.id.Birthday);

        User.setText("Preloaded " );
        Birth.setText("Preloaded " );





        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutOfApp();
            }
        });

        FriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directedToFriendsList();
            }
        });

    }

    private void logoutOfApp() {
        finish();
        System.exit(0);
    }

    private void directedToFriendsList(){

        Intent intent = new Intent(UserProfile.this, FriendsList.class);  // how to go to next thing yayaya
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);

    }


}
