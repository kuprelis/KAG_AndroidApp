package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.fragment.TimetablePagerFragment;

public class StartActivity extends AppCompatActivity {
    private static final int REQUEST_USER_ID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        Utils.updatePollState(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains(TimetablePagerFragment.PREF_USER_ID)) {
            startMain();
        } else {
            Intent i = new Intent(this, OnboardingActivity.class);
            startActivityForResult(i, REQUEST_USER_ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_USER_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra(OnboardingActivity.EXTRA_USER_ID);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit()
                        .putString(TimetablePagerFragment.PREF_USER_ID, id)
                        .apply();
                startMain();
            } else finish();
        }
    }

    private void startMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
