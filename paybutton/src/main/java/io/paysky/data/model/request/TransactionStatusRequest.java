package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TransactionStatusRequest {

    @SerializedName("TxnId")
    public long txnId;

    @SerializedName("SecureHash")
    public String secureHash;

    @SerializedName("DateTimeLocalTrxn")
    public String dateTimeLocalTrxn;

    @SerializedName("TerminalId")
    public String terminalId;

    @SerializedName("MerchantId")
    public String merchantId;
}