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

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.sysolve.androidthings.simplepioex.MainActivity;

import java.io.IOException;

public class Blink extends Thread {
    private static final String TAG = MainActivity.class.getSimpleName();

    public boolean ledOn = false;

    public int blinkInterval_ms;

    public Gpio gpio;

    public Blink(Gpio mLedGpio, int blinkInterval_ms) throws Exception {
        this.blinkInterval_ms = blinkInterval_ms;
        this.gpio = mLedGpio;
        this.gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
    }

    public void close() {
        try {
            gpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {

        }
        gpio = null;
    }

    public void run() {
        while (gpio!=null) {
            ledOn = !ledOn;
            try {
                gpio.setValue(ledOn);
            } catch (IOException e) {
                Log.i(TAG, e.getMessage(), e);
            }

            try {
                Thread.sleep(blinkInterval_ms);
            } catch (Exception e) {
                Log.i(TAG, e.getMessage(), e);
                break;
            }
        }
    }
}
