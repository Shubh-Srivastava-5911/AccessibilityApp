package com.example.messagerecorder;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

public class ViewModelForTexts extends AndroidViewModel
{
    ArrayList<String> al = new ArrayList<>();

    public ViewModelForTexts(@NonNull Application application) {
        super(application);
    }

    public ArrayList<String> getList(Context c) {
        return new ModelForSharedPreferences().getList(c);
    }

}
