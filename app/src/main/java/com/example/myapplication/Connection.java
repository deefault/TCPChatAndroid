package com.example.myapplication;
import android.content.Intent;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public abstract class Connection {
    // client or server object
    protected MainActivity mainActivity;
    protected ChatActivity chatActivity;
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected InputThread inputThread;
    protected boolean readMessages=false;

    public boolean isConnected() { return socket == null; }

    public void connect(){
        if (socket != null){
            setIOStreams();
        }
    }

    protected void setIOStreams(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void stop();

    public String getMessage(){
        throw new RuntimeException();
    }

    public void sendMessage(final String text){
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                out.write(text);
                //onWriteCompleted();
            }
        });
    }

    public void startGettingIncomingMessages(){
        readMessages = true;
        if (inputThread == null){
            inputThread = new InputThread();
            inputThread.start();
        }
    }

    public void stopGettingIncomingMessages(){
        readMessages = false;
    }

    public class InputThread extends Thread {
        @Override
        public void run() {
            while (readMessages){
                try {
                    String line = in.readLine();
                    if (line != null && line != ""){
                        onDataReceived(line);
                    }
                    else {
                        Thread.yield();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
            super.interrupt();
        }
    }

    protected void onDataReceived(String data) {
        if (chatActivity!=null){
            final Message message = new Message(data,false);
            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatActivity.onMessage(message);
                }
            });
        }
    }



    public void setChatActivity(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }
}
