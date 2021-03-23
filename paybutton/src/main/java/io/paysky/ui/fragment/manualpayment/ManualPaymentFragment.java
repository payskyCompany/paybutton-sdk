package io.paysky.ui.fragment.manualpayment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paybutton.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import io.paysky.data.model.PaymentData;
import io.paysky.data.model.ReceiptData;
import io.paysky.ui.base.BaseFragment;
import io.paysky.ui.custom.CardEditText;
import io.paysky.ui.fragment.paymentfail.PaymentFailedFragment;
import io.paysky.ui.fragment.paymentsuccess.PaymentApprovedFragment;
import io.paysky.ui.fragment.webview.WebPaymentFragment;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;
import io.paysky.util.CardsValidation;
import io.paysky.util.LocaleHelper;
import io.paysky.util.ToastUtils;


public class ManualPaymentFragment extends BaseFragment implements ManualPaymentView, View.OnClickListener {


    //Objects
    private ManualPaymentPresenter presenter;
    //GUI.
    private CardEditText cardNumberEditText;
    private EditText cardOwnerNameEditText;
    private EditText expireDateEditText;
    private EditText ccvEditText;
    private Button proceedButton;
    private String cardNumber;
    private String expireDate;
    private ImageView scanCardImageView;
    private String ccv;


    static final int MY_SCAN_REQUEST_CODE = 1;


    public ManualPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get data from bundle.
        presenter = new ManualPaymentPresenter(getArguments());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_manual_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView(view);
    }

    private void initView(View view) {
        activity.setHeaderIcon(R.drawable.ic_back);
        activity.setHeaderIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        ImageView cardTypeImageView = view.findViewById(R.id.card_type_imageView);
        cardNumberEditText = view.findViewById(R.id.card_number_editText);
        cardNumberEditText.setCardTypeImage(cardTypeImageView);
        cardOwnerNameEditText = view.findViewById(R.id.card_owner_name_editText);
        expireDateEditText = view.findViewById(R.id.expire_date_editText);
        ccvEditText = view.findViewById(R.id.ccv_editText);
        proceedButton = view.findViewById(R.id.proceed_button);
        proceedButton.setOnClickListener(this);
        if (LocaleHelper.getLocale().equals("ar")) {
            cardNumberEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            expireDateEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            ccvEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
        scanCardImageView = view.findViewById(R.id.scan_camera_imageView);
        scanCardImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.equals(proceedButton)) {
            makePaymentClick();
        } else if (view.equals(scanCardImageView)) {
            onScanCardCameraButtonClick();
        }
    }

    private void makePaymentClick() {
        // get manual payment data.
        cardNumber = getText(cardNumberEditText).replaceAll(" ", "");
        String cardOwnerName = getText(cardOwnerNameEditText);
        expireDate = getText(expireDateEditText).replaceAll("/", "");
        ccv = getText(ccvEditText);
        if (!isInputsValid(cardNumber, cardOwnerName, expireDate, ccv)) return;
        // replace expire date.
        String month = expireDate.substring(0, 2);
        String year = expireDate.substring(2);
        expireDate = year + month;

        AppUtils.hideKeyboard(proceedButton);

        presenter.makePayment(cardNumber, expireDate, cardOwnerName, ccv);
    }


    private void onScanCardCameraButtonClick() {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
        ToastUtils.showLongToast(getActivity(), getString(R.string.allow_light_scan));

    }


    private boolean isInputsValid(String cardNumber, String ownerName, String expireDate, String ccv) {
        boolean isValidInputs = true;


        if (cardNumberEditText.getText().toString().isEmpty()) {
            isValidInputs = false;
            cardNumberEditText.setError(getString(R.string.invalid_card_number_length));
        }
        if (!cardNumberEditText.getText().toString().isEmpty()
                && !CardsValidation.luhnCheck(cardNumber)) {
            isValidInputs = false;
            cardNumberEditText.setError(getString(R.string.invalid_card_number_length));
        }

        if (isEmpty(ownerName)) {
            isValidInputs = false;
            cardOwnerNameEditText.setError(getString(R.string.enter_valid_owner));
        }

        if (isEmpty(expireDate) || expireDate.length() < 4) {
            isValidInputs = false;
            expireDateEditText.setError(getString(R.string.invalid_expire_date));
        } else {
            // validate that expire date large than today.
            int enteredMonth = Integer.parseInt(expireDate.substring(0, 2));
            int enteredYear = Integer.parseInt(expireDate.substring(2));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.US);
            String date = sdf.format(new Date());
            String month = date.substring(0, date.indexOf("/"));
            String year = date.substring(date.indexOf("/") + 1);
            int monthNumber = Integer.valueOf(month);
            int yearNumber = Integer.valueOf(year);
            if (enteredYear < yearNumber) {
                // invalid year.
                isValidInputs = false;
                expireDateEditText.setError(getString(R.string.invalid_expire_date_date));
            } else if (enteredYear == yearNumber) {
                // validate month.
                if (enteredMonth < monthNumber) {
                    isValidInputs = false;
                    expireDateEditText.setError(getString(R.string.invalid_expire_date_date));
                }
            }
        }

        if (isEmpty(ccv) || ccv.length() < 3) {
            isValidInputs = false;
            ccvEditText.setError(getString(R.string.invalid_ccv));
        }

        if (ccv.equals("000")) {
            isValidInputs = false;
            ccvEditText.setError(getString(R.string.invalid_ccv_value));
        }

        return isValidInputs;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            // get result of read card data.
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard creditCard = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                // success.
                char[] chars = creditCard.cardNumber.toCharArray();
                StringBuilder numberBuilder = new StringBuilder();
                for (int i = 0; i < chars.length; i++) {
                    numberBuilder.append(chars[i]);
                    if (i + 1 % 4 == 0 && i + 1 != chars.length) {
                        numberBuilder.append(" ");
                    }
                }
                cardNumberEditText.setText(numberBuilder.toString());


                if (creditCard.isExpiryValid()) {
                    String month;
                    if (String.valueOf(creditCard.expiryMonth).length() < 2)
                        month = "0" + creditCard.expiryMonth;
                    else month = String.valueOf(creditCard.expiryMonth);
                    String resultDisplayStr = month + "/" + String.valueOf(creditCard.expiryYear).substring(2, 4) + "\n";
                    expireDateEditText.setText(resultDisplayStr);
                }

                if (creditCard.cardholderName != null) {
                    cardOwnerNameEditText.setText(creditCard.cardholderName);
                }
            }
        }
    }


    @Override
    public void showTransactionApprovedFragment(String transactionNumber, String approvalCode,
                                                String retrievalRefNr, String cardHolderName, String cardNumber, String systemTraceNumber, PaymentData paymentData) {
        Bundle bundle = new Bundle();
        ReceiptData receiptData = new ReceiptData();
        receiptData.rrn = transactionNumber;
        receiptData.authNumber = approvalCode;
        receiptData.channelName = AppConstant.TransactionChannelName.CARD;
        receiptData.refNumber = retrievalRefNr;
        receiptData.receiptNumber = retrievalRefNr;
        receiptData.amount = paymentData.amountFormatted;
        receiptData.cardHolderName = cardHolderName;
        receiptData.cardNumber = cardNumber;
        receiptData.merchantName = paymentData.merchantName;
        receiptData.merchantId = paymentData.merchantId;
        receiptData.terminalId = paymentData.terminalId;
        receiptData.paymentType = ReceiptData.PaymentDoneBy.MANUAL.toString();
        receiptData.stan = systemTraceNumber;
        receiptData.transactionType = ReceiptData.TransactionType.SALE.name();
        receiptData.secureHashKey = paymentData.secureHashKey;
        bundle.putParcelable(AppConstant.BundleKeys.RECEIPT, receiptData);
        activity.replaceFragmentAndRemoveOldFragment(PaymentApprovedFragment.class, bundle);
        activity.hidePaymentOptions();
    }


    @Override
    public void showErrorInServerToast() {
        ToastUtils.showLongToast(activity, getString(R.string.error_try_again));
    }

    @Override
    public void showPaymentFailedFragment(Bundle bundle) {
        activity.replaceFragmentAndRemoveOldFragment(PaymentFailedFragment.class, bundle);
    }


    public void show3dpWebView(String url, PaymentData paymentData) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);

        activity.replaceFragmentAndRemoveOldFragment(WebPaymentFragment.class, bundle);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
