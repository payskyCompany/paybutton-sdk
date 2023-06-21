package io.paysky.ui.fragment.webview;

import static io.paysky.util.AppUtils.getDateTimeLocalTrxn;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.paybutton.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.ReceiptData;
import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.network.ApiLinks;
import io.paysky.exception.TransactionException;
import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.ui.base.BaseFragment;
import io.paysky.ui.fragment.paymentfail.PaymentFailedFragment;
import io.paysky.ui.fragment.paymentsuccess.PaymentApprovedFragment;
import io.paysky.util.AppConstant;
import io.paysky.util.ToastUtils;
import io.paysky.util.TransactionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebPaymentFragment extends BaseFragment implements WebPaymentView {

    private WebPaymentPresenter presenter;
    private WebView webView;
    private PaymentData paymentData;

    private String url;

    public WebPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new WebPaymentPresenter();
        activity = (PaymentActivity) getActivity();
        extractBundleData();
    }

    private void extractBundleData() {
        Bundle arguments = getArguments();
        assert arguments != null;
        paymentData = arguments.getParcelable(AppConstant.BundleKeys.PAYMENT_DATA);

        url = arguments.getString("url");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_payment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView(view);
    }

    private void initView(View view) {
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        load3dTransactionWebView();
    }


    boolean sucessTransaction = false;

    @Override
    public void load3dTransactionWebView() {
        webView.loadUrl(url);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isVisible() || sucessTransaction) {
                    return;
                }

                if (url.contains(ApiLinks.PAYMENT_LINK) && url.contains("Success")) {
                    dismissProgress();

                    webView.setVisibility(View.GONE);
                    webView.stopLoading();
                    sucessTransaction = true;
                    // call server.
                    try {
                        Uri uri = Uri.parse(url);
                        Set<String> names = uri.getQueryParameterNames();
                        JSONObject jsonObject = new JSONObject();
                        for (String key : names) {

                            jsonObject.put(key, uri.getQueryParameter(key));

                        }


                        Bundle bundle = new Bundle();

                        ReceiptData receiptData = new ReceiptData();

                        receiptData.channelName = AppConstant.TransactionChannelName.CARD;

                        receiptData.amount = paymentData.amountFormatted;
                        receiptData.cardHolderName = "";
                        receiptData.cardNumber = "";
                        receiptData.merchantName = paymentData.merchantName;
                        receiptData.merchantId = paymentData.merchantId;
                        receiptData.terminalId = paymentData.terminalId;
                        receiptData.paymentType = ReceiptData.PaymentDoneBy.MANUAL.toString();
                        receiptData.transactionType = ReceiptData.TransactionType.SALE.name();
                        receiptData.secureHashKey = paymentData.secureHashKey;
                        bundle.putParcelable(AppConstant.BundleKeys.RECEIPT, receiptData);


                        if (jsonObject.getBoolean("Success")) {
                            receiptData.rrn = jsonObject.getString("AuthCode");

                            receiptData.authNumber = jsonObject.getString("NetworkReference");
                            receiptData.refNumber = jsonObject.getString("MerchantReference");

                            receiptData.receiptNumber = jsonObject.getString("ReceiptNumber");

                            receiptData.stan = jsonObject.getString("SystemReference");


                            SuccessfulCardTransaction cardTransaction = new SuccessfulCardTransaction();
                            cardTransaction.ActionCode = jsonObject.getString("ActionCode");
                            cardTransaction.AuthCode = receiptData.rrn;
                            cardTransaction.MerchantReference = receiptData.refNumber;
                            cardTransaction.Message = jsonObject.getString("Message");
                            cardTransaction.NetworkReference = receiptData.authNumber;
                            cardTransaction.ReceiptNumber = receiptData.receiptNumber;
                            cardTransaction.SystemReference = receiptData.stan;
                            cardTransaction.Success = true;
                            cardTransaction.merchantId = paymentData.merchantId;
                            cardTransaction.terminalId = paymentData.terminalId;
                            cardTransaction.amount = paymentData.executedTransactionAmount;
                            if (paymentData.customerId != null) {
                                cardTransaction.tokenCustomerId = paymentData.customerId;
                            }
                            TransactionManager.setCardTransaction(cardTransaction);

                            activity.replaceFragmentAndRemoveOldFragment(PaymentApprovedFragment.class, bundle);
                        } else {
                            String cause = jsonObject.getString("Message");
                            if (!cause.isEmpty())
                                bundle.putString(AppConstant.BundleKeys.DECLINE_CAUSE, cause);
                            activity.replaceFragmentAndRemoveOldFragment(PaymentFailedFragment.class, bundle);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!isVisible()) {
                        return;
                    }
                    showProgress();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {


                super.onPageFinished(view, url);
                if (!isVisible()) {
                    return;
                }
                dismissProgress();

                String dateTimeLocalTrnx = getDateTimeLocalTrxn();
                if (url.contains("NAPSInquiry")) {
                    presenter.checkTransactionPaymentStatus(
                            paymentData.secureHashKey,
                            paymentData.terminalId,
                            paymentData.merchantId,
                            dateTimeLocalTrnx,
                            true);
                } else if (url.contains("OMInquiry")) {
                    presenter.checkTransactionPaymentStatus(
                            paymentData.secureHashKey,
                            paymentData.terminalId,
                            paymentData.merchantId,
                            dateTimeLocalTrnx,
                            false);
                }

                webView.evaluateJavascript(
                        "document.documentElement.outerHTML;",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                if (html.contains("HTTP Status - 400")) {
                                    try {
                                        html = html.replaceAll("<.*?>", "");
                                        html = html.substring(html.indexOf("E5000:") + 7);
                                        html = html.substring(0, html.indexOf("\\u003C"));
                                        ToastUtils.showLongToast(getActivity(), html);
                                        TransactionManager.setTransactionException(new TransactionException(html));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        ToastUtils.showLongToast(getActivity(), getString(R.string.error_try_again));
                                    }
                                    webView.setWebViewClient(null);
                                    activity.showPaymentBasedOnPaymentOptions(0);
                                }
                            }
                        });
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (!isVisible()) return;
                //-2 address not reachable , -14 error in server.
                if (errorCode == -14 || errorCode == -2 || errorCode == -8) {
                    // error in server.
                    if (!isVisible()) return;
                    dismissProgress();
                    ToastUtils.showLongToast(activity, getString(R.string.error_try_again));
                    activity.showPaymentBasedOnPaymentOptions(0);
                    webView.setWebViewClient(null);
                }
            }

        };
        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress != 100 || view.getUrl().contains(ApiLinks.PAYMENT_LINK)) {

                    if (progressDialog != null && !progressDialog.isShowing())
                        progressDialog.show();
                } else {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void showPaymentStatus(String message, String systemTxnId, boolean type) {
        Bundle bundle = new Bundle();
        if (type) {
            bundle.putString("massage", message);
            bundle.putString("systemTxnId", systemTxnId);
            bundle.putString("opened_by", "manual_payment");
            activity.replaceFragmentAndRemoveOldFragment(PaymentApprovedFragment.class, bundle);
        } else {
            bundle.putString(AppConstant.BundleKeys.DECLINE_CAUSE, message);
            bundle.putString("systemTxnId", systemTxnId);
            bundle.putString("opened_by", "manual_payment");
            activity.replaceFragmentAndRemoveOldFragment(PaymentFailedFragment.class, bundle);
        }
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
