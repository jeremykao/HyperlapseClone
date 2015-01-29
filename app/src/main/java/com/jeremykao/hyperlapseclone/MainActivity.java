package com.jeremykao.hyperlapseclone;

import android.app.Activity;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.hardware.Camera;

import java.io.IOException;


public class MainActivity extends Activity {
    private final String LOGGING_TAG = "MainActivity.java";
    private HyperlapsePreview cameraPreview;
    private Camera camera;
    private HyperlapseRecorder camcorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecordButton();
        setupCameraPreview();
        setupRecorder();
    }
    private void setupCameraPreview(){
        Log.d(LOGGING_TAG, "Setting up preview");
        camera = Camera.open();
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        cameraPreview = new HyperlapsePreview(this, camera, rotation);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);
    }
    private void setupRecordButton(){
        ImageButton recordButton = (ImageButton) findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.camcorder.getRecordingStatus()){
                    MainActivity.this.camcorder.stopRecording();
                    Toast stopRecordingToast = Toast.makeText(getApplicationContext(), "Stopped Recording", Toast.LENGTH_LONG);
                    stopRecordingToast.show();
                }
                else{
                    try{
                        MainActivity.this.camcorder.startRecording();
                        Toast startRecordingToast = Toast.makeText(getApplicationContext(), "Started Recording", Toast.LENGTH_LONG);
                        startRecordingToast.show();
                    }
                    catch (IOException e){
                        Log.d(LOGGING_TAG, "Cannot start recording...");
                    }
                }
            }
        });
    }
    private void setupRecorder(){
        Log.d(LOGGING_TAG, "setting up camcorder");
        this.camcorder = new HyperlapseRecorder(this.camera, this.cameraPreview);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
