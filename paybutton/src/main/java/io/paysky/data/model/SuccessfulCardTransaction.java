package io.paysky.data.model;

public class SuccessfulCardTransaction {
    public String SystemReference;
    public String NetworkReference;
    public String ReceiptNumber;
    public String AuthCode;
    public String ActionCode;
    public String MerchantReference;
    public String Message;
    public boolean Success;
    public String terminalId, merchantId, amount;
    public String tokenCustomerId="";

    @Override
    public String toString() {
        return "SuccessfulCardTransaction{" +
                "SystemReference='" + SystemReference + '\'' +
                ", NetworkReference='" + NetworkReference + '\'' +
                ", CustomerId='" + tokenCustomerId + '\'' +
                ", ReceiptNumber='" + ReceiptNumber + '\'' +
                ", AuthCode='" + AuthCode + '\'' +
                ", ActionCode='" + ActionCode + '\'' +
                ", MerchantReference='" + MerchantReference + '\'' +
                ", Message='" + Message + '\'' +
                ", Success=" + Success +
                ", terminalId='" + terminalId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
