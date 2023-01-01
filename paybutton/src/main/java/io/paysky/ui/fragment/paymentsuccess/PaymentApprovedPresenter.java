package io.paysky.ui.fragment.paymentsuccess;

import com.example.paybutton.R;

import io.paysky.data.model.request.SendReceiptByMailRequest;
import io.paysky.data.model.response.SendReceiptByMailResponse;
import io.paysky.data.network.ApiConnection;
import io.paysky.data.network.ApiResponseListener;
import io.paysky.ui.mvp.BasePresenter;
import io.paysky.util.AppUtils;
import io.paysky.util.HashGenerator;

class PaymentApprovedPresenter extends BasePresenter<PaymentApprovedView> {


    void sendEmail(String hashKey, final String email, String terminalId, String merchantId,
                   String referenceNumber, String channelName, String transactionId, final int operationType) {
        // check internet.
        if (!view.isInternetAvailable()) {
            view.showNoInternetDialog();
            return;
        }

        view.showProgress();
        // create request body.
        final SendReceiptByMailRequest request = new SendReceiptByMailRequest();
        request.dateTimeLocalTrxn = AppUtils.getDateTimeLocalTrxn();
        request.emailTo = email;
        request.terminalId = terminalId;
        request.merchantId = merchantId;
        request.transactionId = transactionId;
        request.transactionChannel = channelName;
        request.externalReceiptNo = referenceNumber;
        request.secureHash = HashGenerator.encode(hashKey, request.dateTimeLocalTrxn, merchantId, terminalId);
        // call Api.
        ApiConnection.sendReceiptByMail(request, new ApiResponseListener<SendReceiptByMailResponse>() {
            @Override
            public void onSuccess(SendReceiptByMailResponse response) {
                view.dismissProgress();
                if (response.success) {
                    view.setSendEmailSuccess(request.emailTo, operationType);
                } else {
                    // show error dialog.
                    view.showErrorToast(R.string.cannot_send_mail);
                }
            }

            @Override
            public void onFail(Throwable error) {
                error.printStackTrace();
                view.dismissProgress();
                view.showErrorToast(R.string.cannot_send_mail);
            }
        });
    }
}
