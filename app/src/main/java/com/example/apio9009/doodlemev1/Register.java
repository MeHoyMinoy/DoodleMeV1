package com.example.apio9009.doodlemev1;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText birthdate;
    private EditText question;
    private EditText answer;
    private TextView message;
    public String Fakeuser = "Username";                //used in if statement to compare with other usernames in the database so two cannot be the same
    public String slash = "/";
    Bundle bundle = new Bundle();                                                                   //Create the bundle


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.setUsername);
        password = findViewById(R.id.setPass);
        birthdate = findViewById(R.id.setBirth);
        question =(EditText)findViewById(R.id.setQuestion);
        answer =(EditText)findViewById(R.id.setAnswer);
        message = (TextView)findViewById(R.id.warning);
    }

    public void validate(View V) {
        String userName = username.getText().toString();
        String userPassword = password.getText().toString();
        String bDate = birthdate.getText().toString();
        String quest = question.getText().toString();
        String ans = answer.getText().toString();

        if (userName == null || userName.length() < 3 || userName.length() > 14) {
            message.setText("Invalid Length.  Username must be between 3 to 14 characters.  ");
        }
        else if (userName == Fakeuser){                                                                 //Replace fakeuser with a list of all pre exisiting usernames
            message.setText("Username is already taken.");
        }
        else if (userPassword == null || userPassword.length() < 3 || userPassword.length() > 14){
            message.setText("Invalid Length.  Username must be between 3 to 14 characters.  ");
        }
        else if (userName == userPassword){
            message.setText("Your username cannot be the same as your password.");
        }
        /*else if (  !(bDate.substring(0,1).matches("[0-9]+")) || !(bDate.substring(2,2) == slash) || !(bDate.substring(3,4).matches("[0-9]+")) || !(bDate.substring(5,5) == slash) || !(bDate.substring(6,9).matches("[0-9]+"))   ){                                                 //if the birthdate has anything but a number it alerts the user
            message.setText("Invalid Format.  Please use only numbers for your birthdate with slashes inbetween");
        }*/
        else if (quest.length() > 30){
            message.setText("Invalid Length.  Question should be less than 30 characters.  ");

        }
        else if (ans.length() > 20){
            message.setText("Invalid Length.  Answer should be less than 20 characters.  ");
        }
        else {

            //create bundle-----------------------------------------------------------------------\\
            Intent i = new Intent(Register.this, HomePage.class);
            String[] fList;
            String userID = username.toString();
            bundle.putString("UserID", userID);
            //Add the bundle to the intent
            i.putExtras(bundle);
            startActivity(i);
            //end bundle--------------------------------------------------------------------------//

          }



        }


    }

