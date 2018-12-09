package com.example.apio9009.doodlemev1;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Drawing extends AppCompatActivity {

    private static final String CHANNEL_ID = "DoodleMe";
    private CanvasView canvasView;
    boolean newDoodle;
    Bundle bundle;
    String stuff;
    ArrayList<String> flist;
    String userID;
    ArrayList<String> Painting = new ArrayList<String>();
    String doodleEnc;
    Bitmap doodle;
    String currentPlayer;
    CanvasView cView;
    String encoded;
    int cpSpot = 0;
    InputStream inputStream;
    HttpURLConnection conn;
    private HTTPAsyncTask mTask;
    int PaintingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                          //On create function
        super.onCreate(savedInstanceState);
//        ActivityCompat.requestPermissions(Drawing.this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                1);
        //Get the bundle
        bundle = getIntent().getExtras();
        //Extract the data…
        userID = bundle.getString("UserID");
        newDoodle = bundle.getBoolean("newDoodle");
        stuff = bundle.getString("GroupName");
        flist = bundle.getStringArrayList("FriendsList");
        String image = bundle.getString("imageString");
        cpSpot = bundle.getInt("cpSpot");
        PaintingID = bundle.getInt("paintingID");
        setContentView(R.layout.activity_drawing);
        ImageView in = (ImageView) findViewById(R.id.image3);

        canvasView = findViewById(R.id.canvas);
        if(!newDoodle){
            byte [] decodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap b = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
            Bitmap a = b.copy(Bitmap.Config.ARGB_8888, true);
            canvasView.setDoodle(a);
            }
    }

    public void send(View v){
        cView = findViewById(R.id.canvas);
        Canvas imageC = canvasView.getCanvas();
        cView.draw(imageC);
        Bitmap image = canvasView.getDoodle();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        if(newDoodle){
            mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/CreatePainting");
            }
        else
            mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/UpdatePainting");
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
            if(!newDoodle) {
                doodleJ.put("paintingID", PaintingID);
            }
            doodleJ.put("image", encoded);
            doodleJ.put("players", JArray);
            doodleJ.put("gameName", stuff);
            doodleJ.put("currentPlayerUserName", currentPlayer);
            doodleJ.put("currentPlayerSpot", cpSpot);
            doodleJ.put("ownerUserName", userID);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
//        serverResult = convertStreamToString(inputStream);
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
        Intent intent = new Intent(Drawing.this, HomePage.class);
        bundle.putString("UserID", userID);
        intent.putExtras(bundle);
        //intent is used to go from one activity to another like a source and a destination
        startActivity(intent);
    }

    //END SERVER COMMUNICATION


    //SaveImage
    public void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap);
        }else{
            //prompt the user or do something
        }
    }

    public void save(View v){
        cView = findViewById(R.id.canvas);
        Canvas imageC = canvasView.getCanvas();
        //cView.draw(imageC);
        Bitmap image = canvasView.getDoodle();
        saveImage(image);
    }

    private void saveImage(Bitmap finalBitmap) {
        //finalBitmap.eraseColor(Color.WHITE);
        Bitmap imageWithBG = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(),finalBitmap.getConfig());  // Create another image the same size
        imageWithBG.eraseColor(Color.WHITE);  // set its background to white, or whatever color you want
        Canvas canvas = new Canvas(imageWithBG);  // create a canvas to draw on the new image
        canvas.drawBitmap(finalBitmap, 0f, 0f, null); // draw old image on the background
        finalBitmap.recycle();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "DoodleMe"+ timeStamp +".png";
        MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap, fname , "Saved image from DoodleMe");
        /*String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root +"/DoodleMe");
        System.out.println(myDir);
        myDir.mkdirs();
        File file = new File(myDir, fname);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//
//        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //end save

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(Drawing.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }

}
