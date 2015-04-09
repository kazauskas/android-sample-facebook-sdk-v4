package com.kazauskas.facebook.android.globaltesting.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.kazauskas.facebook.android.globaltesting.R;


public class MainActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

    }
}
