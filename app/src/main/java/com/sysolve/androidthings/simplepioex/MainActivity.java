package com.sysolve.androidthings.simplepioex;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.sysolve.androidthings.simplepioex.Blink;
import com.sysolve.androidthings.utils.BoardSpec;

public class MainActivity extends Activity {
    private static final String TAG = com.sysolve.androidthings.simplepioex.MainActivity.class.getSimpleName();

    Blink blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();

        try {
            blink = new Blink(service.openGpio(BoardSpec.getLedGpioPin()), 1000);
            blink.start();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //close all used Gpios
        if (blink!=null) blink.close();
    }

}
