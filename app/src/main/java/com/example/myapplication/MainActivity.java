package com.example.myapplication;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends ServiceActivity {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    Server server;
    Client client;
    Button startServerButton;
    Button stopServerButton;
    TextView ipTextView;
    EditText editTextPort;
    EditText editTextIp;
    private ProgressDialog progress;

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
        editTextPort = findViewById(R.id.editTextPort);
        editTextIp = findViewById(R.id.editTextIp);


    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent activityIntent = new Intent(this,TCPService.class);
        //startService(activityIntent);
        bindService(activityIntent, connection, BIND_AUTO_CREATE);
    }

    public void onClientReceived() {
        switchToChatActivity();
    }

    public void switchToChatActivity(){
        //unbindService(connection);
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startServerButtonClick(View view) {

        if (server == null){
            try {

                service.setServer(new Server(11111,this));
                server = service.getServer();
                service.isServer = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ipTextView.setText("Server started at " + server.address + ":" + server.port);
            server.connect();
            stopServerButton.setEnabled(true);
            view.setEnabled(false);
        }


    }

    public void stopServerButtonClick(View view) {
        if (server.isConnected()){
            server.stop();
        }
        else {
            server.stop();
            ipTextView.setText("Server stopped");
            server = null;
            startServerButton.setEnabled(true);
            view.setEnabled(false);
        }
        service.isServer = false;
    }


    public void onServerConnected() {
        progress.dismiss();
        service.setClient(client);
        switchToChatActivity();
    }

    public void onServerConnectionTimeout() {
        progress.dismiss();
       new AlertDialog.Builder(MainActivity.this)
                .setTitle("Connection error")
                .setMessage("Connection timeout or refused")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editTextPort.clearComposingText();
                        editTextIp.clearComposingText();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert).show();

    }

    public void connectClick(View view) {
        service.isServer = false;
        client = service.getClient();
        boolean error = false;
        if (client == null) {
            String ip;
            int port=0;
            try{
                port = parseInt(editTextPort.getText().toString());
            } catch (NumberFormatException e){
                error = true;
            }
            if (port<1024 || port > 65535 || error){
                editTextPort.setError("[1024-65535]");
            }
            ip = editTextIp.getText().toString();
            if (!Pattern.matches(IPADDRESS_PATTERN,ip)){
                error= true;
                editTextIp.setError("XXX.XXX.XXX.XXX [XXX = 0-255]");
            }
            if (!error){
                client = new Client(ip,port,this);
                client.connect();
                progress = new ProgressDialog(this);
                progress.setTitle("Connecting");
                progress.setMessage("Wait while connecting...");
                progress.setCancelable(false);
                progress.show();

            }
            else return;

        }

    }
}

