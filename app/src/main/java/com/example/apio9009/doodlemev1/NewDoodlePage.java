package com.example.apio9009.doodlemev1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewDoodlePage extends AppCompatActivity {
    private EditText groupName;                                                                     //initialize groupName
    private SearchView friendS;                                                                     //initialize friendS
    private TextView fList;                                                                         //initialize fList
    public List<String> gList;                                                                      //initialize gList, this is the list of people in the group.
    Bundle bundle = new Bundle();                                                                   //Create the bundle

    protected void onCreate(Bundle savedInstanceState) {                                            //On create function
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdoodle);
        friendS = findViewById(R.id.friendSearch);
        fList = findViewById(R.id.groupList);
        gList =  new ArrayList<String>();
        gList.add("AdminUserID");                                                                   //adds current user to list. replace this with current user. right now it is defualted to admin
    }


    public void Start(View V){

        Intent i = new Intent(NewDoodlePage.this, Drawing.class);
        String[] fList;
        groupName = (EditText)findViewById(R.id.SetGroupName);
        String gName = groupName.getText().toString();
        String[] gArray = gList.toArray(new String[gList.size()]);
        bundle.putString("GroupName", gName);
        bundle.putStringArray("FriendsList",gArray);
        //Add the bundle to the intent
        i.putExtras(bundle);
        startActivity(i);
    }

    public void addF(View V){
        String aFriend = friendS.getQuery().toString();
        if(!aFriend.isEmpty()) {
            if(!gList.contains(aFriend)){
            fList.append(aFriend + "\n ");
            gList.add(aFriend);}
        }
    }

    public void removeF(View V){
        String rFriend = friendS.getQuery().toString();
        if(gList.contains(rFriend)){
            gList.remove(rFriend);
        }
        String[] gArray = gList.toArray(new String[gList.size()]);
        fList.setText(" ");
        for(int i = 1; i < gArray.length; i++){
            fList.append(gArray[i] + "\n ");
        }

    }

}
