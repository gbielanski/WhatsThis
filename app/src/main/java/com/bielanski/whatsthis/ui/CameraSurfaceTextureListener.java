package com.bielanski.whatsthis.ui;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

public class CameraSurfaceTextureListener implements TextureView.SurfaceTextureListener {
    private OnSurfaceTextureAvailable callback;

    public interface OnSurfaceTextureAvailable{
        void onSurfaceTextureAvailable();
    }

    CameraSurfaceTextureListener(OnSurfaceTextureAvailable callback) {
        this.callback = callback;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        callback.onSurfaceTextureAvailable();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
