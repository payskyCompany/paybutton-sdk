package io.paysky.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by Paysky-202 on 5/13/2018.
 */

public class ToastUtils {

    public static void showToast(Context context, @StringRes int text) {
        Toast.makeText(context, context.getString(text), Toast.LENGTH_SHORT).show();
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
