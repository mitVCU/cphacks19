package com.example.cphacks19;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FaceRecognizer {
    private  final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
    private  final String subscriptionKey = "b07f31b292fb400abc7360abcabd3323";
    private EmotionViewModel model;

    public FaceRecognizer(MainActivity activity){
       model = ViewModelProviders.of(activity).get(EmotionViewModel.class);
   }

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);


    public void detectAndSetEmotionValue(final Bitmap imageBitmap, final String emotionName) {
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
                                    false,        // returnFaceLandmarks
                                new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Age,
                                    FaceServiceClient.FaceAttributeType.Gender }

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
                        if (result == null){
                            emotionValue = 0;
                        }else if (result.length != 1){
                            emotionValue = 0;
                        } else {
                            emotionValue = FaceResultAttributeFactory.get(result[0],emotionName);
                        }
                        model.setEmotionLiveData(emotionValue);
                    }
                };

        detectTask.execute(inputStream);
    }
}

class FaceResultAttributeFactory {

    static double get(Face result, String name) {
        if (name == null)
            return 0;
        if(result == null)
            return 0;
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

interface EmotionValueReadyCallback{
    double onValueReady();
}
