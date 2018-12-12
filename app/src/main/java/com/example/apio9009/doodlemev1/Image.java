package com.example.apio9009.doodlemev1;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Image {
    private int paintingID;
    private String gameName;
    private String ownerUserName;
    private String image;
    private String currentPlayerUserName;
    private int currentPlayerSpot;
    private ArrayList<String> players;
    private Boolean myTurn;
    private int rounds;

    public String getCurrentPlayerUserName() {
        return currentPlayerUserName;
    }

    public void setCurrentPlayerUserName(String currentPlayerUserName) {
        this.currentPlayerUserName = currentPlayerUserName;
    }

    public int getCurrentPlayerSpot() {
        return currentPlayerSpot;
    }

    public void setCurrentPlayerSpot(int currentPlayerSpot) {
        this.currentPlayerSpot = currentPlayerSpot;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getPaintingID() {
        return paintingID;
    }

    public void setPaintingID(int paintingID) {
        this.paintingID = paintingID;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Boolean getMyTurn() {
        return myTurn;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public Bitmap getBitmap(){
        byte [] encodeByte = Base64.decode(image, Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return b;
    }
}
