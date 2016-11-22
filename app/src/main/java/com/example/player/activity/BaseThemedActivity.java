package com.example.player.activity;

import com.afollestad.appthemeengine.ATEActivity;
import com.example.player.utils.Helpers;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by naman on 31/12/15.
 */
public class BaseThemedActivity extends ATEActivity {

    @Nullable
    @Override
    public String getATEKey() {
        return Helpers.getATEKey(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make volume keys change multimedia volume even if music is not playing now
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
}
