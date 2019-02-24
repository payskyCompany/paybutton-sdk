package io.paysky.ui.fragment.webview;

import io.paysky.ui.mvp.BasePresenter;

class WebPaymentPresenter extends BasePresenter<WebPaymentView> {


    void load3dWebView() {
        view.load3dTransactionWebView();
    }
}
