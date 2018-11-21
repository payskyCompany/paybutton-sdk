package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ManualPaymentRequest {

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("cvv2")
    public String cvv2;

    @SerializedName("POSEntryMode")
    public String pOSEntryMode;

    @SerializedName("DateTimeLocalTrxn")
    public String dateTimeLocalTrxn;

    @SerializedName("AmountTrxn")
    public String amountTrxn;

    @SerializedName("CardAcceptorIDcode")
    public String cardAcceptorIDcode;

    @SerializedName("ProcessingCode")
    public String processingCode;

    @SerializedName("CardAcceptorTerminalID")
    public String cardAcceptorTerminalID;

    @SerializedName("ISFromPOS")
    public boolean iSFromPOS;

    @SerializedName("DateExpiration")
    public String dateExpiration;

    @SerializedName("SystemTraceNr")
    public String systemTraceNr;

    @SerializedName("MessageTypeID")
    public String messageTypeID;

    @SerializedName("CurrencyCodeTrxn")
    public String currencyCodeTrxn;

    @SerializedName("PAN")
    public String pAN;

    @SerializedName("SecureHash")
    public String secureHash;

    @SerializedName("MerchantId")
    public String merchantId;

    @SerializedName("TerminalId")
    public String terminalId;

    @SerializedName("IsWebRequest")
    public boolean isWebRequest = true;

    @SerializedName("VerToken")
    public String verToken;

    @SerializedName("ThreeDSenrolled")
    public String threeDSenrolled;

    @SerializedName("ThreeDSstatus")
    public String threeDSstatus;

    @SerializedName("VerType")
    public String verType;

    @SerializedName("Message")
    public String message;

    @SerializedName("ThreeDSXID")
    public String threeDSXID;

    @SerializedName("Success")
    public boolean success;

    @SerializedName("ThreeDSECI")
    public String threeDSECI;

}