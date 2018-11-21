package io.paysky.util;

import android.graphics.Bitmap;

public interface QrBitmapLoadListener {

    void onLoadBitmapQrSuccess(Bitmap bitmap);

    void onLoadBitmapQrFailed();
}
