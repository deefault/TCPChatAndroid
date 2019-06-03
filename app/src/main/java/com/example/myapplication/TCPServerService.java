package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerService extends Service {
    // Binder given to clients
    private final IBinder binder = (IBinder) new TCPServerBinder();
    public final int port;
    String address;
    ServerSocket server;
    Socket client = null;

    public TCPServerService(int port) {
        this.port = port;
        try {
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = server.getInetAddress().getHostAddress();

    }

    public boolean isClientConnected() { return client == null; }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        stop();
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TCPServerBinder extends Binder {
        TCPServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TCPServerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    /** method for clients */

    public void start() {
        if (server!=null){
            Runnable beginAcceptClient = new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = server.accept();
                        client = socket;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            beginAcceptClient.run();
        }

    }

    public void stop(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
