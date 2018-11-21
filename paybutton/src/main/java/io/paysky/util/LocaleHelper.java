package io.paysky.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Paysky-202 on 5/27/2018.
 */

public class LocaleHelper {


    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);

        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            Resources resources = context.getResources();

            Configuration configuration = resources.getConfiguration();

            DisplayMetrics displayMetrics = resources.getDisplayMetrics();

            configuration.setLocale(locale);

            resources.updateConfiguration(configuration, displayMetrics);

        } else {

            Configuration config = new Configuration();

            config.locale = locale;

            context.getResources().updateConfiguration(config,

                    context.getResources().getDisplayMetrics());

        }


    }


    public static String getLocale(){
        return Locale.getDefault().getLanguage();
    }

    public static void changeAppLanguage(Context context) {
        String appLocale = Locale.getDefault().getLanguage();
        if (appLocale.equals("ar")) {
            setLocale(context, "en");
        } else {
            setLocale(context, "ar");
        }
    }
}
