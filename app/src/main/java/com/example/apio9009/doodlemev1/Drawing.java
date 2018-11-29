package com.example.apio9009.doodlemev1;

import android.graphics.Bitmap;
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
    Bundle bundle;
    String stuff;
    String[] flist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                          //On create function


        //bundle stuff----------------------------------------------------------------------------\\
        super.onCreate(savedInstanceState);
        //Get the bundle
        bundle = getIntent().getExtras();

        //Extract the data…
        stuff = bundle.getString("GroupName");
        flist = bundle.getStringArray("FriendsList");
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
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        JSONObject doodle = new JSONObject();
        JSONArray gMembers = new JSONArray();
        for(int i = 0; i < flist.length; i++){
            gMembers.put(flist[i]);
        }
        try{
            doodle.put("Doodle",encoded);
            doodle.put("gMembers", gMembers);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //end json conversion---------------------------------------------------------------------//
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
