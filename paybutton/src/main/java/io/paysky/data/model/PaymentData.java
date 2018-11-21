package io.paysky.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentData implements Parcelable {

    public String merchantId, terminalId;
    public String merchantName, receiverMail;
    public double amount;
    public String amountFormatted;
    public String secureHashKey;
    public String currencyCode;
    public String transactionReferenceNumber;
    public boolean is3dsEnabled;
    public int paymentMethod;
    public boolean isTahweel, isVisa;
    public String currencyName;
    public String executedTransactionAmount;

    public PaymentData() {

    }

    protected PaymentData(Parcel in) {
        merchantId = in.readString();
        terminalId = in.readString();
        merchantName = in.readString();
        receiverMail = in.readString();
        amount = in.readDouble();
        amountFormatted = in.readString();
        secureHashKey = in.readString();
        currencyCode = in.readString();
        transactionReferenceNumber = in.readString();
        is3dsEnabled = in.readByte() != 0;
        paymentMethod = in.readInt();
        isTahweel = in.readByte() != 0;
        isVisa = in.readByte() != 0;
        currencyName = in.readString();
        executedTransactionAmount = in.readString();
    }

    public static final Creator<PaymentData> CREATOR = new Creator<PaymentData>() {
        @Override
        public PaymentData createFromParcel(Parcel in) {
            return new PaymentData(in);
        }

        @Override
        public PaymentData[] newArray(int size) {
            return new PaymentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(merchantId);
        parcel.writeString(terminalId);
        parcel.writeString(merchantName);
        parcel.writeString(receiverMail);
        parcel.writeDouble(amount);
        parcel.writeString(amountFormatted);
        parcel.writeString(secureHashKey);
        parcel.writeString(currencyCode);
        parcel.writeString(transactionReferenceNumber);
        parcel.writeByte((byte) (is3dsEnabled ? 1 : 0));
        parcel.writeInt(paymentMethod);
        parcel.writeByte((byte) (isTahweel ? 1 : 0));
        parcel.writeByte((byte) (isVisa ? 1 : 0));
        parcel.writeString(currencyName);
        parcel.writeString(executedTransactionAmount);
    }
}
