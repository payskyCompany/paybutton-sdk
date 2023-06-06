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
    public boolean isTahweel, isVisa, isCard, isTokenized;
    public String currencyName;
    public String executedTransactionAmount;
    public String customerId, customerSession;
    public String customerEmail, customerMobile;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.merchantId);
        dest.writeString(this.terminalId);
        dest.writeString(this.merchantName);
        dest.writeString(this.receiverMail);
        dest.writeDouble(this.amount);
        dest.writeString(this.amountFormatted);
        dest.writeString(this.secureHashKey);
        dest.writeString(this.currencyCode);
        dest.writeString(this.transactionReferenceNumber);
        dest.writeByte(this.is3dsEnabled ? (byte) 1 : (byte) 0);
        dest.writeInt(this.paymentMethod);
        dest.writeByte(this.isTahweel ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVisa ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCard ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTokenized ? (byte) 1 : (byte) 0);
        dest.writeString(this.currencyName);
        dest.writeString(this.executedTransactionAmount);
        dest.writeString(this.customerId);
        dest.writeString(this.customerSession);
        dest.writeString(this.customerEmail);
        dest.writeString(this.customerMobile);
    }

    public PaymentData() {
    }

    protected PaymentData(Parcel in) {
        this.merchantId = in.readString();
        this.terminalId = in.readString();
        this.merchantName = in.readString();
        this.receiverMail = in.readString();
        this.amount = in.readDouble();
        this.amountFormatted = in.readString();
        this.secureHashKey = in.readString();
        this.currencyCode = in.readString();
        this.transactionReferenceNumber = in.readString();
        this.is3dsEnabled = in.readByte() != 0;
        this.paymentMethod = in.readInt();
        this.isTahweel = in.readByte() != 0;
        this.isVisa = in.readByte() != 0;
        this.isCard = in.readByte() != 0;
        this.isTokenized = in.readByte() != 0;
        this.currencyName = in.readString();
        this.executedTransactionAmount = in.readString();
        this.customerId = in.readString();
        this.customerSession = in.readString();
        this.customerEmail = in.readString();
        this.customerMobile = in.readString();
    }

    public static final Parcelable.Creator<PaymentData> CREATOR = new Parcelable.Creator<PaymentData>() {
        @Override
        public PaymentData createFromParcel(Parcel source) {
            return new PaymentData(source);
        }

        @Override
        public PaymentData[] newArray(int size) {
            return new PaymentData[size];
        }
    };
}
