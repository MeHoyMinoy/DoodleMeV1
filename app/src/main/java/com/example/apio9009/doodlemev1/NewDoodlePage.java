package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class NewDoodlePage extends AppCompatActivity {
    private EditText groupName;                                                                     //initialize groupName
    private EditText roundNumber;
    private TextView friendS;                                                                     //initialize friendS
    private TextView fList;                                                                         //initialize fList
    public List<String> gList;                                                                      //initialize gList, this is the list of people in the group
    int rNumber;
    String userID;
    private ArrayList<String> FriendSearch;
    Bundle bundle = new Bundle();

    protected void onCreate(Bundle savedInstanceState) {                                            //On create function
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        userID = bundle.getString("UserID");

        setContentView(R.layout.activity_newdoodle);

        groupName = (EditText) findViewById(R.id.SetGroupName);
        friendS = findViewById(R.id.actv);
        roundNumber = (EditText) findViewById(R.id.SetRoundNumber);
        fList = findViewById(R.id.groupList);
        gList =  new ArrayList<String>();
        FriendSearch = bundle.getStringArrayList("friendsList");
        gList.add(bundle.getString("UserID"));                                                                   //adds current user to list. replace this with current user. right now it is defualted to admin

        AutoCompleteTextView editText = findViewById(R.id.actv);
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, FriendSearch);
        editText.setAdapter(adap);
    }



    public void Start(View V){
        if(gList.size()>1) {
            Intent i = new Intent(NewDoodlePage.this, Drawing.class);
            String[] fList;
            TextView sNum = findViewById(R.id.SetRoundNumber);
            int setNum = Integer.parseInt(sNum.getText().toString());
            int rNumber = Integer.parseInt(roundNumber.getText().toString());
            System.out.println(rNumber);
            String gName = groupName.getText().toString();


            String[] gArrayq = gList.toArray(new String[gList.size()]);
            ArrayList<String> gArray = new ArrayList<String>(Arrays.asList(gArrayq));
            bundle.putInt("NumOfPaint", 1);/////////////////////////////////////////////////////////////////////////////////////////////////////////remove
            bundle.putString("GroupName", gName);
            bundle.putStringArrayList("FriendsList", gArray);
            bundle.putBoolean("newDoodle", true);
            bundle.putInt("roundNumber", rNumber);
            //Add the bundle to the intent
            i.putExtras(bundle);
            finish();
            startActivity(i);
        }
    }

    public void addF(View V){
        String aFriend = friendS.getText().toString();
        if(!aFriend.isEmpty()) {
            if(!gList.contains(aFriend)){
            fList.append(aFriend + "\n");
            gList.add(aFriend);}
        }
        friendS.setText("");
    }

    public void removeF(View V){
        String rFriend = friendS.getText().toString();
        if(gList.contains(rFriend)){
            gList.remove(rFriend);
        }
        String[] gArray = gList.toArray(new String[gList.size()]);
        fList.setText(" ");
        for(int i = 1; i < gArray.length; i++){
            fList.append(gArray[i] + "\n");
        }
        friendS.setText("");

    }



}
