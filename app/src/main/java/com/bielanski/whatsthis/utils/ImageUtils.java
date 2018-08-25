package com.bielanski.whatsthis.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ImageUtils {
    public static final String FILE_PATH_KEY = "FILE_PATH_KEY";

    public static Bitmap getBitmapFromByteArray(byte[] bitmapByteArray){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    public static Drawable getDrawableFromPath(Context context, String filePath){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        File imgFile = new  File(filePath);
        Bitmap inBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap, 0, 0, inBitmap.getWidth(), inBitmap.getHeight(), matrix, true);
        Drawable drawable = new BitmapDrawable(context.getResources(), outBitmap);
        return drawable;
    }

    public static File getWikiImageFile(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final File inputFile = new File(path, "pic.jpg");
        return inputFile;
    }

    public static String saveImageFile() {
        InputStream in = null;
        OutputStream out = null;
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        final File inputFile = new File(path, "pic.jpg");
        final File outputFile = new File(path, "pic" + new Date().getTime() + ".jpg");

        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputFile.toString();
    }

    public static byte[] convertFileToByteArray(File file)
    {
        byte[] fileBytes = new byte[(int) file.length()];
        try(FileInputStream inputStream = new FileInputStream(file))
        {
            inputStream.read(fileBytes);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return fileBytes;
    }
}
