package com.simaskuprelis.kag_androidapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.entity.Node;

public class StartActivity extends AppCompatActivity {
    private static final int REQUEST_NODE_ID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.contains(getString(R.string.pref_user_id))) {
            startMain();
        } else {
            Intent i = new Intent(this, NodePickActivity.class);
            i.putExtra(NodePickActivity.EXTRA_UP_NAV, false);
            startActivityForResult(i, REQUEST_NODE_ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NODE_ID) {
            if (resultCode == RESULT_OK) {
                Node n = data.getParcelableExtra(NodePickActivity.RESULT_NODE);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit()
                        .putString(getString(R.string.pref_user_id), n.getId())
                        .putString(getString(R.string.pref_user_name), n.getName())
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
