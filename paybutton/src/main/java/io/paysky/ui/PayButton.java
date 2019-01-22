package io.paysky.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.paysky.paybutton.R;

import io.paysky.data.model.CurrencyCode;
import io.paysky.data.model.PaymentData;
import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.data.model.request.MerchantInfoRequest;
import io.paysky.data.model.response.MerchantInfoResponse;
import io.paysky.data.network.ApiConnection;
import io.paysky.data.network.ApiLinks;
import io.paysky.data.network.ApiResponseListener;
import io.paysky.exception.TransactionException;
import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;
import io.paysky.util.CurrencyHelper;
import io.paysky.util.HashGenerator;
import io.paysky.util.ToastUtils;

public class PayButton {

    private Context context;
    private String merchantId, terminalId;
    private double amount = 0.0;
    private int currencyCode = 0;
    private String merchantSecureHash;
    private String transactionReferenceNumber;
    public static PaymentTransactionCallback transactionCallback;
    private ProgressDialog progressDialog;
    private boolean isProduction;

    public PayButton(Context context) {
        this.context = context;
    }


    public PayButton setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PayButton setTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    public PayButton setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public PayButton setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public PayButton setMerchantSecureHash(String merchantSecureHash) {
        this.merchantSecureHash = merchantSecureHash;
        return this;
    }

    public PayButton setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
        return this;
    }


    public void createTransaction(PaymentTransactionCallback transactionCallback) {
        if (isProduction){
            ApiLinks.PAYMENT_LINK = ApiLinks.CUBE;
        }else{
            ApiLinks.PAYMENT_LINK = ApiLinks.GRAY_LINK;
        }
        PayButton.transactionCallback = transactionCallback;
        // validate user inputs.
        validateUserInputs();
        showProgress();
        loadMerchantInfo();
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.show();
    }


    private void loadMerchantInfo() {
        MerchantInfoRequest request = new MerchantInfoRequest();
        request.merchantId = merchantId;
        request.terminalId = terminalId;
        request.paymentMethod = null;
        request.dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn();
        request.secureHash = HashGenerator.encode(merchantSecureHash, request.dateTimeLocalTrxn, merchantId, terminalId);
        ApiConnection.getMerchantInfo(context , request, new ApiResponseListener<MerchantInfoResponse>() {
            @Override
            public void onSuccess(MerchantInfoResponse response) {
                dismissProgressDialog();
                if (response == null || !response.success) {
                    ToastUtils.showToast(context, R.string.failed_load_merchant_data);
                    PayButton.transactionCallback.onError(new TransactionException("invalid merchant data"));
                    return;
                }
                PaymentData paymentData = new PaymentData();
                paymentData.merchantId = merchantId;
                paymentData.terminalId = terminalId;
                paymentData.transactionReferenceNumber = transactionReferenceNumber;
                paymentData.merchantName = response.merchantName;
                paymentData.is3dsEnabled = response.is3DS;
                paymentData.isTahweel = response.isTahweel;
                paymentData.isVisa = response.ismVisa;
                if (currencyCode == 0) {
                    paymentData.currencyCode = response.merchantCurrency;
                } else {
                    paymentData.currencyCode = currencyCode + "";
                }
                paymentData.amount = amount;
                paymentData.amountFormatted = amount + "";
                paymentData.paymentMethod = response.paymentMethod;
                paymentData.secureHashKey = merchantSecureHash;
                String[] c = {paymentData.currencyCode };
                CurrencyCode currencyCode = CurrencyHelper.getCurrencyCode(context, c[0]);
                if (currencyCode != null) {
                    paymentData.currencyName = currencyCode.currencyShortName;
                } else {
                    paymentData.currencyName = "";
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
                context.startActivity(new Intent(context, PaymentActivity.class).putExtras(bundle));
            }

            @Override
            public void onFail(Throwable error) {
                error.printStackTrace();
                dismissProgressDialog();
                ToastUtils.showToast(context, R.string.check_internet_connection);
                PayButton.transactionCallback.onError(new TransactionException("invalid merchant data"));
            }
        });
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void validateUserInputs() {
        if (merchantId == null || terminalId == null || merchantSecureHash == null || transactionReferenceNumber == null || amount == 0) {
            throw new IllegalArgumentException("add all inputs data");
        }
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (transactionCallback == null) {
            throw new IllegalArgumentException("transaction callback cannot be null");
        }

    }

    public boolean isProduction() {
        return isProduction;
    }

    public void setProduction(boolean production) {
        isProduction = production;
    }

    public interface PaymentTransactionCallback {
        void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction);

        void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction);

        void onError(TransactionException error);
    }
}
