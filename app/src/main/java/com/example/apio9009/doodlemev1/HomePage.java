package com.example.apio9009.doodlemev1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import org.json.JSONArray;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class HomePage extends AppCompatActivity {
    private static final String CHANNEL_ID = "DoodleMe";
    Bundle bundle;
    CountDownLatch latch;
//    HttpURLConnection conn;
    private int requestType = 0;
    InputStream inputStream;
    JSONArray jsonArray;
    String userID;
    ArrayList<String> myList;
    HttpURLConnection connection;
    private String serverResult;
    private ArrayList<String> friendslist = new ArrayList<String>();
    JSONObject json;
    private HTTPAsyncTask mTask;

    int[] IMAGES = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};     //general population
    String[] GROUPNAME = {"Group1", "Group2"};   //general population to see if it works

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        latch = new CountDownLatch(1);
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        userID = bundle.getString("UserID");
        setContentView(R.layout.activity_home);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.newdoodle)
                .setContentTitle("test")
                .setContentText("test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/GetFriendsList?user="+bundle.getString("UserID"));
        waitForResponse();


        //****************************************************************************************\\
        int notificationId = 1;
        notificationManager.notify(notificationId, mBuilder.build());



        //bundle.putStringArrayList("friendsList", friendslist);

        //********************************************************************************************\\

        ListView feed = (ListView) findViewById(R.id.FeedListView);
        CustomAdapter customAdapter = new CustomAdapter();
        feed.setAdapter(customAdapter);

        //**************************************************************************************

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return IMAGES.length;

        }

        @Override
        public Object getItem(int i) {
            return null;

        }

        @Override
        public long getItemId(int i) {
            return 0;

        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.Image);
            TextView textViewGroup = (TextView) view.findViewById(R.id.textViewGroup);

            imageView.setImageResource(IMAGES[i]);
            textViewGroup.setText(GROUPNAME[i]);

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
        startActivity(intent);    }

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

    public void waitForResponse() {
        try {
            boolean result = latch.await(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // check result and react correspondingly
    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpGet(urls[0]);
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
           // conResult.setText(result);
        }
    }

    private String HttpGet(String myUrl) throws IOException, JSONException {

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
                if(requestType == 1) {
                    sb.append((line));
                }else if(requestType == 2){
                    sb.append((line)+"\n");
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
}
