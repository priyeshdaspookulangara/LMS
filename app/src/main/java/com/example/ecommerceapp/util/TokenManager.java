package com.example.ecommerceapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "ECommerceAppPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    // Add other keys if needed, e.g., KEY_REFRESH_TOKEN, KEY_USER_ID

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // Needs ApplicationContext to avoid memory leaks with Activity context
    public TokenManager(Context context) {
        if (context == null) {
            throw new IllegalStateException("Context cannot be null for TokenManager");
        }
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveAccessToken(String token) {
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearToken() {
        editor.remove(KEY_ACCESS_TOKEN);
        // editor.remove(KEY_USER_ID); // also clear other related session data
        editor.apply();
    }

    public boolean hasToken() {
        return getAccessToken() != null;
    }
}
