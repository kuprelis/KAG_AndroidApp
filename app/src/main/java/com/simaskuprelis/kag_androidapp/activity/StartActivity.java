package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;

public class StartActivity extends AppCompatActivity {
    private static final int REQUEST_USER_ID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        Utils.updatePollState(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains(getString(R.string.pref_user_id))) {
            startMain();
        } else {
            Intent i = new Intent(this, NodePickActivity.class);
            startActivityForResult(i, REQUEST_USER_ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_USER_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra(NodePickActivity.EXTRA_USER_ID);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit()
                        .putString(getString(R.string.pref_user_id), id)
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
