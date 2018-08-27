package com.bielanski.whatsthis.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.ui.WikiActivity;

public class HistoryWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "HistoryWidgetProvider";

    public static final String ACTION_DATA_UPDATED = "com.bielanski.whatsthis.widget.ACTION_DATA_UPDATED";

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Log.v(TAG, "onReceive");

        if (ACTION_DATA_UPDATED.equals(intent.getAction())) {
            Log.v(TAG, "Widget ACTION_DATA_UPDATED");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "onUpdate");

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.history_widget_provider);
            Intent startActivityIntent = new Intent(context, WikiActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, startActivityPendingIntent);
            views.setEmptyView(R.id.widget_list, R.id.widget_empty);
            setRemoteAdapter(context, views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.v(TAG, "setRemoteAdapter");
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WikiRemoteViewsService.class));
    }
}

