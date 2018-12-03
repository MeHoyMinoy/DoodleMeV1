package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Drawing extends AppCompatActivity {

    private static final String CHANNEL_ID = "DoodleMe";
    private CanvasView canvasView;
    boolean newDoodle;
    Bundle bundle;
    String stuff;
    String[] flist;
    Bitmap doodle;
    String doodleEnc;
    String currentPlayer;
    int cpSpot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                          //On create function
        //bundle stuff----------------------------------------------------------------------------\\
        super.onCreate(savedInstanceState);
        //Get the bundle
        bundle = getIntent().getExtras();
        //Extract the data…
        newDoodle = bundle.getBoolean("newDoodle");
        stuff = bundle.getString("GroupName");
        flist = bundle.getStringArray("FriendsList");

        if(!newDoodle) {
            doodleEnc = bundle.getString("encoded");
            byte[] byteArray = Base64.decode(doodleEnc, Base64.DEFAULT);
            doodle = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            currentPlayer = bundle.getString("currentPlayer");
            cpSpot = bundle.getInt("cpSpot");
            canvasView.setDoodle(doodle);
        }
        if(cpSpot < flist.length-1) {
            cpSpot++;
            currentPlayer = flist[cpSpot];
        }else{
            cpSpot = 0;
            currentPlayer = flist[cpSpot];
        }

        //end bundle stuff------------------------------------------------------------------------//


        //notification stuff----------------------------------------------------------------------\\
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.newdoodle)
                .setContentTitle("test")
                .setContentText(flist[0])
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
        //json conversion-------------------------------------------------------------------------\\
        Bitmap image = canvasView.getDoodle();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        System.out.print(encoded);
        JSONObject doodle = new JSONObject();
        try{
            doodle.put("image", encoded);
            doodle.put("players", flist);
            doodle.put("gameName", stuff);
            doodle.put("currentPlayerUserName", currentPlayer);
            doodle.put("currentPlayerSpot", cpSpot);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //end json conversion---------------------------------------------------------------------//
        Intent intent = new Intent(Drawing.this, HomePage.class);
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
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






}
