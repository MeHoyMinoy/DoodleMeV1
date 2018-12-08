package com.example.apio9009.doodlemev1;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private static final String CHANNEL_ID = "DoodleMe";
    Bundle bundle;
    CountDownLatch latch;
//    HttpURLConnection conn;
    private int requestType = 0;
    InputStream inputStream;
    JSONArray jsonArray;
    String doodleEnc;
    String groupName;
    Bitmap doodle;
    String userID;
    int num;
    int numOfPaint;
    ArrayList<String> myList;
    ArrayList<Image> myFeed = new ArrayList<>();
    Object temp;
    HttpURLConnection connection;
    private String serverResult;
    JSONObject json;
    private HTTPAsyncTask friendsTask;
    private HTTPAsyncTask2 feedTask;

    int[] IMAGES = {R.drawable.ic_launcher_background};     //general population
    String[] GROUPNAME = {"Group1","Group2"};   //general population to see if it works

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        userID = bundle.getString("UserID");

        numOfPaint = bundle.getInt("NumOfPaint");///////////////////////////////////////////
        setContentView(R.layout.activity_home);
        friendsTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/GetFriendsList?user="+bundle.getString("UserID"));
//        feedTask = (HTTPAsyncTask2) new HTTPAsyncTask2().execute("http://10.0.2.2:8080/GetFeed?user="+bundle.getString("UserID"));

        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.newdoodle)
                .setContentTitle("test")
                .setContentText("test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);



        //feedTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/GetFeed?user="+bundle.getString("UserID"));


        //****************************************************************************************\\
        int notificationId = 1;
        notificationManager.notify(notificationId, mBuilder.build());*/



        //bundle.putStringArrayList("friendsList", friendslist);

        //********************************************************************************************\\


        //feed.

        //**************************************************************************************

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Activate();
        }

    private void Activate(){
        friendsTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/GetFriendsList?user="+bundle.getString("UserID"));
        feedTask = (HTTPAsyncTask2) new HTTPAsyncTask2().execute("http://10.0.2.2:8080/GetFeed?user="+bundle.getString("UserID"));


        bundle = getIntent().getExtras();
        String encoded = bundle.getString("Doodle");
        int currentPlayerSpot = bundle.getInt("cpSpot");
        String currentPlayer = bundle.getString("currentPlayer");
        groupName = bundle.getString("GroupName");
        ArrayList<String> friendGroup = bundle.getStringArrayList("FriendsList");
        num = bundle.getInt("NumOfPaint");/////////////////////////////////////////////////////////////////////////////////////////////////////////remove
        if(encoded != null) {
            byte[] byteArray = Base64.decode(encoded, Base64.DEFAULT);
            doodle = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
        }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myFeed.size();

        }

        @Override
        public Object getItem(int i) {
            return 1;

        }

        @Override
        public long getItemId(int i) {
            return 0;

        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item, viewGroup, false);
                ImageView imageView = (ImageView) view.findViewById(R.id.Image);
                TextView textViewGroup = (TextView) view.findViewById(R.id.textViewGroup);
                imageView.setImageBitmap(myFeed.get(i).getBitmap());
                textViewGroup.setText(myFeed.get(i).getGameName());


            return view;
        }

    }
    public void NewDoodle(View V) {
        Intent intent = new Intent(HomePage.this, NewDoodlePage.class);
        bundle.putString("UserID", userID);
        intent.putExtras(bundle);
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
        }

    public void goToProfile(View V) {
        Intent intent = new Intent(HomePage.this, UserProfile.class);
        bundle.putString("UserID", userID);
        intent.putExtras(bundle);
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
    }

    public void goToFriendsList(View V) {
        Intent intent = new Intent(HomePage.this, FriendsList.class);
        bundle.putString("UserID", userID);
        intent.putExtras(bundle);
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
        }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
                    try {
                        try {
                            return HttpGetFriends(urls[0]);
                            }
                        catch (JSONException e) {
                            e.printStackTrace();
                            return "Error!";
                            }
                        }
                    catch (IOException e) {
                        return "Unable to retrieve web page. URL may be invalid.";
                        }
                    }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // conResult.setText(result);
        }
    }    //Friends

    private String  HttpGetFriends(String myUrl) throws IOException, JSONException {

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
        replaceS = replaceS.replace("\"","");
        myList = new ArrayList<String>(Arrays.asList(replaceS.split(",")));
        in.close();
        bundle.putStringArrayList("friendsList", myList);
        return "lel";
    }


    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {  //Feed
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpGetFeed(urls[0]);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            }
            catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(!myFeed.isEmpty()) {
                ListView feed = findViewById(R.id.FeedListView);
                CustomAdapter customAdapter = new CustomAdapter();
                feed.setAdapter(customAdapter);
            }
        }
    }

    @SuppressLint("NewApi")
    private String  HttpGetFeed(String myUrl) throws IOException, JSONException {
        URL url = new URL(myUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }

        String replaceS = response.toString();
        replaceS = replaceS.replace("[","");
        replaceS = replaceS.replaceAll("\"paintingID\":", "");
        replaceS = replaceS.replaceAll("\"gameName\":", "");
        replaceS = replaceS.replaceAll("\"ownerUserName\":", "");
        replaceS = replaceS.replaceAll("\"image\":", "");
        replaceS = replaceS.replaceAll("\"currentPlayerUserName\":", "");
        replaceS = replaceS.replaceAll("\"currentPlayerSpot\":", "");
        replaceS = replaceS.replaceAll("\"players\":", "");
        replaceS = replaceS.replace("\"","");
        replaceS = replaceS.replace("]","");
        replaceS = replaceS.replaceAll("\\},","}");
        replaceS = replaceS.replace("{","");

        ArrayList<String> stringManip1 = new ArrayList<String>(Arrays.asList(replaceS.split("\\}")));
        ArrayList<String> stringManip2;
        Image temp;
        if(stringManip1.get(0) != "") {
            for (int i = 0; i < stringManip1.size(); i++) {
                stringManip2 = new ArrayList<String>(Arrays.asList(stringManip1.get(i).split(",")));
                temp = new Image();
                temp.setPaintingID(Integer.parseInt(stringManip2.get(0)));
                temp.setGameName(stringManip2.get(1));
                temp.setOwnerUserName(stringManip2.get(2));
                temp.setImage(stringManip2.get(3).replaceAll("\\\\n", ""));
                System.out.println(temp.getImage());
                temp.setCurrentPlayerUserName(stringManip2.get(4));
                temp.setCurrentPlayerSpot(Integer.parseInt(stringManip2.get(5)));
                temp.setPlayers(null);
                System.out.println(i);
                myFeed.add(temp);
            }
        }

        return "lel";
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

    private void setGetRequestContent(HttpURLConnection conn) throws IOException {
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
}
