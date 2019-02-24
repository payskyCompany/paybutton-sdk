package io.paysky.ui.fragment.manualpayment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.paysky.paybutton.R;

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
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, 1005);
        ToastUtils.showLongToast(getActivity(), getString(R.string.allow_light_scan));
    }


    private boolean isInputsValid(String cardNumber, String ownerName, String expireDate, String ccv) {
        boolean isValidInputs = true;
        if (!cardNumberEditText.isValid() || !CardsValidation.isCardValid(cardNumber)) {
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
        if (requestCode == 1005) {
            // get result of read card data.
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard creditCard = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                // success.
                char[] chars = creditCard.cardNumber.toCharArray();
                StringBuilder numberBuilder = new StringBuilder();
                for (char aChar : chars) {
                    numberBuilder.append(aChar);
                }
                cardNumberEditText.setText(numberBuilder.toString());
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


    public void show3dpWebView(String webBody, String url, int gatewayType, PaymentData paymentData) {
        Bundle bundle = new Bundle();
        bundle.putString("request_body", webBody);
        bundle.putString("url", url);
        bundle.putInt("gateway_type", gatewayType);
        bundle.putString("card_number", cardNumber);
        bundle.putString("expiry_date", expireDate);
        bundle.putString("cvv", ccv);
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
        activity.replaceFragmentAndRemoveOldFragment(WebPaymentFragment.class, bundle);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
