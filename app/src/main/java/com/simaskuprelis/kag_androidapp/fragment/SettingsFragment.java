package com.simaskuprelis.kag_androidapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.simaskuprelis.kag_androidapp.BuildConfig;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.NodePickActivity;
import com.simaskuprelis.kag_androidapp.api.FirebaseDatabaseApi;
import com.simaskuprelis.kag_androidapp.api.FirebaseListener;
import com.simaskuprelis.kag_androidapp.entity.Node;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_NODE_ID = 0;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        findPreference(getString(R.string.pref_app_ver)).setSummary(BuildConfig.VERSION_NAME);

        updateIdPref();
        Preference idPref = findPreference(getString(R.string.pref_user_id));
        idPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getContext(), NodePickActivity.class);
                startActivityForResult(i, REQUEST_NODE_ID);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_NODE_ID) {
            String s = data.getStringExtra(NodePickActivity.RESULT_NODE_ID);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            sp.edit().putString(getString(R.string.pref_user_id), s).apply();
            updateIdPref();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_poll_interval))
                || key.equals(getString(R.string.pref_notify_important))) {
            Utils.updatePollState(getContext());
        }
    }

    private void updateIdPref() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String id = getString(R.string.pref_user_id);
        String val = sp.getString(id, null);
        if (val == null) return;
        FirebaseDatabaseApi.getNode(val, new FirebaseListener<Node>() {
            @Override
            public void onLoad(Node obj) {
                Preference p = findPreference(id);
                p.setSummary(obj.getName());
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }
}
