package com.example.amrel.paybuttonexample;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.PayButton;
import io.paysky.util.AllURLsStatus;
import io.paysky.util.AppUtils;
import io.paysky.util.LocaleHelper;

public class MainActivity extends AppCompatActivity
        implements View.OnLongClickListener, View.OnClickListener {

    private EditText merchantIdEditText, terminalIdEditText,
            amountEditText, secureHashKeyEditText, transactionRefNumberEditText,
            customerIdEditText, emailTextField, mobileNumberTextField;
    private TextView paymentStatusTextView;
    private TextView payTextView;
    private EditText currencyEditText;
    private Spinner spinner_type, authTypeSpinner;

    private View notSubscribedLayout, subscribedLayout, emailLayout, mobileNumberLayout;
    private View notSubscribedLine, subscribedLine, customerIdLayout;
    private TextView notSubscribedTextView, subscribedTextView;
    private Boolean isSubscribed = false;

    private final String[] list_to_show = {"PRODUCTION", "TESTING"};
    private String[] authenticationTypes;
    private final AllURLsStatus[] list_to_URLS = {AllURLsStatus.PRODUCTION, AllURLsStatus.GREY};
    private int item_position = 0;

    private int selectedAuthType = -1;
    private final int MOBILE_INDEX = 1, EMAIL_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authenticationTypes = new String[]{getString(R.string.auth_spinner_hint),
                getString(R.string.mobile_number_hint), getString(R.string.email_address)};

        linkViewsWithIds();

        initializePaymentTypesSpinner();
        initializeAuthTypeSpinner();


        setupPayButton();
        setupSubscribedNotSubscribedView();

        TextView appVersion = findViewById(R.id.app_version_textView);
        appVersion.setText(getString(R.string.paybutton_version_formatted,
                AppUtils.getVersionNumber(this)));

        ImageView logoImageView = findViewById(R.id.logo_imageView);
        logoImageView.setOnLongClickListener(this);

        setLanguageTextView();
    }

    private void initializeAuthTypeSpinner() {
        ArrayAdapter<String> authTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, authenticationTypes) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                if (position == 0) {
                    // Set the hint text color gray
                    textView.setTextColor(getResources().getColor(R.color.gray200));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.gray900));
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                if (position == 0) {
                    // Set the hint text color gray
                    textView.setTextColor(getResources().getColor(R.color.gray200));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.gray900));
                }
                return view;
            }
        };
        authTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authTypeSpinner.setAdapter(authTypeAdapter);


        authTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    selectedAuthType = i;
                    if (i == MOBILE_INDEX) {
                        hideAndClearEmail();
                        mobileNumberLayout.setVisibility(View.VISIBLE);
                        payTextView.setEnabled(true);
                    } else if (i == EMAIL_INDEX) {
                        hideAndClearPhone();
                        emailLayout.setVisibility(View.VISIBLE);
                        payTextView.setEnabled(true);
                    }
                } else {
                    hideAndClearPhoneAndEmail();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void hideAndClearPhone() {
        mobileNumberLayout.setVisibility(View.GONE);
        mobileNumberTextField.setText("");
    }

    private void hideAndClearEmail() {
        emailLayout.setVisibility(View.GONE);
        emailTextField.setText("");
    }

    private void setupPayButton() {
        payTextView.setOnClickListener(v -> {
            paymentStatusTextView.setText("");
            String terminalId = terminalIdEditText.getText().toString().trim();
            String merchantId = merchantIdEditText.getText().toString().trim();
            String amount = amountEditText.getText().toString().trim();
            String secureHashKey = secureHashKeyEditText.getText().toString().trim();
            String transactionRefNumber = transactionRefNumberEditText.getText().toString().trim();
            String customerId = customerIdEditText.getText().toString().trim();
            String email = emailTextField.getText().toString().trim();
            String mobileNumber = mobileNumberTextField.getText().toString().trim();

            boolean hasErrors = validateData(
                    terminalId,
                    merchantId,
                    amount,
                    secureHashKey,
                    customerId,
                    mobileNumber,
                    email);

            if (hasErrors) {
                return;
            }

            // add payments data.
            PayButton payButton = new PayButton(MainActivity.this);
            payButton.setMerchantId(merchantId); // Merchant id
            payButton.setTerminalId(terminalId); // Terminal  id
            payButton.setAmount(Double.parseDouble(amount)); // Amount
            payButton.setTransactionReferenceNumber(transactionRefNumber);
            //payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());

            payButton.setMerchantSecureHash(secureHashKey);
            payButton.setProductionStatus(list_to_URLS[item_position]);
            if (isSubscribed) {
                payButton.setCustomerId(customerId);
            } else {
                if (selectedAuthType == MOBILE_INDEX) {
                    payButton.setCustomerMobile(mobileNumber);
                } else if (selectedAuthType == EMAIL_INDEX) {
                    payButton.setCustomerEmail(email);
                }
            }

            String a = currencyEditText.getText().toString();
            if (a.isEmpty()) {
                payButton.setCurrencyCode(0); // Currency Code
            } else {
                payButton.setCurrencyCode(Integer.parseInt(a)); // Currency Code
            }

            payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                @Override
                public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
                    //  paymentStatusTextView.setText(cardTransaction.toString());
                }

                @Override
                public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
                    //  paymentStatusTextView.setText(walletTransaction.toString());
                }

                @Override
                public void onError(TransactionException error) {
                    //     paymentStatusTextView.setText("failed by:- " + error.errorMessage);
                }
            });
        });
    }

    private boolean validateData(String terminalId,
                                 String merchantId,
                                 String amount,
                                 String secureHashKey,
                                 String customerId,
                                 String mobileNumber,
                                 String email) {
        boolean hasErrors = false;
        if (terminalId.isEmpty()) {
            terminalIdEditText.setError(getString(R.string.required));
            hasErrors = true;
        }
        if (merchantId.isEmpty()) {
            merchantIdEditText.setError(getString(R.string.required));
            hasErrors = true;
        }
        if (amount.isEmpty() || amount.equals("0")) {
            amountEditText.setError(getString(R.string.required));
            hasErrors = true;
        }

        if (secureHashKey.isEmpty()) {
            secureHashKeyEditText.setError(getString(R.string.required));
            hasErrors = true;
        }

        if (isSubscribed) {
            if (customerId.isEmpty()) {
                customerIdEditText.setError(getString(R.string.required));
                hasErrors = true;
            }
        } else {
            if (selectedAuthType != -1) {
                if (selectedAuthType == MOBILE_INDEX) {
                    if (mobileNumber.isEmpty()) {
                        mobileNumberTextField.setError(getString(R.string.required));
                        hasErrors = true;
                    }
                } else if (selectedAuthType == EMAIL_INDEX) {
                    if (email.isEmpty()) {
                        emailTextField.setError(getString(R.string.required));
                        hasErrors = true;
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailTextField.setError(getString(R.string.invalid_email));
                        hasErrors = true;
                    }
                }
            } else {
                hasErrors = true;
            }
        }
        return hasErrors;
    }

    private void setupSubscribedNotSubscribedView() {
        notSubscribedLayout.setOnClickListener(view -> {
            isSubscribed = false;
            //hide customer data
            hideAndClearCustomerId();

            //show not subscribed fields
            showNotSubscribedFields();

            payTextView.setEnabled(false);

            setViewAsSelected(notSubscribedTextView, notSubscribedLine);
            setViewAsNotSelected(subscribedTextView, subscribedLine);
        });

        subscribedLayout.setOnClickListener(view -> {
            isSubscribed = true;
            //hide not subscribed items
            hideAndClearNotSubscribedData();

            //show customer id
            customerIdLayout.setVisibility(View.VISIBLE);

            payTextView.setEnabled(true);

            setViewAsSelected(subscribedTextView, subscribedLine);
            setViewAsNotSelected(notSubscribedTextView, notSubscribedLine);
        });
    }

    private void initializePaymentTypesSpinner() {
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_to_show);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(dataAdapter);
        spinner_type.setSelection(item_position);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item_position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLanguageTextView() {
        TextView languageTextView = findViewById(R.id.language_textView);
        languageTextView.setOnClickListener(this);
        if (LocaleHelper.getLocale().equals("ar")) {
            languageTextView.setText(R.string.change_language);
            merchantIdEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            terminalIdEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            amountEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        } else {
            languageTextView.setText(R.string.change_language);
        }
    }

    private void linkViewsWithIds() {
        merchantIdEditText = findViewById(R.id.merchant_id_editText);
        terminalIdEditText = findViewById(R.id.terminal_id_editText);
        amountEditText = findViewById(R.id.amount_editText);
        paymentStatusTextView = findViewById(R.id.payment_status_textView);
        currencyEditText = findViewById(R.id.currency_editText);
        secureHashKeyEditText = findViewById(R.id.secureHash_editText);
        transactionRefNumberEditText = findViewById(R.id.trnx_ref_number_editText);
        customerIdEditText = findViewById(R.id.customer_id_editText);
        emailTextField = findViewById(R.id.email_editText);
        mobileNumberTextField = findViewById(R.id.phone_number_editText);

        notSubscribedLayout = findViewById(R.id.not_subscribed_layout);
        subscribedLayout = findViewById(R.id.subscribed_layout);
        notSubscribedTextView = findViewById(R.id.not_subscribed_textview);
        subscribedTextView = findViewById(R.id.subscribed_textview);
        notSubscribedLine = findViewById(R.id.not_subscribed_line);
        subscribedLine = findViewById(R.id.subscribed_line);

        customerIdLayout = findViewById(R.id.customer_id_layout);
        emailLayout = findViewById(R.id.email_layout);
        mobileNumberLayout = findViewById(R.id.mobile_number_layout);

        spinner_type = findViewById(R.id.spinner_type);
        authTypeSpinner = findViewById(R.id.auth_type_spinner);
        payTextView = findViewById(R.id.pay_textView);
    }

    private void showNotSubscribedFields() {
        authTypeSpinner.setVisibility(View.VISIBLE);
    }

    private void hideAndClearCustomerId() {
        customerIdLayout.setVisibility(View.GONE);
        customerIdEditText.setText("");
    }

    private void hideAndClearNotSubscribedData() {
        authTypeSpinner.setSelection(0);
        authTypeSpinner.setVisibility(View.GONE);

        hideAndClearPhoneAndEmail();
    }

    private void hideAndClearPhoneAndEmail() {
        hideAndClearEmail();

        hideAndClearPhone();
    }

    private void setViewAsNotSelected(TextView textView, View line) {
        textView.setTextColor(getResources().getColor(R.color.gray100));
        textView.setTypeface(null, Typeface.NORMAL);
        line.setBackgroundColor(getResources().getColor(R.color.gray100));
    }

    private void setViewAsSelected(TextView textView, View line) {
        textView.setTextColor(getResources().getColor(R.color.blue100));
        textView.setTypeface(null, Typeface.BOLD);
        line.setBackgroundColor(getResources().getColor(R.color.blue100));
    }

    @Override
    public boolean onLongClick(View view) {
        startActivity(new Intent(this, SettingActivity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        LocaleHelper.changeAppLanguage(this);
        recreate();
    }
}