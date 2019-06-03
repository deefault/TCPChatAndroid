package com.example.myapplication;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.net.*;
import java.util.EventListener;
import java.util.EventObject;
import java.net.DatagramSocket;


// Roughly analogous to .NET EventArgs
class ClickEvent extends EventObject {
    public ClickEvent(Object source) {
        super(source);
    }
}

// Roughly analogous to .NET delegate
interface clientListener extends EventListener {
    void clientRecieved(ClickEvent e);
}

public class Server {

    int port;
    String address;


    ServerSocket server;
    Socket client = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Server(int port) {
        this.port = port;
        try {
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            address = socket.getLocalAddress().getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }


    public void start() {
        try {
            Socket socket = server.accept();
            client = socket;


        }  catch (SocketException e) {
            if (server!=null) {
                try {
                    server.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            server = null;
        } catch (IOException e) {
            e.printStackTrace();
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

    public boolean isClientConnected() { return client == null; }

    //private BeginAccept(){


}