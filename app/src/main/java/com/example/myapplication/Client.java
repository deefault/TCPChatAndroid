package com.example.myapplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client extends Connection{
    String serverAddress;
    int port;


    private class ServerConnectionThread extends Thread {
        @Override
        public void run() {

            try {
                socket.connect(new InetSocketAddress(serverAddress,port),4000);
            } catch (SocketTimeoutException e) {
                connectionTimeoutCallback();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket.isConnected()){
                setIOStreams();
                connectionCallback();
            }
            else {
                connectionTimeoutCallback();
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

    private void connectionCallback() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onServerConnected();
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
