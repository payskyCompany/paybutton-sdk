package io.paysky.util;

import android.content.Context;

import com.google.gson.Gson;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;
import com.prashantsolanki.secureprefmanager.SecurePrefManagerInit;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public class PrefsUtils {

    public static void initialize(Context application) {
        new SecurePrefManagerInit.Initializer(application.getApplicationContext())
                .useEncryption(true)
                .initialize();
    }

    public static void save(Context context, String key, Object value) {
        SecurePrefManager.with(context)
                .set(key)
                .value(value.toString())
                .go();
    }

    public static void saveAsJson(Context context, String key, Object value) {
        SecurePrefManager.with(context)
                .set(key)
                .value(new Gson().toJson(value))
                .go();
    }

    public static String get(Context context, String key) {
        return SecurePrefManager.with(context)
                .get(key)
                .defaultValue("")
                .go();
    }


    public static <T> T get(Context context, String key, Class<T> tClass) {
        String object = SecurePrefManager.with(context)
                .get(key)
                .defaultValue("")
                .go();
        if (!object.isEmpty()) {
            return new Gson().fromJson(object, tClass);
        }
        return null;
    }


}
