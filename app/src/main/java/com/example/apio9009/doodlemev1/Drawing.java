package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Drawing extends AppCompatActivity {

    private static final String CHANNEL_ID = "DoodleMe";
    private CanvasView canvasView;
    boolean newDoodle;
    Bundle bundle;
    String stuff;
    ArrayList<String> flist;
    String userID;
    Bitmap doodle;
    ArrayList<String> Painting = new ArrayList<String>();
    String doodleEnc;
    String currentPlayer;
    int cpSpot = 0;
    InputStream inputStream;
    private String serverResult;
    HttpURLConnection conn;
    private HTTPAsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                          //On create function
        //bundle stuff----------------------------------------------------------------------------\\
        super.onCreate(savedInstanceState);
        //Get the bundle
        bundle = getIntent().getExtras();
        //Extract the data…
        userID = bundle.getString("UserID");
        newDoodle = bundle.getBoolean("newDoodle");
        stuff = bundle.getString("GroupName");
        flist = bundle.getStringArrayList("FriendsList");

        if(!newDoodle) {
            doodleEnc = bundle.getString("encoded");
            byte[] byteArray = Base64.decode(doodleEnc, Base64.DEFAULT);
            doodle = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

            currentPlayer = bundle.getString("currentPlayer");
            cpSpot = bundle.getInt("cpSpot");
            canvasView.setDoodle(doodle);
        }
        if(cpSpot < flist.size()-1) {
            cpSpot++;
            currentPlayer = flist.get(cpSpot);
        }else{
            cpSpot = 0;
            currentPlayer = flist.get(cpSpot);
        }

        //end bundle stuff------------------------------------------------------------------------//


        //notification stuff----------------------------------------------------------------------\\
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.newdoodle)
                .setContentTitle("test")
                .setContentText(flist.get(0))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        notificationManager.notify(notificationId, mBuilder.build());
        //end notification info-------------------------------------------------------------------//
        setContentView(R.layout.activity_drawing);
        canvasView = findViewById(R.id.canvas);
    }

    public void send(View v){
        serverResult = null;
        if(newDoodle){
            mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/CreatePainting");
        }else mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/UpdatePainting");
    }

    public void setBlack(View V){
        int co = Color.BLACK;
        canvasView.setColor(co);
    }

    public void setBlue(View V){
        int co = Color.BLUE;
        canvasView.setColor(co);
    }

    public void setRed(View V){
        int co = Color.RED;
        canvasView.setColor(co);
    }

    public void setYellow(View V){
        int co = Color.YELLOW;
        canvasView.setColor(co);
    }

    public void setGreen(View V){
        int co = Color.GREEN;
        canvasView.setColor(co);
    }

    public void setOrange(View V){
        int co = Color.rgb(255, 165, 0 );
        canvasView.setColor(co);
    }

    public void setGrey(View V){
        int co = Color.GRAY;
        canvasView.setColor(co);
    }

    public void setBrown(View V){
        int co = Color.rgb(165, 42, 42 );
        canvasView.setColor(co);
    }

    public void setPurple(View V){
        int co = Color.MAGENTA;
        canvasView.setColor(co);
    }

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
            next();
        }
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
        //json conversion-------------------------------------------------------------------------\\
        Bitmap image = canvasView.getDoodle();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        JSONArray JArray = new JSONArray(flist);
        JSONObject doodleJ = new JSONObject();
        try{
            doodleJ.put("image", encoded);
            doodleJ.put("players", JArray);
            doodleJ.put("gameName", stuff);
            doodleJ.put("currentPlayerUserName", currentPlayer);
            doodleJ.put("currentPlayerSpot", cpSpot);
            doodleJ.put("ownerUserName", flist.get(0));
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //end json conversion---------------------------------------------------------------------//
        System.out.print(doodleJ);
        return doodleJ;
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

    public void next(){
        if (serverResult!=null) {
            if (serverResult.equals("1")) {
                Intent intent = new Intent(Drawing.this, HomePage.class);
                bundle.putString("UserID", userID);
                intent.putExtras(bundle);
                //intent is used to go from one activity to another like a source and a destination
                startActivity(intent);
            } else {

            }
        }else{

        }


    }

    //END SERVER COMMUNICATION






}
