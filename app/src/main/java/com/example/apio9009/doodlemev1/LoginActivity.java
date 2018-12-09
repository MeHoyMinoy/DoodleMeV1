package com.example.apio9009.doodlemev1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

        private EditText name;
        private EditText password;
        private TextView info;
        private TextView appIsConnected;
        private TextView conResult;
        InputStream inputStream;
        HttpURLConnection conn;
        private String serverResult;
        private int counter = 5;
        private int requestType = 0;
        CountDownLatch latch;
        Bundle bundle = new Bundle();                                                                   //Create the bundle

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        name = (EditText)findViewById(R.id.etName);
        password =(EditText)findViewById(R.id.setPass);
        info = (TextView)findViewById(R.id.etInfo);
        appIsConnected = (TextView)findViewById(R.id.appIsConnected);
        conResult = (TextView)findViewById(R.id.resultTest);
        Button login = (Button) findViewById(R.id.buttonLogin);
        Button reg = (Button) findViewById(R.id.registerB);

        info.setText("No of attempts remaining:   5");
        checkNetworkConnection();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection())
                validate(name.getText().toString(), password.getText().toString());
            }
        });



    }

    public void register(View V){
        startActivity(new Intent(LoginActivity.this, Register.class));
        //intent is used to go from one activity to another like a source and a destination

    }

    private void validate(String userName, String userPassword) {
        requestType = 1;
        serverResult = null;
        latch = new CountDownLatch(1);
        HTTPAsyncTask mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/Login");
    }

    @SuppressLint("SetTextI18n")
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            appIsConnected.setText("Connected "+networkInfo.getTypeName());
            // change background color to red
            appIsConnected.setTextColor(0xFF7CCC26);


        } else {
            // show "Not Connected"
            appIsConnected.setText("Not Connected");
            // change background color to green
            appIsConnected.setTextColor(0xFFFF0000);
        }

        return isConnected;
    }

    //SERVE COMMUNICATION
    @SuppressLint("StaticFieldLeak")
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            conResult.setText(result);
            login();
        }
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buildJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }

    private JSONObject buildJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        if(requestType == 1) {
            jsonObject.accumulate("userName", name.getText().toString());
            jsonObject.accumulate("password", password.getText().toString());
        }

        return jsonObject;
    }
    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(LoginActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();

        inputStream = new BufferedInputStream(conn.getInputStream());
        serverResult = convertStreamToString(inputStream);
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if(requestType == 1) {
                    sb.append((line));
                }else if(requestType == 2){
                    sb.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @SuppressLint("SetTextI18n")
    public void login(){
        if (serverResult!=null) {
            if (serverResult.equals("1")) {
                Intent intent = new Intent(LoginActivity.this, HomePage.class);
                bundle.putInt("NumOfPaint", 0);/////////////////////////////////////////////////////////////////////////////////////////////////////////remove
                bundle.putString("UserID", name.getText().toString());
                intent.putExtras(bundle);
                //intent is used to go from one activity to another like a source and a destination
                startActivity(intent);
            } else {
                counter--;
                info.setText("No. of attempts remaining: " + counter);
            }
        }else{
            info.setText("Trouble communicating with the server. Please try again.");
        }


    }

    //END SERVER COMMUNICATION

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
