package com.bielanski.whatsthis.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class WikiDeletedBroadcastReceiver extends BroadcastReceiver {

    public WikiDeletedBroadcastReceiver() {
        super();
        Timber.tag("WikiDeletedBroadcastReceiver");
    }

    private WikiDeleted mWikiDeleted;

    public interface WikiDeleted {
        void onWikiDeleted();
    }

    public void setWikiDeletedCallback(WikiDeleted wikiDeleted) {
        Timber.d("setWikiDeletedCallback");
        mWikiDeleted = wikiDeleted;
        if (mWikiDeleted == null)
            Timber.d("setWikiDeletedCallback is null");
        else
            mWikiDeleted.onWikiDeleted();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mWikiDeleted == null)
            Timber.d("onReceive is null");
        else {
            Timber.d("onReceive is not null");
            mWikiDeleted.onWikiDeleted();
        }
    }
}
