package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class TCPService extends Service {
    // Binder given to clients
    private final IBinder binder = new TCPServiceBinder();
    private Server server;
    private Client client;

    public boolean isServer;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public class TCPServiceBinder extends Binder {
        TCPService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TCPService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
