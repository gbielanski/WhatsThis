package com.bielanski.whatsthis.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;

public class ImageUtils {
    public static Drawable getDrawableFromPath(Context context, String filePath){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        File imgFile = new  File(filePath);
        Bitmap inBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap, 0, 0, inBitmap.getWidth(), inBitmap.getHeight(), matrix, true);
        Drawable drawable = new BitmapDrawable(context.getResources(), outBitmap);
        return drawable;
    }
}