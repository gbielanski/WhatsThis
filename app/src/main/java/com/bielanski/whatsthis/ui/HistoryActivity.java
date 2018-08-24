package com.bielanski.whatsthis.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.WhatIsThisApp;
import com.bielanski.whatsthis.database.WikiDatabase;
import com.bielanski.whatsthis.database.WikiIntentService;
import com.bielanski.whatsthis.database.data.WikiAsyncTaskLoader;
import com.bielanski.whatsthis.database.data.WikiEntity;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WikiEntity>>,WikiAdapter.OnClickWikiHandler, WikiDeletedBroadcastReceiver.WikiDeleted {
    public static final String WIKI_KEY = "WIKI_KEY";
    public static final String HISTORY_MAIN = "HistoryMain";
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<WikiEntity> mListOfWikiEntities;
    public static final int WIKI_HISTORY_LOADER_ID = 234;
    public static final String TAG = "HistoryActivity";
    @BindView(R.id.history_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.history_toolbar) Toolbar mToolbar;
    private WikiAdapter mAdapter;
    private WikiDeletedBroadcastReceiver mWikiDeletedBroadcastReceiver;
    private IntentFilter intentFilter;

    public static void startHistory(Context context){
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        mToolbar.setTitle(R.string.history_title);
        Timber.tag(TAG);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new WikiAdapter(new ArrayList<WikiEntity>(), this);
        mRecyclerView.setAdapter(mAdapter);
        mWikiDeletedBroadcastReceiver = new WikiDeletedBroadcastReceiver();
        mWikiDeletedBroadcastReceiver.setWikiDeletedCallback(this);
        intentFilter = new IntentFilter(WikiIntentService.ACTION_WIKI_DELETED);


        getSupportLoaderManager().initLoader(WIKI_HISTORY_LOADER_ID, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String wikiTitle = mAdapter.getWikiAtPosition(viewHolder.getAdapterPosition());
                WikiIntentService.startActionDeleteWiki(HistoryActivity.this, wikiTitle);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @NonNull
    @Override
    public Loader<List<WikiEntity>> onCreateLoader(int id, @Nullable Bundle args) {
        WikiAsyncTaskLoader asyncTaskLoader = new WikiAsyncTaskLoader(this);
        WikiDatabase database = WikiDatabase.getInstance(HistoryActivity.this);
        asyncTaskLoader.setDatabase(database);
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<WikiEntity>> loader, List<WikiEntity> listOfWikiEntities) {

        if(listOfWikiEntities == null || listOfWikiEntities.size() == 0){
            Timber.d("listOfWikiEntities is empty");
        }

        mListOfWikiEntities = listOfWikiEntities;

        for (WikiEntity entity : mListOfWikiEntities){
            Timber.d(entity.toString());
        }

        Timber.d("listOfWikiEntities size %d", mListOfWikiEntities.size());
        mAdapter.addWikiList(mListOfWikiEntities);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<WikiEntity>> loader) {

    }

    @Override
    public void wikiOnClick(int position) {
        final WikiEntity wikiEntity = mAdapter.getWikiList().get(position);
        Intent intent = new Intent(this, WikiActivity.class);
        intent.putExtra(WIKI_KEY, wikiEntity);
        startActivity(intent);
    }

    @Override
    public void onWikiDeleted() {
        getSupportLoaderManager().initLoader(WIKI_HISTORY_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, HISTORY_MAIN);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        LocalBroadcastManager.getInstance(this).registerReceiver(mWikiDeletedBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mWikiDeletedBroadcastReceiver);
    }
}
