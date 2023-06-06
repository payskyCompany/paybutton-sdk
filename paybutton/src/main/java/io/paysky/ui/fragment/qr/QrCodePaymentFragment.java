package io.paysky.ui.fragment.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paybutton.R;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.ReceiptData;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.data.model.response.TransactionStatusResponse;
import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.ui.base.BaseFragment;
import io.paysky.ui.fragment.paymentsuccess.PaymentApprovedFragment;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;
import io.paysky.util.ToastUtils;
import io.paysky.util.TransactionManager;

public class QrCodePaymentFragment extends BaseFragment implements QrView, View.OnClickListener {


    //Variables.
    private boolean qrGenerated, paymentDone;
    private long transactionId;
    //GUI.
    private ImageView qrImageView;
    private LinearLayout smsPaymentLayout;
    private EditText mobileNumberEditText;
    private Button sendOtpButton;
    private Button requestPaymentButton;
    //Objects.
    private Handler checkTransactionHandler = new Handler();
    private Runnable checkPaymentRunnable;
    private QrPresenter presenter;


    public QrCodePaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PaymentActivity) getActivity();
        presenter = new QrPresenter(getArguments());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView(view);
        activity.setHeaderIcon(R.drawable.ic_close);
        activity.setHeaderIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        checkPaymentRunnable = new Runnable() {
            @Override
            public void run() {
                presenter.checkPaymentApproval(transactionId);
            }
        };
        // this is terminal id
        if (PaymentActivity.qrBitmap == null) {
            presenter.generateQrCode();
        } else {
            showQrImage(PaymentActivity.qrBitmap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (qrGenerated && !paymentDone) {
            checkTransactionHandler.postDelayed(checkPaymentRunnable, 5000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (checkPaymentRunnable != null && qrGenerated && !paymentDone) {
            checkTransactionHandler.removeCallbacks(checkPaymentRunnable);
        }
    }


    private void initView(View view) {
        // find views.
        smsPaymentLayout = view.findViewById(R.id.sms_payment_layout);
        smsPaymentLayout.setOnClickListener(this);
        mobileNumberEditText = view.findViewById(R.id.mobile_number_editText);
        sendOtpButton = view.findViewById(R.id.send_otp_button);
        sendOtpButton.setOnClickListener(this);
        requestPaymentButton = view.findViewById(R.id.request_payment);
        requestPaymentButton.setOnClickListener(this);
        qrImageView = view.findViewById(R.id.qr_imageView);
    }


    @Override
    public void onClick(View view) {
        if (view.equals(sendOtpButton)) {
            sendOtpButtonClick();
        } else if (view.equals(requestPaymentButton)) {
            smsPaymentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void sendOtpButtonClick() {
        String mobileNumber = getText(mobileNumberEditText);
        if (!AppUtils.validPhone(mobileNumber)) {
            mobileNumberEditText.setError(getString(R.string.invalid_mobile_number));
            return;
        }
        // send to server.
        presenter.requestToPay(mobileNumber);
    }

    @Override
    public void setPaymentApproved(TransactionStatusResponse response, PaymentData paymentData) {
        paymentDone = true;
        checkTransactionHandler.removeCallbacks(checkPaymentRunnable);
        // response to caller.
        SuccessfulWalletTransaction walletTransaction = new SuccessfulWalletTransaction();
        walletTransaction.IsPaid = response.isPaid;
        walletTransaction.MerchantReference = response.merchantReference;
        walletTransaction.Message = response.message;
        walletTransaction.NetworkReference = response.networkReference;
        walletTransaction.Payer = response.payer;
        walletTransaction.PayerName = response.payerName;
        walletTransaction.Success = response.success;
        walletTransaction.SystemReference = response.systemReference + "";
        walletTransaction.TxnDate = response.txnDate;
        walletTransaction.merchantId = paymentData.merchantId;
        walletTransaction.terminalId = paymentData.terminalId;
        walletTransaction.amount = paymentData.executedTransactionAmount;
        TransactionManager.setWalletTransaction(walletTransaction);
        // show payment approved
        Bundle bundle = new Bundle();
        ReceiptData receiptData = new ReceiptData();
        receiptData.terminalId = paymentData.terminalId;
        receiptData.merchantId = paymentData.merchantId;
        receiptData.receiptNumber = response.externalTxnId;
        receiptData.amount = paymentData.amountFormatted;
        receiptData.authNumber = response.systemReference + "";
        receiptData.rrn = response.networkReference;
        receiptData.channelName = AppConstant.TransactionChannelName.TAHWEEL;
        receiptData.merchantName = paymentData.merchantName;
        receiptData.secureHashKey = paymentData.secureHashKey;
        bundle.putParcelable(AppConstant.BundleKeys.RECEIPT, receiptData);
        activity.hidePaymentOptions();
        activity.replaceFragmentAndAddOldToBackStack(PaymentApprovedFragment.class, bundle);
    }

    public void listenToPaymentApproval() {
        checkTransactionHandler.postDelayed(checkPaymentRunnable, 5000);
    }


    @Override
    public void showInfoToast(String message) {
        ToastUtils.showLongToast(activity, message);
    }

    @Override
    public void showErrorInServerToast() {
        ToastUtils.showLongToast(activity, getString(R.string.error_try_again));
    }

    @Override
    public void showQrImage(Bitmap bitmap) {
        qrImageView.setImageBitmap(bitmap);
    }

    @Override
    public void disableR2pViews() {
        mobileNumberEditText.setEnabled(false);
        requestPaymentButton.setEnabled(false);
        sendOtpButton.setEnabled(false);
    }

    @Override
    public void setGenerateQrSuccess(long transactionId) {
        qrGenerated = true;
        this.transactionId = transactionId;
        checkTransactionHandler.postDelayed(checkPaymentRunnable, 5000); // listen to payment approval.
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
