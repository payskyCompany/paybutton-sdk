package io.paysky.ui.fragment.webview;

import io.paysky.ui.mvp.BaseView;

public interface WebPaymentView extends BaseView {
    void load3dTransactionWebView();

    void showPaymentStatus(String message, String systemTxnId, boolean type);
}
