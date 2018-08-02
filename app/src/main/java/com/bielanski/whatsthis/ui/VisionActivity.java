package com.bielanski.whatsthis.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bielanski.whatsthis.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class VisionActivity extends AppCompatActivity {

    private final String TAG = "VisionActivity";
    @BindView(R.id.vision_image)
    ImageView visionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        final String filePath = getIntent().getStringExtra(MainActivity.FILE_PATH);
        Timber.d(filePath);
        Drawable drawable  = Drawable.createFromPath(filePath);

        visionImage.setImageDrawable(drawable);


    }
}
