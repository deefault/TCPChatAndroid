package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    private String text;
    private boolean fromCurrentUser;
    private Date dateTime;
    private static final DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public Message(String text, boolean fromCurrentUser) {
        this.text = text;
        this.fromCurrentUser = fromCurrentUser;
    }

    public Date getDateTime(){
        return dateTime;
    }

    public void setDateTime(Date date){
        this.dateTime = date;
    }

    public void setDateTime(){
        this.dateTime = new Date();
    }

    public String getTimeString(){
        return sdf.format(getDateTime());

    }

    public String getText() {
        return text;
    }

    public boolean isFromCurrentUser() {
        return fromCurrentUser;
    }
}

