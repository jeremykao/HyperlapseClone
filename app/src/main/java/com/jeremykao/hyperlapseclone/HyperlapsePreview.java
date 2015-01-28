package com.jeremykao.hyperlapseclone;

/**
 * Created by jeremykao on 1/18/15.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;

import java.io.IOException;

/** A basic Camera preview class */
public class HyperlapsePreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private final String LOGGING_TAG = "HyperlapsePreview.java";
    private int screenOrientation;

    public HyperlapsePreview(Context context, Camera camera, int rotation) {
        super(context);
        this.mCamera = camera;
        this.screenOrientation = rotation;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            Log.d(LOGGING_TAG, "setting display Orientation: " + getDisplayOrientation());
            this.mCamera.setDisplayOrientation(getDisplayOrientation());
            this.mCamera.setPreviewDisplay(holder);
            this.mCamera.startPreview();
        } catch (IOException e) {
            Log.d(LOGGING_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    public int getDisplayOrientation(){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);

        return (info.orientation - this.screenOrientation + 360) % 360;
    }
    public Surface getSurface(){
        return this.mHolder.getSurface();
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(LOGGING_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}