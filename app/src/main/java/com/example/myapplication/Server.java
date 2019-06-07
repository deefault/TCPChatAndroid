package com.example.myapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.net.*;
import java.net.DatagramSocket;




public class Server extends Connection{

    int port;
    String address;
    ServerSocket server;


    private class WaitForClientThread extends Thread {
        @Override
        public void run() {
            try {
                    socket = server.accept();
                    setIOStreams();
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.onClientReceived();
                        }
                    });
                    
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Server(int port,MainActivity mainActivity) throws InterruptedException {
        this.port = port;
        this.mainActivity = mainActivity;
        try {
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try(final DatagramSocket socket = new DatagramSocket()){
                    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                    address = socket.getLocalAddress().getHostAddress().toString();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        th.join();

    }

    @Override
    public void connect() {

        if (!isConnected() && server != null){
            Thread waitForClientThread = new Thread(new WaitForClientThread());
            waitForClientThread.start();
        }
    }


    public void stop(){
        try {
            server.close();
            server = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    //private BeginAccept(){


}