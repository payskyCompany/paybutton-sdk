package com.example.amrel.paybuttonexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.PayButton;
import io.paysky.util.AppUtils;
import io.paysky.util.LocaleHelper;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    //GUI.
    private EditText merchantIdEditText, terminalIdEditText, amountEditText;
    private TextView paymentStatusTextView;
    private EditText currencyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                payButton.setProduction(false);
                payButton.setMerchantId(merchantId);
                payButton.setTerminalId(terminalId);
                payButton.setAmount(Double.valueOf(amount));
                String currency = currencyEditText.getText().toString();
                if (currency.isEmpty()) {
                    payButton.setCurrencyCode(0);
                } else {
                    payButton.setCurrencyCode(Integer.valueOf(currency));
                }

                payButton.setMerchantSecureHash("63616133323632652D636439312D346435312D623832312D643665666539653633626638");
                payButton.setTransactionReferenceNumber("1");

                payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                    @Override
                    public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
                        new AlertDialog.Builder(MainActivity.this).setMessage(cardTransaction.toString()).setPositiveButton("OK", null).show();
                    }

                    @Override
                    public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
                        new AlertDialog.Builder(MainActivity.this).setMessage(walletTransaction.toString()).setPositiveButton("OK", null).show();
                    }

                    @Override
                    public void onError(TransactionException error) {
                        new AlertDialog.Builder(MainActivity.this).setMessage("failed by:- " + error.errorMessage).setPositiveButton("OK", null).show();
                    }
                });
            }
        });
        TextView appVersion = findViewById(R.id.app_version_textView);
        appVersion.setText("PaySDK - PayButton module - Ver.  " + AppUtils.getVersionNumber(this));
        ImageView logoImageView = findViewById(R.id.logo_imageView);
        logoImageView.setOnLongClickListener(this);
        TextView languageTextView = findViewById(R.id.language_textView);
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
