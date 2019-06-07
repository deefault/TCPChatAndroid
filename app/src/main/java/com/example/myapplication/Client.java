package com.example.myapplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Connection{
    String serverAddress;
    int port;


    private class ServerConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(serverAddress,port),4000);
                if (socket.isConnected()){
                    setIOStreams();
                }
                else {
                    connectionTimeoutCallback();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectionTimeoutCallback() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onServerConnectionTimeout();
            }
        });
    }

    public Client(String address, int port, MainActivity activity){
        this.serverAddress = address;
        this.port = port;
        this.mainActivity = activity;
        socket = new Socket();
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    @Override
    public void connect(){
        if (!isConnected()){
            Thread serverConnectionThread = new Thread(new ServerConnectionThread());
            serverConnectionThread.start();
            try {
                serverConnectionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.onServerConnected();
                }
            });
        }
    }

    public void stop(){
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
