package io.paysky.util;

import android.content.Intent;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paybutton.R;

import io.paysky.data.model.ReceiptData;

public class ReceiptManager {

    //GUI.
    private View receiptView;
    private TextView merchantNameTextView;
    private TextView receiptDate;
    private TextView receiptTime;
    private TextView receiptMid;
    private TextView receiptTid;
    private TextView receiptNumberTextView;
    private TextView transactionTypeTextView;
    private TextView cardNumber;
    private TextView cardType;
    private TextView cardHolderName;
    private TextView stan;
    private TextView rrn;
    private TextView authNumber;
    private TextView total;
    private TextView transactionPayType;
    private LinearLayout signLayout;
    private TextView receiptPrintTo;
    //Objects
    private ReceiptData receiptData;
    private PrintReceiptListener printReceiptListener;

    public ReceiptManager(View parentView, ReceiptData receiptData, PrintReceiptListener printReceiptListener) {
        receiptView = parentView.findViewById(R.id.receipt_layout);
        this.receiptData = receiptData;
        this.printReceiptListener = printReceiptListener;
        initView(receiptView);
    }


    private void initView(View receiptView) {
        merchantNameTextView = receiptView.findViewById(R.id.pb_merchant_name_textView);
        receiptDate = receiptView.findViewById(R.id.pb_receipt_date);
        receiptTime = receiptView.findViewById(R.id.pb_receipt_time);
        receiptMid = receiptView.findViewById(R.id.pb_receipt_mid);
        receiptTid = receiptView.findViewById(R.id.pb_receipt_tid);
        receiptNumberTextView = receiptView.findViewById(R.id.pb_receipt_number);
        transactionTypeTextView = receiptView.findViewById(R.id.pb_transaction_type_info);
        cardNumber = receiptView.findViewById(R.id.pb_card_number);
        cardType = receiptView.findViewById(R.id.pb_card_type);
        cardHolderName = receiptView.findViewById(R.id.pb_card_holder_name);
        stan = receiptView.findViewById(R.id.pb_stan);
        rrn = receiptView.findViewById(R.id.pb_rrn);
        authNumber = receiptView.findViewById(R.id.pb_auth_number);
        total = receiptView.findViewById(R.id.pb_total);
        transactionPayType = receiptView.findViewById(R.id.pb_transaction_pay_type);
        receiptPrintTo = receiptView.findViewById(R.id.pb_receipt_print_to);
        signLayout = receiptView.findViewById(R.id.pb_sign_layout);
     /*   AppUtils.applyFont("receipt_font.ttf", merchantNameTextView, receiptDate, receiptTime,
                receiptMid, receiptTid, receiptNumberTextView, transactionTypeTextView, cardNumber,
                 cardType, cardHolderName, stan, rrn, authNumber
                , total, transactionPayType, receiptPrintTo
        );*/
    }


    public void printMerchantReceipt() {
        receiptPrintTo.setText(R.string.merchant_copy__);
        setReceiptData();
        printReceipt();
    }


    public void printCustomerReceipt() {
        receiptPrintTo.setText(R.string.customer_copy__);
        setReceiptData();
        printReceipt();
    }

    private void setReceiptData() {
        merchantNameTextView.setText(receiptData.merchantName);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        receiptDate.setText(String.format("Date: %d-%d-%d", today.monthDay, (today.month + 1), today.year));
        receiptTime.setText(String.format("Time: %d:%d:%d", today.hour, today.minute, today.second));
        receiptMid.setText("MID: " + receiptData.merchantId);
        receiptTid.setText("TID: " + receiptData.terminalId);
        receiptNumberTextView.setText("Receipt #: " + receiptData.receiptNumber);
        transactionTypeTextView.setText(receiptData.transactionType);
        cardHolderName.setText(receiptData.cardHolderName);
        String card = receiptData.cardNumber;
        if (receiptData.channelName.equals(AppConstant.TransactionChannelName.TAHWEEL)) {
            cardType.setText(AppConstant.TransactionChannelName.TAHWEEL);
            transactionPayType.setText(R.string.qr);
            this.cardNumber.setText(card);
            stan.setVisibility(View.GONE);
            rrn.setVisibility(View.GONE);
            authNumber.setVisibility(View.GONE);
            signLayout.setVisibility(View.GONE);
        } else {
            StringBuilder hiddenCard = new StringBuilder();
            char[] chars = card.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 12 || i == 13 || i == 14 || i == 15) {
                    hiddenCard.append(chars[i]);
                } else {
                    hiddenCard.append("X");
                }
            }
            cardNumber.setText(hiddenCard.toString());
            transactionPayType.setText(receiptData.paymentType);
            if (card.startsWith("4")) {
                cardType.setText("VISA");
            } else {
                cardType.setText("MasterCard");
            }
        }
        stan.setText("STAN: " + receiptData.stan);
        rrn.setText("RRN: " + receiptData.rrn);
        authNumber.setText("Auth No #: " + receiptData.authNumber);
        total.setText("TOTAL :      " + "EGP " + receiptData.amount);
    }

    private void printReceipt() {
        // get layout image to print.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                receiptView.setDrawingCacheEnabled(true);
                receiptView.measure(View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                receiptView.layout(0, 0, receiptView.getMeasuredWidth(), receiptView.getMeasuredHeight());
                receiptView.buildDrawingCache();
                final Bitmap myBitmap = Bitmap.createBitmap(receiptView.getDrawingCache());
                receiptView.setDrawingCacheEnabled(false); // clear drawing cache

                // print image.
                PrinterManager printer = new PrinterManager();
                printer.setupPage(-1, -1);
                int ret = printer.drawBitmap(myBitmap, 0, 0);
                int retprinter = printer.printPage(0);
                if (retprinter != 0) {
                    printReceiptListener.onPrintFail();
                } else {
                    Intent intent = new Intent("urovo.prnt.message");
                    intent.putExtra("ret", ret);
                    receiptView.getContext().sendBroadcast(intent);
                    printer.prn_paperForWard(15);
                    printReceiptListener.onPrintSuccess();
                }
            }
        }, 100);
    }

}

