package com.example.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends ServiceActivity {

    Toolbar myToolbar ;
    MessageAdapter messageAdapter;
    ListView messagesView;
    EditText messageText;
    Connection tcpConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        //setSupportActionBar(myToolbar);
        Intent intent = new Intent(this,TCPService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        //myToolbar = findViewById(R.id.my_toolbar);
        messagesView = findViewById(R.id.messages);
        messageText = findViewById(R.id.messageText);
        //myToolbar.setTitle("Chat with " + server.socket.getInetAddress().toString());
        messageAdapter = new MessageAdapter(this);
        messagesView.setAdapter(messageAdapter);
        messageText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        //messagesView.
        super.onResume();
    }

    public void onMessage(Message message){
        message.setDateTime();
        messageAdapter.add(message);
        // scroll the ListView to the last added element
        messagesView.setSelection(messagesView.getCount() - 1);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMessageClick(View view) {

        if (tcpConnection == null){
            if (service.isServer){
                tcpConnection = (Connection) service.getServer();
            }
            else {
                tcpConnection = (Connection) service.getClient();
            }
            tcpConnection.setChatActivity(this);
            tcpConnection.startGettingIncomingMessages();
        }
        else {
            sendMessage();
        }
    }

    private void sendMessage() {
        Message message = new Message(messageText.getText().toString(),true);
        tcpConnection.sendMessage(message.getText());
        onMessage(message);
        messageText.getText().clear();
    }
}
