package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class FriendsList extends AppCompatActivity {
    private EditText groupName;                                                                     //initialize groupName
    private TextView friendS;                                                                     //initialize friendS
    private TextView fList;                                                                         //initialize fList
    public List<String> gList;                                                                      //initialize gList, this is the list of people in the group.
    String userID;
    String serverResult;
    Bundle bundle = new Bundle();
    HttpURLConnection conn;
    CountDownLatch latch;
    private HTTPAsyncTask mTask;
    private ArrayList<String> FriendSearch;//Create the bundle
    InputStream inputStream;

    protected void onCreate(Bundle savedInstanceState) {                                            //On create function
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        setContentView(R.layout.activity_friends_list);
        FriendSearch = bundle.getStringArrayList("friendsList");
        friendS = findViewById(R.id.SetGroupName);
        fList = findViewById(R.id.groupList);
        gList =  new ArrayList<String>();
        userID = bundle.getString("UserID");
        gList.add(bundle.getString("UserID"));                                                                   //adds current user to list. replace this with current user. right now it is defualted to admin


        for(int i = 0; i < FriendSearch.size(); i++){
            fList.append(FriendSearch.get(i) + "\n");
            gList.add(FriendSearch.get(i) + "\n");
            }
    }

    public void addF(View V){
        /*String aFriend = friendS.getText().toString();
        if(!aFriend.isEmpty()) {
            if(!gList.contains(aFriend)){
                fList.append(aFriend + "\n");
                gList.add(aFriend);}
        }*/

        serverResult = null;
        latch = new CountDownLatch(1);
        mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/AddFriend");
        waitForResponse();
        mTask.cancel(true);
    }

    /*public void removeF(View V){
        String rFriend = friendS.getText().toString();
        if(gList.contains(rFriend)){
            gList.remove(rFriend);
        }
        String[] gArray = gList.toArray(new String[gList.size()]);
        //fList.setText(" ");
        for(int i = 1; i < gArray.length; i++){
            fList.append(gArray[i] + "\n");
        }

    }*/

    //SERVE COMMUNICATION
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
            //conResult.setText(result);
        }
    }

    public void waitForResponse() {
        while(serverResult == null) {
            try {
                boolean result = latch.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // check result and react correspondingly
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("userName", userID);
        jsonObject.accumulate("friendUserName", friendS.getText().toString());

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
