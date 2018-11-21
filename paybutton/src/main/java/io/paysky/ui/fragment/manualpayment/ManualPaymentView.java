package io.paysky.ui.fragment.manualpayment;

import android.os.Bundle;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.ui.mvp.BaseView;

public interface ManualPaymentView extends BaseView {


    void show3dpWebView(String webBody, String paymentServerURL, int gatewayType,PaymentData paymentData);

    void showErrorInServerToast();

    void showPaymentFailedFragment(Bundle bundle);

    void showTransactionApprovedFragment(String transactionNo, String authCode, String receiptNumber, String cardHolder, String cardNumber, String systemReference, PaymentData paymentData);
}
