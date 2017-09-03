package com.simaskuprelis.kag_androidapp.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.simaskuprelis.kag_androidapp.Utils;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.updatePollState(context);
    }
}
