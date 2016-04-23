package com.psa.rxlightstreamer.sample.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.psa.rxlightstreamer.sample.R;

/**
 * <p>Provide a way to set the data to connect to LightStreamer.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
