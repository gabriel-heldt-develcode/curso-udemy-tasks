package com.example.tasks.service.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 */
public class SecurityPreferences {

    private SharedPreferences mSharedPreferences;

    public SecurityPreferences(Context context) {
        this.mSharedPreferences = context.getSharedPreferences("TasksShared", Context.MODE_PRIVATE);
    }

    public void storeString(String key, String value) {                 //método salva
        this.mSharedPreferences.edit().putString(key, value).apply();
    }

    public String storeString(String key) {                             //método busca
        return this.mSharedPreferences.getString(key, "");
    }

    public void remove(String key) {                                    //método remove
        this.mSharedPreferences.edit().remove(key).apply();
    }
}
