package com.bielanski.whatsthis.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bielanski.whatsthis.R;

public class WikiSavedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.wiki_saved, Toast.LENGTH_LONG).show();
    }
}
