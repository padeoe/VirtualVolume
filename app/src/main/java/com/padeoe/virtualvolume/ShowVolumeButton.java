package com.padeoe.virtualvolume;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

public class ShowVolumeButton extends Service {
    @Override
    public void onCreate(){
        createSmallWindow();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void createSmallWindow() {
        VolumButton.getInstance(this,getApplicationContext());
    }
}
