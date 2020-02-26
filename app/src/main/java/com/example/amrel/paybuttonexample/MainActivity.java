package com.example.amrel.paybuttonexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.PayButton;
import io.paysky.util.AllURLsStatus;
import io.paysky.util.AppUtils;
import io.paysky.util.LocaleHelper;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    //GUI.
    private EditText merchantIdEditText, terminalIdEditText, amountEditText;
    private TextView paymentStatusTextView;
    private TextView languageTextView;
    private EditText currencyEditText;
    private Spinner spinner_type;

    String[] list_to_show = {"PRODUCTION", "TESTING", "UPG_STAGING", "UPG_PRODUCTION"};
    AllURLsStatus[] list_to_URLS = {AllURLsStatus.PRODUCTION, AllURLsStatus.GREY,
            AllURLsStatus.UPG_STAGING, AllURLsStatus.UPG_PRODUCTION};
    int item_position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner_type = findViewById(R.id.spinner_type);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_to_show);
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


        // find views.
        TextView payTextView = (TextView) findViewById(R.id.pay_textView);
        merchantIdEditText = findViewById(R.id.merchant_id_editText);
        terminalIdEditText = findViewById(R.id.terminal_id_editText);
        amountEditText = findViewById(R.id.amount_editText);
        paymentStatusTextView = findViewById(R.id.payment_status_textView);
        currencyEditText = findViewById(R.id.currency_editText);
        payTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentStatusTextView.setText("");
                String terminalId = terminalIdEditText.getText().toString().trim();
                String merchantId = merchantIdEditText.getText().toString().trim();
        /*        String merchantId = "43808";
                String terminalId = "222222";*/
                String amount = amountEditText.getText().toString().trim();

                boolean hasErrors = false;
                if (terminalId.isEmpty()) {
                    terminalIdEditText.setError(getString(R.string.required));
                    hasErrors = true;
                }
                if (merchantId.isEmpty()) {
                    merchantIdEditText.setError(getString(R.string.required));
                    hasErrors = true;
                }
                if (amount.isEmpty()) {
                    amountEditText.setError(getString(R.string.required));
                    hasErrors = true;
                }

                if (hasErrors) {
                    return;
                }

                // add payments data.
                PayButton payButton = new PayButton(MainActivity.this);
                payButton.setMerchantId(merchantId); // Merchant id
                payButton.setTerminalId(terminalId); // Terminal  id
                payButton.setAmount(Double.valueOf(amount)); // Amount
                String a = currencyEditText.getText().toString();
                if (a.isEmpty()) {
                    payButton.setCurrencyCode(0); // Currency Code
                } else {
                    payButton.setCurrencyCode(Integer.valueOf(a)); // Currency Code
                }

                payButton.setMerchantSecureHash("33383162386166332D373634652D346638372D393432332D636338623335633630363137");
               // payButton.setMerchantSecureHash("65613962386534372D383936362D343166322D383838622D323062373865623039303461");
                payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());
                payButton.setProductionStatus(list_to_URLS[item_position]);
                payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                    @Override
                    public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
                        paymentStatusTextView.setText(cardTransaction.toString());
                    }

                    @Override
                    public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
                        paymentStatusTextView.setText(walletTransaction.toString());
                    }

                    @Override
                    public void onError(TransactionException error) {
                        paymentStatusTextView.setText("failed by:- " + error.errorMessage);
                    }
                });
            }
        });
        TextView appVersion = findViewById(R.id.app_version_textView);
        appVersion.setText("PaySDK - PayButton module - Ver.  " + AppUtils.getVersionNumber(this));
        ImageView logoImageView = findViewById(R.id.logo_imageView);
        logoImageView.setOnLongClickListener(this);
        languageTextView = findViewById(R.id.language_textView);
        languageTextView.setOnClickListener(this);
        if (LocaleHelper.getLocale().equals("ar")) {
            languageTextView.setText(R.string.english);
            merchantIdEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            terminalIdEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            amountEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        } else {
            languageTextView.setText(R.string.arabic);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        startActivity(new Intent(this, SettingActivity.class));
        return true;
    }

    @Override
    public void onClick(View view) {
        LocaleHelper.changeAppLanguage(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
