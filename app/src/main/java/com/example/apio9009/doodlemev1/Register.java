package com.example.apio9009.doodlemev1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private EditText username;
    private EditText password;
    public static EditText birthdate;
    private EditText firstName;
    private EditText lastName;
    private TextView message;
    public static String bDay;
    InputStream inputStream;
    HttpURLConnection conn;
    private String serverResult;
    public String Fakeuser = "Username";                //used in if statement to compare with other usernames in the database so two cannot be the same
    public String slash = "/";
    Bundle bundle = new Bundle();                                                                   //Create the bundle
    CountDownLatch latch;
    private HTTPAsyncTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.setUsername);
        password = findViewById(R.id.setPass);
        birthdate = findViewById(R.id.setBirth);
        birthdate.setEnabled(false);
        birthdate.setKeyListener(null);
        firstName = (EditText) findViewById(R.id.setFirstName);
        lastName = (EditText) findViewById(R.id.setLastName);
        message = (TextView) findViewById(R.id.warning);
    }

    public void validate(View V) {
        String userName = username.getText().toString();
        String userPassword = password.getText().toString();
        String bDate = birthdate.getText().toString();
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();

        if (userName == null || userName.length() < 3 || userName.length() > 14) {
            message.setText("Invalid Length.  Username must be between 3 to 14 characters.  ");
        } else if (userPassword == null || userPassword.length() < 3 || userPassword.length() > 14) {
            message.setText("Invalid Length.  Username must be between 3 to 14 characters.  ");
        } else if (userName == userPassword) {
            message.setText("Your username cannot be the same as your password.");
        }
        /*else if (  !(bDate.substring(0,1).matches("[0-9]+")) || !(bDate.substring(2,2) == slash) || !(bDate.substring(3,4).matches("[0-9]+")) || !(bDate.substring(5,5) == slash) || !(bDate.substring(6,9).matches("[0-9]+"))   ){                                                 //if the birthdate has anything but a number it alerts the user
            message.setText("Invalid Format.  Please use only numbers for your birthdate with slashes inbetween");
        }*/
        else if (fName.length() > 30) {
            message.setText("Invalid Length.  First name should be less than 30 characters.  ");

        } else if (lName.length() > 30) {
            message.setText("Invalid Length.  Last name should be less than 20 characters.  ");
        } else {
            serverResult = null;
            latch = new CountDownLatch(1);
            mTask = (HTTPAsyncTask) new HTTPAsyncTask().execute("http://10.0.2.2:8080/CreateProfile");

        }


    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            message.setText("Connected " + networkInfo.getTypeName());
            // change background color to red
            message.setTextColor(0xFF7CCC26);


        } else {
            // show "Not Connected"
            message.setText("Not Connected");
            // change background color to green
            message.setTextColor(0xFFFF0000);
        }

        return isConnected;
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
            if (serverResult.equals("-1")) {
                message.setText("That username is already taken.");
            } else if (serverResult.equals("1")) {
                //create bundle-----------------------------------------------------------------------\\
                Intent i = new Intent(Register.this, HomePage.class);
                String[] fList;
                String userID = username.getText().toString();
                bundle.putString("UserID", userID);
                //Add the bundle to the intent
                i.putExtras(bundle);
                startActivity(i);
                //end bundle--------------------------------------------------------------------------//
            }
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
        return conn.getResponseMessage() + "";

    }

    private JSONObject buildJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("userName", username.getText().toString());
        jsonObject.accumulate("password", password.getText().toString());
        jsonObject.accumulate("firstName", firstName.getText().toString());
        jsonObject.accumulate("lastName", lastName.getText().toString());
        jsonObject.accumulate("birthDate", birthdate.getText().toString());

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


    public void waitForResponse() {
        try {
            boolean result = latch.await(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // check result and react correspondingly
    }

    public void notifyOKCommandReceived() {
        latch.countDown();
    }






    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
            bDay = month+"-"+day+"-"+year;
            birthdate.setText(bDay);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
