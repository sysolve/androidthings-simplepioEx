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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.sysolve.androidthings.utils.BoardSpec;

import java.io.IOException;

public class Speaker {
    private static final String TAG = Speaker.class.getSimpleName();

    String pwmPin;
    com.google.android.things.contrib.driver.pwmspeaker.Speaker mSpeaker;
    ValueAnimator slide;

    int timesLeft = 0;

    public Speaker(String pwmPin) {
        this.pwmPin = pwmPin;
        init();
    }

    /**
     * @param times  play times, -1 for loop
     */
    public void play(int times) {
        timesLeft = times;
        if (mSpeaker!=null && slide!=null) {
            slide.start();
            Log.i(TAG, "Speaker play, times left "+timesLeft);
        }
    }

    public void init() {
            try {
                mSpeaker = new com.google.android.things.contrib.driver.pwmspeaker.Speaker(pwmPin);
                slide = ValueAnimator.ofFloat(440, 440 * 4);
                slide.setDuration(50);
                slide.setRepeatCount(5);
                slide.setInterpolator(new LinearInterpolator());
                slide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            float v = (float) animation.getAnimatedValue();
                            mSpeaker.play(v);
                        } catch (IOException e) {
                            throw new RuntimeException("Error sliding speaker", e);
                        }
                    }
                });
                slide.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        try {
                            mSpeaker.stop();

                            if (mSpeaker!=null && timesLeft<0 || timesLeft>0) {
                                if (timesLeft>0) timesLeft--;
                                slide.start();
                                Log.i(TAG, "Speaker play, times left "+timesLeft);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error sliding speaker", e);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error initializing speaker", e);
            }
    }

    public void close() {
        try {
            mSpeaker.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {

        }
        mSpeaker = null;
    }
}
