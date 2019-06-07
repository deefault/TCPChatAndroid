package com.example.myapplication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import com.example.myapplication.TCPService.TCPServiceBinder;

public class ServiceActivity extends AppCompatActivity {
    public TCPService service;
    protected boolean mBound = false;

    protected ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder _service) {
            TCPServiceBinder binder = (TCPServiceBinder)_service;
            service = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
