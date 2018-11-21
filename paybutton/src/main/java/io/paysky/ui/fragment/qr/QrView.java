package io.paysky.ui.fragment.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.response.TransactionStatusResponse;
import io.paysky.ui.mvp.BaseView;

public interface QrView extends BaseView {


    void setPaymentApproved(TransactionStatusResponse response, PaymentData paymentData);

    void listenToPaymentApproval();

    void setGenerateQrSuccess(long txnId);

    void showInfoToast(String message);

    void showErrorInServerToast();

    Context getContext();

    void showQrImage(Bitmap bitmap);

    void disableR2pViews();
}
