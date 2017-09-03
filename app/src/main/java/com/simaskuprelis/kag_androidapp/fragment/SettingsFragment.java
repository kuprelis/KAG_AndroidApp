package com.simaskuprelis.kag_androidapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.simaskuprelis.kag_androidapp.BuildConfig;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.NodePickActivity;
import com.simaskuprelis.kag_androidapp.entity.Node;


public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_NODE_ID = 0;

    private static final String PARAM_VALUE = "value";

    private Preference idPref;
    private FirebaseAnalytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        findPreference(getString(R.string.pref_app_ver)).setSummary(BuildConfig.VERSION_NAME);

        idPref = findPreference(getString(R.string.pref_user_id));
        idPref.setSummary(sp.getString(getString(R.string.pref_user_name), null));
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
            Node n = data.getParcelableExtra(NodePickActivity.RESULT_NODE);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            sp.edit()
                    .putString(getString(R.string.pref_user_id), n.getId())
                    .putString(getString(R.string.pref_user_name), n.getName())
                    .apply();
            idPref.setSummary(n.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        String name = getClass().getSimpleName();
        analytics.setCurrentScreen(getActivity(), name, name);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        String keyInterval = getString(R.string.pref_poll_interval);
        String keyImportant = getString(R.string.pref_notify_important);
        String keyNews = getString(R.string.pref_notify_news);
        if (key.equals(keyInterval) || key.equals(keyImportant) || key.equals(keyNews)) {
            Utils.updatePollState(getContext());

            Bundle b = new Bundle();
            if (key.equals(keyInterval)) {
                b.putString(PARAM_VALUE, sp.getString(key, null));

            } else if (key.equals(keyImportant)) {
                b.putBoolean(PARAM_VALUE, sp.getBoolean(key, true));

            } else if (key.equals(keyNews)) {
                b.putBoolean(PARAM_VALUE, sp.getBoolean(key, false));
            }
            analytics.logEvent(key, b);
        }
    }
}
