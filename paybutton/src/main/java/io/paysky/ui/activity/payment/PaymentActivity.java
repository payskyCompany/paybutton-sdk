package io.paysky.ui.activity.payment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paybutton.R;

import io.paysky.data.model.PaymentData;
import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.base.BaseActivity;
import io.paysky.ui.dialog.DialogAgreeButtonClick;
import io.paysky.ui.dialog.DialogCancelButtonClick;
import io.paysky.ui.dialog.InfoDialog;
import io.paysky.ui.fragment.manualpayment.ManualPaymentFragment;
import io.paysky.ui.fragment.qr.QrCodePaymentFragment;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;
import io.paysky.util.DialogUtils;
import io.paysky.util.LocaleHelper;
import io.paysky.util.PrefsUtils;
import io.paysky.util.ToastUtils;
import io.paysky.util.TransactionManager;
import me.grantland.widget.AutofitHelper;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    //GUI.
    private ImageView headerBackImage;
    private LinearLayout cardPaymentLayout;
    private LinearLayout qrPaymentLayout;
    private TextView currencyTextView;
    private TextView amountTextView;
    private TextView merchantNameTextView;
    //Objects,
    public static Bitmap qrBitmap;
    private PaymentData paymentData;
    //Variables.
    private static boolean NORMAL_CLOSE = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefsUtils.initialize(this);
        makeActivityFullScreen();
        AppUtils.preventScreenshot(this);
        setContentView(R.layout.activity_pay);
        hideActionBar();
        initView();
        paymentData = getIntent().getExtras().getParcelable(AppConstant.BundleKeys.PAYMENT_DATA);
        // show activity layout.
        AutofitHelper.create(merchantNameTextView);
        merchantNameTextView.setText(paymentData.merchantName);
        String amount = AppUtils.currencyFormat(paymentData.amountFormatted);
        paymentData.executedTransactionAmount = amount;
        amountTextView.setText(amount);
        currencyTextView.setText(paymentData.currencyName);
        showPaymentBasedOnPaymentOptions(paymentData.paymentMethod);
    }


    public void makeActivityFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void hideActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }


    //GUI Methods.
    private void initView() {
        headerBackImage = findViewById(R.id.header_back_imageView);
        headerBackImage.setOnClickListener(this);
        merchantNameTextView = findViewById(R.id.pb_merchant_name_textView);
        currencyTextView = findViewById(R.id.currency_textView);
        amountTextView = findViewById(R.id.amount_textView);
        TextView languageTextView = findViewById(R.id.language_textView);
        languageTextView.setOnClickListener(this);
        TextView termsTextView = findViewById(R.id.terms_conditions_textView);
        termsTextView.setOnClickListener(this);
        termsTextView.setOnClickListener(this);
        cardPaymentLayout = findViewById(R.id.card_payment_layout);
        qrPaymentLayout = findViewById(R.id.qr_payment_layout);
        cardPaymentLayout.setOnClickListener(this);
        qrPaymentLayout.setOnClickListener(this);
    }


    public void showPaymentBasedOnPaymentOptions(int paymentOptions) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
        switch (paymentOptions) {
            case 0:
                // card payment enabled.
                cardPaymentLayout.setVisibility(View.VISIBLE);
                cardPaymentLayout.setOnClickListener(this);
                showCardPaymentFragment(bundle);
                changePaymentOptionButton(1);
                break;
            case 1:
                // wallet payment enabled.
                qrPaymentLayout.setVisibility(View.VISIBLE);
                qrPaymentLayout.setOnClickListener(this);
                showQrPaymentFragment(bundle);
                changePaymentOptionButton(2);
                break;
            case 2:
                // both payments enabled.
                cardPaymentLayout.setVisibility(View.VISIBLE);
                cardPaymentLayout.setOnClickListener(this);
                qrPaymentLayout.setVisibility(View.VISIBLE);
                qrPaymentLayout.setOnClickListener(this);
                showCardPaymentFragment(bundle);
                break;
        }
    }


    public void showCardPaymentFragment(Bundle bundle) {
        replaceFragmentAndRemoveOldFragment(ManualPaymentFragment.class, bundle);
    }


    public void showQrPaymentFragment(Bundle bundle) {
        replaceFragmentAndRemoveOldFragment(QrCodePaymentFragment.class, bundle);
    }


    public Context getContext() {
        return this;
    }

    public void setHeaderIcon(int icon) {
        headerBackImage.setImageResource(icon);
    }


    public void setHeaderIconClickListener(View.OnClickListener clickListener) {
        headerBackImage.setOnClickListener(clickListener);
    }


    public void replaceFragmentAndRemoveOldFragment(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        replaceFragment(fragmentClass, bundle, false);
    }


    public void replaceFragmentAndAddOldToBackStack(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        replaceFragment(fragmentClass, bundle, true);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.header_back_imageView) {
            onBackPressed();
        } else if (i == R.id.language_textView) {
            // change app language.
            LocaleHelper.changeAppLanguage(this);
            NORMAL_CLOSE = false;
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
            startActivity(new Intent(this, PaymentActivity.class).putExtras(bundle));
            finish();
        } else if (i == R.id.terms_conditions_textView) {
            // show terms dialog.
            DialogUtils.showTermsAndConditionsDialog(this);
        } else if (i == R.id.card_payment_layout) {
            changePaymentOptionButton(1);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
            showCardPaymentFragment(bundle);
        } else if (i == R.id.qr_payment_layout) {
            changePaymentOptionButton(2);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData);
            showQrPaymentFragment(bundle);
        }
    }

    private void changePaymentOptionButton(int type) {
        if (type == 1) {
            // manual payment.
            cardPaymentLayout.setBackgroundResource(R.drawable.payment_option_selected);
            TextView manualTextView = cardPaymentLayout.findViewById(R.id.card_payment_textView);
            manualTextView.setTextColor(getResources().getColor(android.R.color.white));
            if (LocaleHelper.getLocale().equals("ar")) {
                manualTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_card_white, 0);
            } else {
                manualTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card_white, 0, 0, 0);
            }
            qrPaymentLayout.setBackgroundResource(R.drawable.payment_option_unselected);
            TextView qrTextView = qrPaymentLayout.findViewById(R.id.qr_payment_textView);
            qrTextView.setTextColor(getResources().getColor(R.color.font_gray_color3));
            if (LocaleHelper.getLocale().equals("ar")) {
                qrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wallet_gray, 0);
            } else {
                qrTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wallet_gray, 0, 0, 0);
            }
        } else {
            // qr payment.
            cardPaymentLayout.setBackgroundResource(R.drawable.payment_option_unselected);
            TextView manualTextView = cardPaymentLayout.findViewById(R.id.card_payment_textView);
            manualTextView.setTextColor(getResources().getColor(R.color.font_gray_color3));
            if (LocaleHelper.getLocale().equals("ar")) {
                manualTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_card_black, 0);
            } else {
                manualTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card_black, 0, 0, 0);
            }
            qrPaymentLayout.setBackgroundResource(R.drawable.payment_option_selected);
            TextView qrTextView = qrPaymentLayout.findViewById(R.id.qr_payment_textView);
            qrTextView.setTextColor(getResources().getColor(android.R.color.white));
            if (LocaleHelper.getLocale().equals("ar")) {
                qrTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wallet_white, 0);
            } else {
                qrTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wallet_white, 0, 0, 0);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (NORMAL_CLOSE) {
            qrBitmap = null;
            TransactionManager.sendTransactionEvent();
        } else {
            PaymentActivity.NORMAL_CLOSE = true;
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    public void showManualPayment() {
        cardPaymentLayout.performClick();
    }

    public void hidePaymentOptions() {
        qrPaymentLayout.setVisibility(View.GONE);
        cardPaymentLayout.setVisibility(View.GONE);
    }


}
