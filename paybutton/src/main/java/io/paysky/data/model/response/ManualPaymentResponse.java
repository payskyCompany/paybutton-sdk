package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ManualPaymentResponse {


    @SerializedName("Message")
    public String message;

    @SerializedName("Success")
    public Boolean success;

    @SerializedName("ActionCode")
    public String actionCode;

    @SerializedName("AuthCode")
    public String authCode;

    @SerializedName("MWActionCode")
    public Object mWActionCode;

    @SerializedName("MWMessage")
    public String mWMessage;

    @SerializedName("MerchantReference")
    public String merchantReference;

    @SerializedName("NetworkReference")
    public String networkReference;

    @SerializedName("ReceiptNumber")
    public String receiptNumber;

    @SerializedName("RefNumber")
    public String refNumber;

    @SerializedName("SystemReference")
    public Integer systemReference;

    @SerializedName("TransactionNo")
    public String transactionNo;

    @SerializedName("ThreeDSUrl")
    public String threeDSUrl;

    @SerializedName("ChallengeRequired")
    public boolean challengeRequired;

    @SerializedName("TokenCustomerId")
    public String tokenCustomerId;
}