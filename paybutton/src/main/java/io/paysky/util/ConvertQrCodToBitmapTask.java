package io.paysky.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

/**
 * Created by Paysky-202 on 5/16/2018.
 */

public class ConvertQrCodToBitmapTask extends AsyncTask<String, String, Bitmap> {


    private WeakReference<QrBitmapLoadListener> loadListener;
    private int sizePx;

    public ConvertQrCodToBitmapTask(QrBitmapLoadListener loadListener, int sizePx) {
        this.loadListener = new WeakReference<>(loadListener);
        this.sizePx = sizePx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override

    protected Bitmap doInBackground(String... params) {

        String path = params[0];

        Bitmap ja = null;

        final QRCodeWriter writer = new QRCodeWriter();

        BitMatrix bitMatrix;

        try {

            Hashtable hints = new Hashtable();

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            bitMatrix = writer.encode(path,

                    BarcodeFormat.QR_CODE, Math.round(sizePx)

                    , Math.round(sizePx), hints);

            int width = Math.round(sizePx);

            int height = Math.round(sizePx);

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            bmp.setHasAlpha(true);

            for (int x = 0; x < width; x++) {

                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);

                }

            }
            ja = bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return ja;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        QrBitmapLoadListener qrBitmapLoadListener = loadListener.get();
        if (qrBitmapLoadListener != null) {
            if (result == null) {
                qrBitmapLoadListener.onLoadBitmapQrFailed();
            } else {
                qrBitmapLoadListener.onLoadBitmapQrSuccess(result);
            }
        }
    }
}
