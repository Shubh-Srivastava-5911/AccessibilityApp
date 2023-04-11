package com.example.messagerecorder;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

public class ModelForSharedPreferences
{
    SharedPreferences spf;

    public ArrayList<String> getList(Context c)
    {
        ArrayList<String> al = new ArrayList<>();
        spf = c.getSharedPreferences("comexamplemessagerecorder", Context.MODE_PRIVATE);
        Map<String,?> allMsgs = spf.getAll();

        // getting things from shared preference
        for(Map.Entry<String,?> entry : allMsgs.entrySet()) {
            al.add(entry.getKey());
        }
        return al;
    }
}
