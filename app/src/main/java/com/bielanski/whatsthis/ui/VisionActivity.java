package com.bielanski.whatsthis.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bielanski.whatsthis.R;
import com.bielanski.whatsthis.utils.ImageUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.bielanski.whatsthis.ui.MainActivity.FILE_PATH_KEY;

public class VisionActivity extends AppCompatActivity {
    public static final String WIKI_KEY = "WIKI_KEY";
    public final String TAG = "VisionActivity";
    @BindView(R.id.vision_image) ImageView visionImage;
    @BindView(R.id.vision_list) ListView visionList;
    @BindView(R.id.vision_toolbar) Toolbar mToolbar;
    @BindView(R.id.progress_bar_vision) ProgressBar mProgressBar;
    @BindView(R.id.data_no_available_tv) TextView mNoDataTextView;
    @BindView(R.id.vision_close_button) ImageButton mVisionCloseButton;

    private ArrayAdapter<String> mAdapter;
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Timber.tag(TAG);
        mFilePath = getIntent().getStringExtra(FILE_PATH_KEY);

        Drawable drawable = ImageUtils.getDrawableFromPath(this, mFilePath);

        //Drawable drawable = Drawable.createFromPath(mFilePath);
        visionImage.setImageDrawable(drawable);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>());
        visionList.setAdapter(mAdapter);
        visionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String visionItem = mAdapter.getItem(position);
                Intent intent = new Intent(VisionActivity.this, WikiActivity.class);
                intent.putExtra(WIKI_KEY, visionItem);
                intent.putExtra(FILE_PATH_KEY, mFilePath);
                startActivity(intent);
            }
        });

        mVisionCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        visionList.setVisibility(View.GONE);
        mNoDataTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        // TODO use one of this function dependence on Firebase plan, please see Read me file in repo
        // visionApiLabelingOnDevide(bitmapDrawable);
        visionApiLabelingCloud(bitmapDrawable);
    }

    private void  visionApiLabelingCloud(BitmapDrawable bitmapDrawable) {
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(15)
                        .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmapDrawable.getBitmap());
        FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance()
                .getVisionCloudLabelDetector();
        Task<List<FirebaseVisionCloudLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                    int count = 0;
                                    ArrayList<String> listOfLabels = new ArrayList<>();
                                    @Override
                                    public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                        visionList.setVisibility(View.VISIBLE);
                                        mNoDataTextView.setVisibility(View.GONE);
                                        mProgressBar.setVisibility(View.GONE);
                                        for (FirebaseVisionCloudLabel label : labels) {
                                            String text = label.getLabel();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();
                                            Timber.d("onSuccess labels[%d] text %s entityId %s confidence %.2f", labels.size(), text, entityId, confidence);
                                            listOfLabels.add(text);
                                        }
                                        mAdapter.clear();
                                        mAdapter.addAll(listOfLabels);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        visionList.setVisibility(View.GONE);
                                        mNoDataTextView.setVisibility(View.VISIBLE);
                                        mProgressBar.setVisibility(View.GONE);
                                        Timber.d("onFailure");
                                    }
                                });
    }

    private void visionApiLabelingOnDevide(BitmapDrawable bitmapDrawable) {
        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmapDrawable.getBitmap());

        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                .getVisionLabelDetector(options);

        Task<List<FirebaseVisionLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                    int count = 0;
                                    ArrayList<String> listOfLabels = new ArrayList<>();
                                    @Override
                                    public void onSuccess(List<FirebaseVisionLabel> labels) {
                                        for (FirebaseVisionLabel label : labels) {
                                            String text = label.getLabel();
                                            String entityId = label.getEntityId();
                                            float confidence = label.getConfidence();
                                            Timber.d("onSuccess labels[%d] text %s entityId %s confidence %.2f", labels.size(), text, entityId, confidence);
                                            listOfLabels.add(text);
                                            if(++count > 5) {
                                                break;
                                            }
                                        }
                                        mAdapter.clear();
                                        mAdapter.addAll(listOfLabels);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Timber.d("onFailure");
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
}
