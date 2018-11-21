package com.example.amrel.paybuttonexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox enableManual;
    private CheckBox enableQr;
    private CheckBox enableMagnetic;
    private Spinner defaultPaymentSpinner;
    private Spinner serverLinksSpinner;
    private Button saveButton;
    private ArrayList<String> defaultPayments;
    private ArrayList<String> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        showViewsData();
    }

    private void showViewsData() {
        boolean enableManualValue = SettingPrefs.getBooleanPrefs(this, "enable_manual", true);
        boolean enableMagneticValue = SettingPrefs.getBooleanPrefs(this, "enable_magnetic", true);
        boolean enableQrValue = SettingPrefs.getBooleanPrefs(this, "enable_qr", true);
        enableManual.setChecked(enableManualValue);
        enableMagnetic.setChecked(enableMagneticValue);
        enableQr.setChecked(enableQrValue);
    }

    private void initView() {
        enableManual = findViewById(R.id.enable_manual);
        enableQr = findViewById(R.id.enable_qr);
        enableMagnetic = findViewById(R.id.enable_magnetic);
        defaultPaymentSpinner = findViewById(R.id.default_payment_spinner);
        serverLinksSpinner = findViewById(R.id.server_links_spinner);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        links = new ArrayList<>();
        links.add("http://197.50.37.116:4006/");
        links.add("http://grey.paysky.io:4006/");
        defaultPayments = new ArrayList<>();
        defaultPayments.add("Manual");
        defaultPayments.add("Magnetic");
        defaultPayments.add("QR");
        ArrayAdapter<String> payAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, defaultPayments);
        defaultPaymentSpinner.setAdapter(payAdapter);
        ArrayAdapter<String> linkAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, links);
        serverLinksSpinner.setAdapter(linkAdapter);
    }

    @Override
    public void onClick(View view) {
        boolean enableManualValue = enableManual.isChecked();
        boolean enableMagneticValue = enableMagnetic.isChecked();
        boolean enableQrValue = enableQr.isChecked();
        SettingPrefs.saveBooleanPrefs(this, "enable_manual", enableManualValue);
        SettingPrefs.saveBooleanPrefs(this, "enable_magnetic", enableMagneticValue);
        SettingPrefs.saveBooleanPrefs(this, "enable_qr", enableQrValue);
        String defaultPayment = defaultPayments.get(defaultPaymentSpinner.getSelectedItemPosition());
        String serverLink = links.get(serverLinksSpinner.getSelectedItemPosition());
        SettingPrefs.saveStringPrefs(this, "default_payment", defaultPayment);
        SettingPrefs.saveStringPrefs(this, "server_link", serverLink);
        finish();
    }
}
