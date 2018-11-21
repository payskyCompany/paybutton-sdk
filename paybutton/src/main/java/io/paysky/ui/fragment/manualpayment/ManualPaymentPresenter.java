package io.paysky.ui.fragment.manualpayment;

import android.os.Bundle;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.request.Compose3dsTransactionRequest;
import io.paysky.data.model.request.ManualPaymentRequest;
import io.paysky.data.model.response.Compose3dsTransactionResponse;
import io.paysky.data.model.response.ManualPaymentResponse;
import io.paysky.data.network.ApiConnection;
import io.paysky.data.network.ApiResponseListener;
import io.paysky.exception.TransactionException;
import io.paysky.ui.mvp.BasePresenter;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;
import io.paysky.util.HashGenerator;
import io.paysky.util.TransactionManager;

class ManualPaymentPresenter extends BasePresenter<ManualPaymentView> {

    private PaymentData paymentData;

    ManualPaymentPresenter(Bundle arguments) {
        paymentData = arguments.getParcelable(AppConstant.BundleKeys.PAYMENT_DATA);
        TransactionManager.setTransactionType(TransactionManager.TransactionType.MANUAL);
    }

    public void makePayment(String cardNumber, String expireDate, String cardOwnerName, String ccv) {
        if (paymentData.is3dsEnabled) {
            expose3dsTransaction(paymentData.secureHashKey, paymentData.merchantId, paymentData.terminalId, cardNumber, paymentData.amountFormatted,
                    ccv, paymentData.currencyCode, expireDate);
        } else {
            executeManualPayment(paymentData.secureHashKey, paymentData.currencyCode, paymentData.amountFormatted, paymentData.merchantId,
                    paymentData.terminalId, ccv, expireDate, cardOwnerName, cardNumber, paymentData.receiverMail);
        }

    }


    private void expose3dsTransaction(String secureHash, String merchantId, String terminalId, String pan, String amount, String ccv2, String currencyCode
            , String dateExpiration) {

        if (!view.isInternetAvailable()) {
            view.showNoInternetDialog();
            return;
        }
        view.showProgress();
        // create 3ds transaction request.
        final Compose3dsTransactionRequest request = new Compose3dsTransactionRequest();
        //     request.clientId = 0;
        request.dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn();
        request.merchantId = merchantId;
        request.secureHash = HashGenerator.encode(secureHash, request.dateTimeLocalTrxn, merchantId, terminalId);
        request.terminalId = terminalId;
        request.amountTrxn = AppUtils.formatPaymentAmountToServer(amount);
        request.cVV2 = ccv2;
        request.merchantReference = paymentData.transactionReferenceNumber;
        request.currencyCodeTrxn = Integer.valueOf(currencyCode);
        request.dateExpiration = dateExpiration;
        request.pAN = pan;
        request.returnURL = "http://localhost.com";
        // call Apis.
        ApiConnection.compose3dsTransaction(request, new ApiResponseListener<Compose3dsTransactionResponse>() {
            @Override
            public void onSuccess(Compose3dsTransactionResponse response) {
                if (isViewDetached())return;
                view.dismissProgress();
                String webBody = String.format("vpc_AccessCode=%s&vpc_Amount=%s&vpc_Card=%s&vpc_CardExp=%s&vpc_CardNum=%s&vpc_CardSecurityCode=%s&vpc_Command=%s&vpc_Currency=%s&" +
                                "vpc_Gateway=%s&vpc_MerchTxnRef=%s&vpc_Merchant=%s&vpc_ReturnURL=%s&vpc_Version=%s&vpc_SecureHash=%s&vpc_SecureHashType=%s",
                        response.accessCode,
                        request.amountTrxn,
                        response.cardType,
                        request.dateExpiration,
                        request.pAN,
                        request.cVV2,
                        response.command,
                        response.currency,
                        response.gateway,
                        request.merchantReference,
                        response.merchantAccount,
                        request.returnURL,
                        response.version,
                        response.secureHash,
                        response.secureHashType
                );
                view.show3dpWebView(webBody, response.paymentServerURL, response.gatewayType, paymentData);
            }

            @Override
            public void onFail(Throwable error) {
                if (isViewDetached())return;
                error.printStackTrace();
                view.dismissProgress();
                view.showErrorInServerToast();
            }
        });
    }


    private void executeManualPayment(String secureHash, String currencyCode, String payAmount, final String merchantId, final String terminalId,
                                      String ccv, String expiryDate, final String cardHolder, final String cardNumber, final String receiverMail) {
        // check internet.
        if (!view.isInternetAvailable()) {
            view.showNoInternetDialog();
            return;
        }
        view.showProgress();
        // create request.
        ManualPaymentRequest paymentRequest = new ManualPaymentRequest();
        final int amount = AppUtils.formatPaymentAmountToServer(payAmount);
        paymentRequest.amountTrxn = amount + "";
        paymentRequest.cardAcceptorIDcode = merchantId;
        paymentRequest.cardAcceptorTerminalID = terminalId;
        paymentRequest.currencyCodeTrxn = currencyCode;
        paymentRequest.cvv2 = ccv;
        paymentRequest.dateExpiration = expiryDate;
        paymentRequest.iSFromPOS = true;
        paymentRequest.pAN = cardNumber;
        paymentRequest.systemTraceNr = paymentData.transactionReferenceNumber;
        paymentRequest.dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn();
        paymentRequest.merchantId = merchantId;
        paymentRequest.terminalId = terminalId;
        // create secure hash.
        paymentRequest.secureHash = HashGenerator.encode(secureHash, paymentRequest.dateTimeLocalTrxn, merchantId, terminalId);
        // make transaction.
        ApiConnection.executePayment(paymentRequest, new ApiResponseListener<ManualPaymentResponse>() {
            @Override
            public void onSuccess(ManualPaymentResponse response) {
                if (isViewDetached())return;
                // server make response.
                view.dismissProgress();

                if (response.mWActionCode != null) {
                    TransactionException transactionException = new TransactionException();
                    transactionException.errorMessage = response.mWMessage;
                    TransactionManager.setTransactionException(transactionException);

                    Bundle bundle = new Bundle();
                    bundle.putString("decline_cause", response.mWMessage);
                    bundle.putString("opened_by","manual_payment");
                    view.showPaymentFailedFragment(bundle);
                } else {
                    if (response.actionCode == null || response.actionCode.isEmpty() || !response.actionCode.equals("00")) {
                        TransactionException transactionException = new TransactionException();
                        transactionException.errorMessage = response.message;
                        TransactionManager.setTransactionException(transactionException);

                        Bundle bundle = new Bundle();
                        bundle.putString("decline_cause", response.message);
                        bundle.putString("opened_by","manual_payment");
                        view.showPaymentFailedFragment(bundle);
                    } else {
                        // transaction success.
                        SuccessfulCardTransaction cardTransaction = new SuccessfulCardTransaction();
                        cardTransaction.ActionCode = response.actionCode;
                        cardTransaction.AuthCode = response.authCode;
                        cardTransaction.MerchantReference = response.merchantReference;
                        cardTransaction.Message = response.message;
                        cardTransaction.NetworkReference = response.networkReference;
                        cardTransaction.ReceiptNumber = response.receiptNumber;
                        cardTransaction.SystemReference = response.systemReference + "";
                        cardTransaction.Success = response.success;
                        cardTransaction.merchantId = paymentData.merchantId;
                        cardTransaction.terminalId = paymentData.terminalId;
                        cardTransaction.amount = paymentData.executedTransactionAmount;
                        TransactionManager.setCardTransaction(cardTransaction);
                        view.showTransactionApprovedFragment(response.transactionNo, response.authCode,
                                response.receiptNumber, cardHolder, cardNumber, response.systemReference + "", paymentData);
                    }
                }
            }

            @Override
            public void onFail(Throwable error) {
                // payment failed.
                if (isViewDetached())return;
                view.dismissProgress();
                TransactionException transactionException = new TransactionException();
                transactionException.errorMessage = error.getMessage();
                TransactionManager.setTransactionException(transactionException);
                error.printStackTrace();
                view.showErrorInServerToast();
            }
        });
    }

}
