package com.bielanski.whatsthis.database;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.bielanski.whatsthis.database.data.WikiEntity;
import com.bielanski.whatsthis.widget.HistoryWidgetProvider;
import java.util.List;

import timber.log.Timber;

public class WikiIntentService extends IntentService {

    private static final String ACTION_INSERT_WIKI = "com.bielanski.whatsthis.database.action.INSERT_WIKI";
    public static final String ACTION_WIKI_SAVED = "com.bielanski.whatsthis.database.action.WIKI_SAVED";
    public static final String ACTION_WIKI_DELETED = "com.bielanski.whatsthis.database.action.WIKI_DELETED";
    private static final String ACTION_DELETE_ALL_WIKI = "com.bielanski.whatsthis.database.action.DELETE_ALL_WIKI";
    private static final String ACTION_DELETE_WIKI = "com.bielanski.whatsthis.database.action.DELETE_WIKI";

    private static final String WIKI = "com.bielanski.whatsthis.database.extra.WIKI";
    private static final String WIKI_TITLE = "com.bielanski.whatsthis.database.extra.WIKI_TITLE";

    public WikiIntentService() {
        super("WikiIntentService");
    }

    public static void startActionInsertWiki(Context context, WikiEntity wiki) {
        Intent intent = new Intent(context, WikiIntentService.class);
        intent.setAction(ACTION_INSERT_WIKI);
        intent.putExtra(WIKI, wiki);
        context.startService(intent);
    }

    public static void startActionDeleteAllWiki(Context context) {
        Intent intent = new Intent(context, WikiIntentService.class);
        intent.setAction(ACTION_DELETE_ALL_WIKI);
        context.startService(intent);
    }

    public static void startActionDeleteWiki(Context context, String wiki_title) {
        Intent intent = new Intent(context, WikiIntentService.class);
        intent.setAction(ACTION_DELETE_WIKI);
        intent.putExtra(WIKI_TITLE, wiki_title);
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
                notifyWidgetDataUpdated();
                notifyUIWikiSaved();
                Timber.d("wiki inserted");

            } else if (ACTION_DELETE_ALL_WIKI.equals(action)) {
                WikiDatabase.getInstance(WikiIntentService.this).wikiDao().deleteAll();
                notifyWidgetDataUpdated();
                Timber.d("all wiki deleted");
            } else if (ACTION_DELETE_WIKI.equals(action)) {
                final String wikiTitle = intent.getStringExtra(WIKI_TITLE);
                final List<WikiEntity> allSavedWikies = WikiDatabase.getInstance(WikiIntentService.this).wikiDao().getAllSavedWikies();
                for (WikiEntity wiki : allSavedWikies)
                    Timber.d("Wiki in DB title %s", wiki.getTitle());
                Timber.d("Wiki to delete title %s", wikiTitle);
                WikiDatabase.getInstance(WikiIntentService.this).wikiDao().deleteWiki(wikiTitle);
                notifyWidgetDataUpdated();
                notifyUIWikiDeleted();
                Timber.d("wiki %s deleted", wikiTitle);
            } else
                Timber.d("action is not ACTION_INSERT_WIKI");

        } else
            Timber.d("intent is null");
    }

    private void notifyUIWikiDeleted() {
        Timber.d("notifyUIWikiDeleted");
        Intent wikiSavedIntent = new Intent(ACTION_WIKI_DELETED);
        sendBroadcast(wikiSavedIntent);
    }

    private void notifyUIWikiSaved() {
        Timber.d("notifyUIWikiSaved");
        Intent wikiSavedIntent = new Intent(ACTION_WIKI_SAVED);
        sendBroadcast(wikiSavedIntent);
    }

    private void notifyWidgetDataUpdated() {
        Timber.d("Widget notifyWidgetDataUpdated");
        Intent dataUpdatedIntent = new Intent(HistoryWidgetProvider.ACTION_DATA_UPDATED);
        sendBroadcast(dataUpdatedIntent);
    }
}
