package io.paysky.ui.mvp;

import android.support.annotation.StringRes;

public interface BaseView {

    void showProgress();

    void showProgress(@StringRes int message);


    void dismissProgress();

    void showToast(@StringRes int message);

    void showNoInternetDialog();

    boolean isInternetAvailable();
}
