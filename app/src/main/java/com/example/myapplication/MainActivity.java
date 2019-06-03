package com.example.myapplication;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapplication.TCPServerService.TCPServerBinder;

public class MainActivity extends AppCompatActivity {

    Server server;
    Thread serverThread;
    private boolean isServer;


    Button startServerButton;
    Button stopServerButton;
    TextView ipTextView;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipTextView = findViewById(R.id.ipTextView);
        startServerButton = findViewById(R.id.startServerButton);
        stopServerButton = findViewById(R.id.stopServerButton);


    }

    public void onClientRecieved() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startServerButtonClick(View view) {

        if (server == null){
            server = new Server(11111);
            ipTextView.setText("Server started at " + server.address + ":" + server.port);
            Runnable beginAcceptClient = new Runnable() {
                @Override
                public void run() {
                    server.start();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onClientRecieved();
                        }
                    });
                }

            };
            isServer = true;
            serverThread = new Thread(beginAcceptClient);
            serverThread.start();
            stopServerButton.setEnabled(true);
            view.setEnabled(false);
        }


    }

    public void stopServerButtonClick(View view) {
        if (server.isClientConnected()){
            server.stop();
        }
        else {
            server.stop();
            isServer = false;
            server = null;
            startServerButton.setEnabled(true);
            view.setEnabled(false);
        }
    }
}

