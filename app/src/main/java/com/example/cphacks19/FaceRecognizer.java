package com.example.cphacks19;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FaceRecognizer {
    private static final String TAG = "Face Recognizer";
    private static final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
    private static final String subscriptionKey = "b07f31b292fb400abc7360abcabd3323";
    private static final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    public static void detectAndSetEmotionValue(final Bitmap imageBitmap, final String emotionName, AppCompatActivity activity) {
        final EmotionViewModel model = ViewModelProviders.of(activity).get(EmotionViewModel.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,
                                    // returnFaceLandmarks
                                new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Emotion
                                    }

                            );
                            if (result == null){
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(String.format(
                                    "Detection Finished. %d face(s) detected",
                                    result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Face[] result) {
                        double emotionValue;
                        if (result.length != 1){
                            Log.d(TAG, "Result length: " + result.length);
                            emotionValue = 0;
                        } else {
                            emotionValue = FaceResultAttributeFactory.get(result[0],emotionName);
                        }
                        Log.d(TAG,"Emotion value: "+ emotionValue);
                        model.setEmotionLiveData(emotionValue);
                    }
                };

        detectTask.execute(inputStream);
    }
}

class FaceResultAttributeFactory {
    private static final String TAG = "FaceResultFactory";

    static double get(Face result, String name) {
        if (name == null){
            Log.d(TAG,"name is null");
            return 0;
        }
            if(result == null){
            Log.d(TAG,"result is null");
            return 0;
        }
        Log.d(TAG,"name " + name + " found! returning value");

        name = name.toLowerCase();
        switch (name) {
            case "anger":
                return result.faceAttributes.emotion.anger;
            case "contempt":
                return result.faceAttributes.emotion.contempt;
            case "disgust":
                return result.faceAttributes.emotion.disgust;
            case "fear":
                return result.faceAttributes.emotion.fear;
            case "happiness":
                return result.faceAttributes.emotion.happiness;
            case "neutral":
                return result.faceAttributes.emotion.neutral;
            case "sadness":
                return result.faceAttributes.emotion.sadness;
            case "surprise":
                return result.faceAttributes.emotion.surprise;
            default:
                return 0;
        }
    }
}
