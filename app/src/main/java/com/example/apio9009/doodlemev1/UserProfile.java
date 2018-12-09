package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {


    private Button FriendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText name = (EditText) findViewById(R.id.Nickname);
        Button logoutButton = (Button) findViewById(R.id.Logout);
        TextView user = (TextView) findViewById(R.id.Username);
        TextView birth = (TextView) findViewById(R.id.Birthd);

        user.setText(" " );
        birth.setText(" " );





        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutOfApp();
            }
        });

        /*FriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directedToFriendsList();
            }
        });*/

    }

    private void logoutOfApp() {
        finish();
        Intent intent = new Intent(UserProfile.this, LoginActivity.class);  // how to go to next thing yayaya
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
    }

    /*private void directedToFriendsList(){

        Intent intent = new Intent(UserProfile.this, FriendsList.class);  // how to go to next thing yayaya
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);

    }*/


}
