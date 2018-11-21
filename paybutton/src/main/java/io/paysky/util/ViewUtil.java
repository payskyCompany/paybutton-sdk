package io.paysky.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public class ViewUtil {

    public static View inflateView(Context context, @LayoutRes int layoutView, ViewGroup parentView, boolean attachToParent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutView, parentView, attachToParent);

    }

    public static View inflateView(Context context, @LayoutRes int layoutView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutView, null);
    }
}
