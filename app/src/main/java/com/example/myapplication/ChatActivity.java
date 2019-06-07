package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        Intent intent = new Intent(this,TCPService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_chat);
        //setSupportActionBar(myToolbar);

        myToolbar = findViewById(R.id.my_toolbar);
        messagesView = findViewById(R.id.messages);
        messageText = findViewById(R.id.messageText);
        //myToolbar.setTitle("Chat with " + server.socket.getInetAddress().toString());
        messageAdapter = new MessageAdapter(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Thread.sleep(100);
        if (service.isServer){
            tcpConnection = (Connection) service.getServer();
        }
        else {
            tcpConnection = (Connection) service.getClient();
        }
        tcpConnection.setChatActivity(this);
        tcpConnection.startGettingIncomingMessages();
        myToolbar.setTitle("Chat with " + tcpConnection.socket.getInetAddress().toString());
    }

    public void onMessage(Message message){
        messageAdapter.add(message);
        // scroll the ListView to the last added element
        messagesView.setSelection(messagesView.getCount() - 1);
    }


    public void sendMessageClick(View view) {
        Message message = new Message(messageText.getText().toString(),true);
        tcpConnection.sendMessage(message.getText());
        onMessage(message);
        messageText.clearComposingText();
        messageText.clearFocus();
    }
}
