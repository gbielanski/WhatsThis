package com.bielanski.whatsthis.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.database.WikiIntentService;
import com.bielanski.whatsthis.database.data.WikiEntity;
import com.bielanski.whatsthis.network.RequestInterface;
import com.bielanski.whatsthis.network.data.WikiInfo;
import com.bielanski.whatsthis.utils.ImageUtils;
import com.squareup.picasso.Picasso;

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

    @BindView(R.id.wiki_toolbar) Toolbar mToolbar;
    @BindView(R.id.wiki_image) ImageView wikiImage;
    @BindView(R.id.wiki_title) TextView wikiTitle;
    @BindView(R.id.wiki_description) TextView wikiDescription;
    @BindView(R.id.wiki_button_save) ImageButton mButtonSave;
    @BindView(R.id.wiki_button_close) ImageButton mButtonClose;
    private WikiSavedBroadcastReceiver mWikiSavedBroadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        ButterKnife.bind(this);
        Timber.tag(TAG);
        setSupportActionBar(mToolbar);
        final Intent intent = getIntent();
        String wikiItem = null;
        WikiEntity wikiEntity = null;

        if (intent != null) {
            wikiEntity = intent.getParcelableExtra(WIKI_KEY);
            if(wikiEntity != null) {
                wikiTitle.setText(wikiEntity.getTitle());
                wikiDescription.setText(wikiEntity.getDescription());
                Picasso.get().load(new File(wikiEntity.getFileName())).into(wikiImage);
                mButtonSave.setVisibility(View.GONE);
                mButtonClose.setVisibility(View.GONE);
                return;
            }
        }

        if (intent != null) {
            wikiItem = intent.getStringExtra(VisionActivity.WIKI_LABEL_KEY);
            final String filePath = getIntent().getStringExtra(FILE_PATH_KEY);

            Drawable drawable = ImageUtils.getDrawableFromPath(this, filePath);
            //Drawable drawable = Drawable.createFromPath(filePath);
            wikiImage.setImageDrawable(drawable);

            Timber.d("wikiItem %s", wikiItem);
        }

        if (wikiItem != null)
            loadJSON(wikiItem);

        mWikiSavedBroadcastReceiver = new WikiSavedBroadcastReceiver();
        intentFilter = new IntentFilter(WikiIntentService.ACTION_WIKI_SAVED);
    }

    @OnClick(R.id.wiki_button_save)
    public void onClickButtonSave(View view) {
        WikiEntity wikiEntity = new WikiEntity(wikiTitle.getText().toString(), wikiDescription.getText().toString());
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

        //progressBar.setVisibility(View.VISIBLE);
        // recyclerView.setVisibility(View.GONE);

        call.enqueue(new Callback<WikiInfo>() {
            @Override
            public void onResponse(Call<WikiInfo> call, Response<WikiInfo> response) {
                Timber.d("onResponse");
                if (response.isSuccessful()) {
                    WikiInfo wikiInfo = response.body();

                    if (wikiInfo != null) {

                        wikiTitle.setText(wikiInfo.getDisplaytitle());
                        wikiDescription.setText(wikiInfo.getExtract());
                        //progressBar.setVisibility(View.GONE);
                        //recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Timber.d("wikiInfo is null");
                    }
                } else {
                    Timber.e("Error Code %s", String.valueOf(response.code()));
                    Timber.e("Error Body %s", response.errorBody().toString());

                    //display the appropriate message...
                }


            }

            @Override
            public void onFailure(Call<WikiInfo> call, Throwable t) {
                Timber.d("onFailure %s", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.history_menu_item) {
            HistoryActivity.startHistory(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mWikiSavedBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mWikiSavedBroadcastReceiver);
    }

}
