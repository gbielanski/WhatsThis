package com.bielanski.whatsthis.widget;

import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.database.WikiDatabase;
import com.bielanski.whatsthis.database.data.WikiEntity;

import java.util.List;

import timber.log.Timber;

import static com.bielanski.whatsthis.ui.HistoryActivity.WIKI_KEY;

public class WikiRemoteViewsService extends RemoteViewsService {
    final static String TAG = "WikiRemoteViewsService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            List<WikiEntity> mWikies = null;

            @Override
            public void onCreate() {
                // Nothing to do
                Timber.tag(TAG);
            }

            @Override
            public void onDataSetChanged() {
                Timber.d("onDataSetChanged");
                final long identityToken = Binder.clearCallingIdentity();
                mWikies = WikiDatabase.getInstance(WikiRemoteViewsService.this).wikiDao().getAllSavedWikies();
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                Timber.d("onDestroy");

                mWikies = null;
            }

            @Override
            public int getCount() {
                int count = mWikies == null ? 0 : mWikies.size();
                Timber.d("getCount %d", count);

                return count;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Timber.d("getViewAt");

                if (mWikies == null || position >= mWikies.size())
                    return null;

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_wiki_list_item);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(WIKI_KEY, mWikies.get(position));
                views.setOnClickFillInIntent(R.id.wiki_item, fillInIntent);

                String wikiTitle = mWikies.get(position).getTitle();
                Timber.d("getViewAt %s", wikiTitle);
                views.setTextViewText(R.id.wiki_item, wikiTitle);
                Timber.d("RemoteViews getViewAt position %d %s", position, wikiTitle);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                Timber.d("getLoadingView");

                return new RemoteViews(getPackageName(), R.layout.widget_wiki_list_item);
            }

            @Override
            public int getViewTypeCount() {
                Timber.d("getViewTypeCount");

                return 1;
            }

            @Override
            public long getItemId(int position) {
                Timber.d("getItemId position %d", position);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                Timber.d("hasStableIds");

                return true;
            }
        };
    }

}
