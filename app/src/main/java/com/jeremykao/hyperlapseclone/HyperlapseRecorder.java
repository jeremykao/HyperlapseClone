package com.jeremykao.hyperlapseclone;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jeremykao on 1/18/15.
 */
public class HyperlapseRecorder{

    private Boolean isRecording;
    private final String LOGGING_TAG = "HyperlapseRecorder.java";
    private MediaRecorder recorder;
    private Camera camera;
    private HyperlapsePreview previewSurface;

    //Constructor to initialize defaults and stuff.
    public HyperlapseRecorder(Camera camera, HyperlapsePreview previewSurface){
        this.camera = camera;
        this.previewSurface = previewSurface;
        this.recorder = new MediaRecorder();
        //this.configureCamcorder();
        this.setRecordingStatus(false);
    }
    public void configureCamcorder(){
        this.camera.unlock();
        this.recorder.setCamera(this.camera);
        this.recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        this.recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        Log.d(LOGGING_TAG, "CamcorderProfile:: " + CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH).toString());
        this.recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        this.recorder.setOrientationHint(this.previewSurface.getDisplayOrientation());
        this.recorder.setOutputFile(this.getOutputMediaFile().toString());
    }
    public void startRecording() throws IOException {
        Log.d(LOGGING_TAG, "setting preview display");
        this.configureCamcorder();
        this.recorder.setPreviewDisplay(this.previewSurface.getSurface());

        try{
            Log.d(LOGGING_TAG, "preparring camcorder");
            this.recorder.prepare();
            Log.d(LOGGING_TAG, "starting camcorder");
            this.recorder.start();
            this.setRecordingStatus(true);

        }
        catch (IOException e){
            Log.d(LOGGING_TAG, "Error preparing Camera");
        }
    }
    public void stopRecording(){
        this.recorder.stop();
        this.recorder.release();
        this.setRecordingStatus(false);
    }
    public Boolean getRecordingStatus(){
        return this.isRecording;
    }
    public void setRecordingStatus(Boolean status){
        this.isRecording = status;
    }
    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }
    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HyperlapseClone");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("HyperlapseRecorder.java", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outputFile =  new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        Log.d(LOGGING_TAG, "this is outputfile_name:: " + outputFile.toString());
        return outputFile;
    }
}
