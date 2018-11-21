package io.paysky.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReceiptData implements Parcelable {
    public String merchantName;
    public String merchantId, terminalId;
    public String receiptNumber;
    public String transactionType; // sale or void.
    public String cardNumber;
    public String cardType;
    public String cardHolderName = "TEST";
    public String stan, rrn, authNumber;
    public String amount;
    public String paymentType;
    public String refNumber, channelName;
    public String secureHashKey;


    public ReceiptData() {
    }

    protected ReceiptData(Parcel in) {
        merchantName = in.readString();
        merchantId = in.readString();
        terminalId = in.readString();
        receiptNumber = in.readString();
        transactionType = in.readString();
        cardNumber = in.readString();
        cardType = in.readString();
        cardHolderName = in.readString();
        stan = in.readString();
        rrn = in.readString();
        authNumber = in.readString();
        amount = in.readString();
        paymentType = in.readString();
        refNumber = in.readString();
        channelName = in.readString();
        secureHashKey = in.readString();
    }

    public static final Creator<ReceiptData> CREATOR = new Creator<ReceiptData>() {
        @Override
        public ReceiptData createFromParcel(Parcel in) {
            return new ReceiptData(in);
        }

        @Override
        public ReceiptData[] newArray(int size) {
            return new ReceiptData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(merchantName);
        parcel.writeString(merchantId);
        parcel.writeString(terminalId);
        parcel.writeString(receiptNumber);
        parcel.writeString(transactionType);
        parcel.writeString(cardNumber);
        parcel.writeString(cardType);
        parcel.writeString(cardHolderName);
        parcel.writeString(stan);
        parcel.writeString(rrn);
        parcel.writeString(authNumber);
        parcel.writeString(amount);
        parcel.writeString(paymentType);
        parcel.writeString(refNumber);
        parcel.writeString(channelName);
        parcel.writeString(secureHashKey);
    }

    public enum TransactionType {
        SALE, VOID;
    }

    public enum PaymentDoneBy {
        MANUAL, MAGNETIC, EMV;
    }


}
