package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {


        private EditText name;
        private EditText password;
        private TextView info;
        private Button login;
        private Button reg;
        private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText)findViewById(R.id.etName);
        password =(EditText)findViewById(R.id.setPass);
        info = (TextView)findViewById(R.id.etInfo);
        login = (Button)findViewById(R.id.buttonLogin);
        reg = (Button)findViewById(R.id.registerB);

        info.setText("No of attempts remaining:   5");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), password.getText().toString());
            }
        });



    }

    public void register(View V){
        startActivity(new Intent(LoginActivity.this, Register.class));
        //intent is used to go from one activity to another like a source and a destination

    }

    private void validate(String userName, String userPassword) {
        try {
            URL url = new URL("localhost:8080/Login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject loginQuery = new JSONObject();
            loginQuery.put("userName", userName);
            loginQuery.put("password", userPassword);

            Log.i("JSON", loginQuery.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(loginQuery.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if ( (userName.equals("Admin")) && (userPassword.equals("1234")) ) {
            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            //intent is used to go from one activity to another like a source and a destination
            startActivity(intent);
        }
        else {
            counter--;

            info.setText("No of attempts remaining:   " + String.valueOf(counter));

            if (counter == 0) {
                login.setEnabled(false);
            }

        }


    }
}
