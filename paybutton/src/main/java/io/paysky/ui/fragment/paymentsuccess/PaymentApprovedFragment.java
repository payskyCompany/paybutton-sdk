package io.paysky.ui.fragment.paymentsuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paybutton.R;

import io.paysky.data.model.ReceiptData;
import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.ui.base.BaseFragment;
import io.paysky.util.AppUtils;
import io.paysky.util.PrintReceiptListener;
import io.paysky.util.ReceiptManager;
import io.paysky.util.ToastUtils;

public class PaymentApprovedFragment extends BaseFragment implements View.OnClickListener, PrintReceiptListener, PaymentApprovedView {

    //Objects.
    private PaymentActivity activity;
    ReceiptManager receiptManager;
    private ReceiptData transactionData;
    private PaymentApprovedPresenter presenter;
    //GUI.
    private EditText emailEditText;
    private LinearLayout sendEmailLayout,
            sendEmailNotificationLayout, sendEmailSuccessLayout;
    private TextView mailSentTextView;
    private Button printReceipt;
    //Variables.
    private boolean printMerchantCopy = true;

    public PaymentApprovedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PaymentActivity) getActivity();
        extractBundle();
        presenter = new PaymentApprovedPresenter();
        presenter.attachView(this);
    }


    private void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            transactionData = bundle.getParcelable("receipt");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        AppUtils.playRingtone(getActivity());
        return inflater.inflate(R.layout.fragment_payment_approved, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        activity.setHeaderIcon(R.drawable.ic_close);
        activity.setHeaderIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        receiptManager = new ReceiptManager(view, transactionData, this);
    }

    private void initView(View view) {
        // find views.
        TextView approvedTextView = view.findViewById(R.id.approved_textView);
        AppUtils.showHtmlText(approvedTextView, R.string.transaction_success);
        TextView authNumberTextView = view.findViewById(R.id.auth_number_textView);
        authNumberTextView.setText(getString(R.string.auth_number_fromatted, transactionData.authNumber));

        TextView trxIdTextView = view.findViewById(R.id.trx_id_textView);
        if (transactionData.rrn.length() > 30) {
            trxIdTextView.setText(getString(R.string.trx_id_formatted, transactionData.stan.substring(transactionData.stan.length() - 6)));
        } else {
            trxIdTextView.setText(getString(R.string.trx_id_formatted,transactionData.stan));
        }
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);
        printReceipt = view.findViewById(R.id.print_receipt_button);
        if (AppUtils.isPaymentMachine()) {
            printReceipt.setVisibility(View.VISIBLE);
            printReceipt.setText(R.string.merchant_copy);
            printReceipt.setOnClickListener(this);
        } else {
            printReceipt.setVisibility(View.GONE);
        }
        emailEditText = view.findViewById(R.id.email_editText);
        Button sendEmailButton = view.findViewById(R.id.send_email_button);
        sendEmailButton.setOnClickListener(this);
        sendEmailLayout = view.findViewById(R.id.send_email_layout);
        sendEmailNotificationLayout = view.findViewById(R.id.send_email_notification);
        sendEmailSuccessLayout = view.findViewById(R.id.sent_email_success_layout);
        mailSentTextView = view.findViewById(R.id.mail_sent_textView);
        showSendNotificationEmailLayout();
        showSendEmailLayout();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.close_button) {
            activity.finish();
        } else if (i == R.id.send_email_button) {
            AppUtils.hideKeyboard(view);
            sendEmailButtonClick();
        } else if (i == R.id.print_receipt_button) {
            printReceiptButtonClick();
        }
    }

    public void printReceiptButtonClick() {
        if (printMerchantCopy) {
            receiptManager.printMerchantReceipt();
        } else {
            receiptManager.printCustomerReceipt();
        }
    }

    private void sendEmailButtonClick() {
        String email = getText(emailEditText);
        if (!AppUtils.validEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_mail));
            return;
        }
        int RECEIPT = 1;
        presenter.sendEmail(transactionData.secureHashKey, email, transactionData.terminalId, transactionData.merchantId,
                transactionData.receiptNumber, transactionData.channelName, transactionData.stan,
                RECEIPT);
    }


    void hideSendNotificationEmailLayout() {
        sendEmailNotificationLayout.setVisibility(View.GONE);
    }

    void hideSendEmailLayout() {
        sendEmailLayout.setVisibility(View.GONE);
    }


    private void showSendNotificationEmailLayout() {
        sendEmailNotificationLayout.setVisibility(View.VISIBLE);
    }

    private void showSendEmailLayout() {
        sendEmailLayout.setVisibility(View.VISIBLE);
    }

    void showSentEmailSuccessLayout() {
        sendEmailSuccessLayout.setVisibility(View.VISIBLE);
    }


    public void setSendEmailSuccess(String email, int operationType) {
        String sendMail = getString(R.string.mail_sent_to) + " " + email;
        int EMAIL = 2;
        if (operationType == EMAIL) {
            hideSendNotificationEmailLayout();
            hideSendEmailLayout();
            showSentEmailSuccessLayout();
        }
        AppUtils.showHtmlText(mailSentTextView, sendMail);
    }

    @Override
    public void showErrorToast(int error) {
        ToastUtils.showLongToast(activity, getString(error));
    }

    @Override
    public void onPrintSuccess() {
        if (printMerchantCopy) {
            printMerchantCopy = false;
            printReceipt.setText(R.string.customer_copy);
        }
    }

    @Override
    public void onPrintFail() {
        Toast.makeText(getContext(), "Printer failure", Toast.LENGTH_SHORT).show();
    }
}
