package com.simaskuprelis.kag_androidapp.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.simaskuprelis.kag_androidapp.BuildConfig;
import com.simaskuprelis.kag_androidapp.R;
import com.simaskuprelis.kag_androidapp.Utils;
import com.simaskuprelis.kag_androidapp.activity.NodePickActivity;
import com.simaskuprelis.kag_androidapp.entity.Node;


public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_NODE_ID = 0;
    private static final String PRIVACY_POLICY_URL = "http://bit.ly/2T4kNCG";

    private Preference idPref;

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

        Preference reportPref = findPreference(getString(R.string.pref_report));
        reportPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String email = getString(R.string.report_email);
                String subject = getString(R.string.report_subject);
                String body = getString(R.string.device) + ": " + Build.MANUFACTURER + " " + Build.MODEL + "\n"
                        + getString(R.string.app_ver) + ": " + BuildConfig.VERSION_NAME + "\n"
                        + getString(R.string.android_ver) + ": " + Build.VERSION.RELEASE + "\n"
                        + getString(R.string.issue) + ": ";
                String uri = "mailto:" + email
                        + "?subject=" + Uri.encode(subject)
                        + "&body=" + Uri.encode(body);

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse(uri));
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), getString(R.string.email_error),
                            Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        Preference policyPref = findPreference(getString(R.string.pref_policy));
        policyPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(PRIVACY_POLICY_URL));
                startActivity(i);
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
        FirebaseAnalytics.getInstance(getContext()).setCurrentScreen(getActivity(), name, name);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        String keyAlerts = getString(R.string.pref_alerts);
        String keyNews = getString(R.string.pref_news);
        if (key.equals(keyAlerts) || key.equals(keyNews)) {
            boolean value = sp.getBoolean(key, false);
            Utils.setSubscription(key, value);
        }
    }
}
