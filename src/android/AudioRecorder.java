package com.aayushshrivastava.cordova.plugin;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.os.Environment;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

public class AudioRecorderPlugin extends CordovaPlugin {

    private MediaRecorder mediaRecorder;
    private String fileName;
    
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        if (action.equals("startRecording")) {
            startRecording(callbackContext);
            return true;
        } else if (action.equals("stopRecording")) {
            stopRecording(callbackContext);
            return true;
        } else if (action.equals("pauseRecording")) {
            pauseRecording(callbackContext);
            return true;
        } else {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }
    }

    private void startRecording(CallbackContext callbackContext) {
        // Check for permissions
        if (cordova.hasPermission(android.Manifest.permission.RECORD_AUDIO)) {
            // Create a new MediaRecorder instance and start recording
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_recording.mp4";
            mediaRecorder.setOutputFile(fileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                callbackContext.success("Recording started");
            } catch (IOException e) {
                callbackContext.error("Error starting recording: " + e.getMessage());
            }
        } else {
            cordova.requestPermission(this, 0, android.Manifest.permission.RECORD_AUDIO);
            callbackContext.error("Permission not granted for audio recording");
        }
    }

    private void stopRecording(CallbackContext callbackContext) {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                callbackContext.success("Recording stopped. File saved to: " + fileName);
            } catch (RuntimeException e) {
                callbackContext.error("Error stopping recording: " + e.getMessage());
            }
        } else {
            callbackContext.error("No recording in progress.");
        }
    }

    private void pauseRecording(CallbackContext callbackContext) {
        if (mediaRecorder != null) {
            try {
                // Pause recording (only available in some Android versions)
                mediaRecorder.pause();
                callbackContext.success("Recording paused");
            } catch (Exception e) {
                callbackContext.error("Error pausing recording: " + e.getMessage());
            }
        } else {
            callbackContext.error("No recording in progress.");
        }
    }

    // Request permission callback
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording(null); // Start recording after permission is granted
            } else {
                Log.e("AudioRecorderPlugin", "Permission denied for audio recording.");
            }
        }
    }
}
