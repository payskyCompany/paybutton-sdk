package io.paysky.ui.fragment.webview;

import static io.paysky.util.AppUtils.getDateTimeLocalTrxn;

import io.paysky.data.model.request.TransactionStatusRequest;
import io.paysky.data.model.response.TransactionStatusResponse;
import io.paysky.data.network.ApiConnection;
import io.paysky.data.network.ApiResponseListener;
import io.paysky.ui.mvp.BasePresenter;
import io.paysky.util.HashGenerator;

class WebPaymentPresenter extends BasePresenter<WebPaymentView> {
    public void load3dWebView() {
        view.load3dTransactionWebView();
    }

    public void checkTransactionPaymentStatus(String hashKey,
                                              String terminalId,
                                              String merchantId,
                                              String ExtraInfo,
                                              boolean IsNaps) {
        // check internet.
        if (!view.isInternetAvailable()) {
            view.showNoInternetDialog();
            return;
        }
        view.showProgress();
        // create request body.
        TransactionStatusRequest request = new TransactionStatusRequest();
        request.dateTimeLocalTrxn = getDateTimeLocalTrxn();
        request.ExtraInfo = ExtraInfo;
        request.IsNaps = IsNaps;
        request.terminalId = terminalId;
        request.merchantId = merchantId;
        request.secureHash = HashGenerator.encode(hashKey, request.dateTimeLocalTrxn, merchantId, terminalId);
        // call Api.
        ApiConnection.checkTransactionPaymentStatus(request, new ApiResponseListener<TransactionStatusResponse>() {
            @Override
            public void onSuccess(TransactionStatusResponse response) {
                view.dismissProgress();
                view.showPaymentStatus(response.message, response.systemTxnId, response.success);
            }

            @Override
            public void onFail(Throwable error) {
                error.printStackTrace();
                view.dismissProgress();
            }
        });
    }
}
