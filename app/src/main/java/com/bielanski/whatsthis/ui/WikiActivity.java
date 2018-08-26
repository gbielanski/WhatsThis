package com.bielanski.whatsthis.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.database.WikiIntentService;
import com.bielanski.whatsthis.database.data.WikiEntity;
import com.bielanski.whatsthis.network.RequestInterface;
import com.bielanski.whatsthis.network.data.WikiInfo;
import com.bielanski.whatsthis.utils.ImageUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.bielanski.whatsthis.ui.HistoryActivity.WIKI_KEY;
import static com.bielanski.whatsthis.utils.ImageUtils.FILE_PATH_KEY;

public class WikiActivity extends AppCompatActivity {
    public static final String TAG = "WikiActivity";
    public static final String WIKIPEDIA_URL = "https://en.wikipedia.org/";
    public static final String WIKI_MAIN = "WikiMain";
    @BindView(R.id.wiki_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.wiki_image)
    ImageView mWikiImage;
    @BindView(R.id.wiki_title)
    TextView mWikiTitle;
    @BindView(R.id.wiki_description)
    TextView wikiDescription;
    @BindView(R.id.wiki_button_save)
    ImageButton mButtonSave;
    @BindView(R.id.wiki_button_close)
    ImageButton mButtonClose;
    @BindView(R.id.wiki_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.wiki_data_no_tv)
    TextView mNoDataTextView;

    private WikiSavedBroadcastReceiver mWikiSavedBroadcastReceiver;
    private IntentFilter intentFilter = new IntentFilter(WikiIntentService.ACTION_WIKI_SAVED);
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        ButterKnife.bind(this);
        Timber.tag(TAG);
        setSupportActionBar(mToolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final Intent intent = getIntent();
        String wikiItem = null;
        WikiEntity wikiEntity;

        if (intent != null) {
            wikiEntity = intent.getParcelableExtra(WIKI_KEY);
            if (wikiEntity != null) {
                mWikiTitle.setText(wikiEntity.getTitle());
                wikiDescription.setText(wikiEntity.getDescription());
                final Bitmap bitmap = ImageUtils.getBitmapFromByteArray(wikiEntity.getImage());
                mWikiImage.setImageBitmap(bitmap);
                mButtonSave.setVisibility(View.GONE);
                mButtonClose.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mNoDataTextView.setVisibility(View.GONE);
                return;
            }
        }

        if (intent != null) {
            wikiItem = intent.getStringExtra(VisionActivity.WIKI_LABEL_KEY);
            final String filePath = getIntent().getStringExtra(FILE_PATH_KEY);
            Drawable drawable = ImageUtils.getDrawableFromPath(this, filePath);
            mWikiImage.setImageDrawable(drawable);
            Timber.d("wikiItem %s", wikiItem);
        }

        if (wikiItem != null)
            loadJSON(wikiItem);

        mWikiSavedBroadcastReceiver = new WikiSavedBroadcastReceiver();
        mNoDataTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.wiki_button_save)
    public void onClickButtonSave(View view) {
        final File imageFile = ImageUtils.getWikiImageFile();
        byte[] fileBytes = ImageUtils.convertFileToByteArray(imageFile);
        WikiEntity wikiEntity = new WikiEntity(mWikiTitle.getText().toString(), wikiDescription.getText().toString(), fileBytes);
        WikiIntentService.startActionInsertWiki(this, wikiEntity);
    }

    @OnClick(R.id.wiki_button_close)
    public void onClickButtonClose(View view) {
        finish();
    }

    private void loadJSON(String title) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WIKIPEDIA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<WikiInfo> call = request.getJSON(title);

        call.enqueue(new Callback<WikiInfo>() {
            @Override
            public void onResponse(Call<WikiInfo> call, Response<WikiInfo> response) {
                Timber.d("onResponse");
                if (response.isSuccessful()) {
                    WikiInfo wikiInfo = response.body();

                    if (wikiInfo != null) {
                        mWikiTitle.setText(wikiInfo.getDisplaytitle());
                        wikiDescription.setText(wikiInfo.getExtract());
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        Timber.d("wikiInfo is null");
                    }
                } else {
                    Timber.e("Error Code %s", String.valueOf(response.code()));
                    Timber.e("Error Body %s", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<WikiInfo> call, Throwable t) {
                Timber.d("onFailure %s", t.getMessage());
                mProgressBar.setVisibility(View.GONE);
                mNoDataTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history_menu_item) {
            HistoryActivity.startHistory(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, WIKI_MAIN);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        if (!TextUtils.isEmpty(mWikiTitle.getText()))
            mProgressBar.setVisibility(View.GONE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mWikiSavedBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mWikiSavedBroadcastReceiver);
    }
}
