package com.bielanski.whatsthis.database;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.bielanski.whatsthis.database.data.WikiEntity;
import com.bielanski.whatsthis.ui.WikiActivity;
import com.bielanski.whatsthis.widget.HistoryWidgetProvider;

import timber.log.Timber;

public class WikiIntentService extends IntentService {

    private static final String ACTION_INSERT_WIKI = "com.bielanski.whatsthis.database.action.INSERT_WIKI";
    private static final String ACTION_DELETE_ALL_WIKI = "com.bielanski.whatsthis.database.action.DELETE_ALL_WIKI";

    private static final String WIKI = "com.bielanski.whatsthis.database.extra.WIKI";

    public WikiIntentService() {
        super("WikiIntentService");
    }

    public static void startActionInsertWiki(Context context, WikiEntity wiki) {
        Intent intent = new Intent(context, WikiIntentService.class);
        intent.setAction(ACTION_INSERT_WIKI);
        intent.putExtra(WIKI, wiki);
        context.startService(intent);
    }

    public static void startActionDeleteAllWiki(Context context, WikiEntity wiki) {
        Intent intent = new Intent(context, WikiIntentService.class);
        intent.setAction(ACTION_DELETE_ALL_WIKI);
        intent.putExtra(WIKI, wiki);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.tag("WikiIntentService");
        Timber.d("onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT_WIKI.equals(action)) {
                final WikiEntity wiki = intent.getParcelableExtra(WIKI);
                WikiDatabase.getInstance(WikiIntentService.this).wikiDao().bulkInsert(wiki);
                Intent dataUpdatedIntent = new Intent(HistoryWidgetProvider.ACTION_DATA_UPDATED);
                sendBroadcast(dataUpdatedIntent);
                Timber.d("wiki inserted");

            }if (ACTION_DELETE_ALL_WIKI.equals(action)) {
                final WikiEntity wiki = intent.getParcelableExtra(WIKI);
                WikiDatabase.getInstance(WikiIntentService.this).wikiDao().deleteAll();
                Intent dataUpdatedIntent = new Intent(HistoryWidgetProvider.ACTION_DATA_UPDATED);
                sendBroadcast(dataUpdatedIntent);
                Timber.d("all wiki deleted");

            }else
                Timber.d("action is not ACTION_INSERT_WIKI");

        }else
            Timber.d("intent is null");
    }
}
