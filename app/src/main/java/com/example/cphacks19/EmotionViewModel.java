package com.example.cphacks19;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EmotionViewModel extends ViewModel {
    MutableLiveData<Double> emotionLiveData = new MutableLiveData<>();

    public void setEmotionLiveData(double value){
        emotionLiveData.setValue(value);
    }

    public LiveData<Double> getEmotionLiveData(){
        return emotionLiveData;
    }
}
