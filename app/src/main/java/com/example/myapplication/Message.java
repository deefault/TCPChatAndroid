package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String text;
    private boolean fromCurrentUser;

    public Message(String text, boolean fromCurrentUser) {
        this.text = text;
        this.fromCurrentUser = fromCurrentUser;
    }

    public String getText() {
        return text;
    }

    public boolean isFromCurrentUser() {
        return fromCurrentUser;
    }
}

