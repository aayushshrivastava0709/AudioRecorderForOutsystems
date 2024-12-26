package com.audio.recorder;

import android.media.MediaRecorder;
import android.os.Environment;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class AudioRecorder extends CordovaPlugin {

    private MediaRecorder recorder;
    private String outputFilePath;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startRecording")) {
            startRecording(callbackContext);
            return true;
        } else if (action.equals("stopRecording")) {
            stopRecording(callbackContext);
            return true;
        }
        return false;
    }

    private void startRecording(CallbackContext callbackContext) {
        try {
            outputFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp3";
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(outputFilePath);
            recorder.prepare();
            recorder.start();

            callbackContext.success("Recording started");
        } catch (IOException e) {
            callbackContext.error("Error starting recording: " + e.getMessage());
        }
    }

    private void stopRecording(CallbackContext callbackContext) {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            callbackContext.success("Recording saved at " + outputFilePath);
        } else {
            callbackContext.error("No recording in progress");
        }
    }
}
