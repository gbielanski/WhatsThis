package com.bielanski.whatsthis.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.database.WikiDatabase;
import com.bielanski.whatsthis.database.data.WikiAsyncTaskLoader;
import com.bielanski.whatsthis.database.data.WikiEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WikiEntity>>,WikiAdapter.OnClickWikiHandler {
    private List<WikiEntity> mListOfWikiEntities;
    public static final int WIKI_HISTORY_LOADER_ID = 234;
    public static final String TAG = "HistoryActivity";
    @BindView(R.id.history_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.history_toolbar) Toolbar mToolbar;
    private WikiAdapter mAdapter;

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
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new WikiAdapter(new ArrayList<WikiEntity>(), this);
        mRecyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(WIKI_HISTORY_LOADER_ID, null, this);
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

    }
}
