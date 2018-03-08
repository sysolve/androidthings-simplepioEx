/*
 * @author Ray, ray@sysolve.com
 * Copyright 2018, Sysolve IoT Open Source
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sysolve.androidthings.simplepioex;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import com.sysolve.androidthings.utils.BoardSpec;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = com.sysolve.androidthings.simplepioex.MainActivity.class.getSimpleName();

    Blink blinkLedGoogleSample;
    Blink blinkAnotherLed;

    Gpio mButtonGpio;

    Speaker speaker;

    int buttonPressedTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager service = PeripheralManager.getInstance();

        try {
            //Get the LED Gpio pin like Google Android Things Sample Simple PIO
            Gpio gpio = service.openGpio(BoardSpec.getGoogleSampleLedGpioPin());
            //Let the Red LED blink every 0.3 sec., INITIALLY LED On
            blinkLedGoogleSample = new Blink(gpio, 300, true);
            blinkLedGoogleSample.start();

            //Get another Gpio pin, PIN_29 from the 40Pin-Connector
            Gpio anotherGpio = service.openGpio(BoardSpec.getInstance().getGpioPin(BoardSpec.PIN_29));
            //Let the Blue LED blink every 0.3 sec., INITIALLY LED Off
            blinkAnotherLed = new Blink(anotherGpio, 300, false);
            blinkAnotherLed.start();

            speaker = new Speaker(BoardSpec.getGoogleSampleSpeakerPwmPin());
            speaker.play(5);

            mButtonGpio = service.openGpio(BoardSpec.getGoogleSampleButtonGpioPin());
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    buttonPressedTimes++;
                    Log.i(TAG, "GPIO changed, button pressed "+ buttonPressedTimes);

                    speaker.play(5);

                    // Return true to continue listening to events
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //close all used Gpios
        if (blinkLedGoogleSample!=null) blinkLedGoogleSample.close();
        if (blinkAnotherLed!=null) blinkAnotherLed.close();

        if (speaker!=null) speaker.close();

        if (mButtonGpio!=null) try {
            mButtonGpio.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
