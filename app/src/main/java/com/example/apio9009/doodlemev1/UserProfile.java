package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class UserProfile extends AppCompatActivity {


    private Button FriendsButton;
    private Bundle bundle;
    private String birthd;
    private String nickn;
    private String userID;
    InputStream inputStream;
    HttpURLConnection conn;
    private String serverResult;
    private HTTPAsyncTask mTask;
    private HTTPAsyncTask2 mTask2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        userID = bundle.getString("UserID");
        setContentView(R.layout.activity_profile);

        EditText name = (EditText) findViewById(R.id.Nickname);
        Button logoutButton = (Button) findViewById(R.id.Logout);
        Button saveButton = (Button) findViewById(R.id.Save);
        TextView user = (TextView) findViewById(R.id.Username);
        TextView birth = (TextView) findViewById(R.id.birthday);
        nickn = name.getText().toString();
        mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/GetUserData?user=" + userID);

        user.setText(userID);



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutOfApp();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void logoutOfApp() {
        finish();
        Intent intent = new Intent(UserProfile.this, LoginActivity.class);  // how to go to next thing yayaya
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
    }

    private void saveData() {
        EditText name = (EditText) findViewById(R.id.Nickname);
        nickn = name.getText().toString();
        mTask2 = (HTTPAsyncTask2) new HTTPAsyncTask2().execute("http://10.0.2.2:8080/UpdateProfile?user=" + userID);
    }



    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpGetData(urls[0]);
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
            //conResult.setText(result);
            TextView n = findViewById(R.id.birthday);
            EditText nickNameLine = (EditText) findViewById(R.id.Nickname);
            nickNameLine.setText(nickn);
            n.setText(birthd);
            System.out.println(nickn);
        }
    }

    private String HttpGetData(String myUrl) throws IOException, JSONException {
        URL url = new URL(myUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }

        String replaceS = response.toString();
        replaceS = replaceS.replace("[","");
        replaceS = replaceS.replace("]","");
        replaceS = replaceS.replace("}","");
        replaceS = replaceS.replaceAll("\"birthDate\":", "");
        replaceS = replaceS.replaceAll("\"nickName\":", "");
        replaceS = replaceS.replace("\"","");
        ArrayList<String> myList = new ArrayList<>(Arrays.asList(replaceS.split(",")));
        in.close();

        String fix = myList.get(4);
        nickn = myList.get(5);
        ArrayList<String> myBirthday = new ArrayList<>(Arrays.asList(fix.split("-")));

        birthd = myBirthday.get(1) + "-" + myBirthday.get(2) + "-" + myBirthday.get(0);
        return "lel";
    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpUpdateData(urls[0]);
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
            //conResult.setText(result);
            TextView birthdayLine = findViewById(R.id.birthday);
            EditText nickNameLine = (EditText) findViewById(R.id.Nickname);
            nickNameLine.setText(nickn);
            birthdayLine.setText(birthd);
        }
    }

    private String HttpUpdateData(String myUrl) throws IOException, JSONException {
        String result = "";

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
        JSONObject doodleJ = new JSONObject();
        try{
            doodleJ.put("userName", userID);
            doodleJ.put("nickName", nickn);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doodleJ;
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

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

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line));
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
//END SERVER COMMUNICATION

}
