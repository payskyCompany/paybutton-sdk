package io.paysky.data.model.request;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by amrel on 17/04/2018.
 */

public class QrGeneratorRequest extends BaseRequest implements Serializable  {

    @SerializedName("MerchantId")
    public String MerchantId;

    @SerializedName("SecureHash")
    public String SecureHash;

    @SerializedName("TerminalId")
    public String TerminalId;

    @SerializedName("Amount")
    public String Amount;

    @SerializedName("MerchantReference")
    public String MerchantReference;

    @SerializedName("TahweelQR")
    public boolean tahweelQR;

    @SerializedName("mVisaQR")
    public boolean mVisaQR;

    @SerializedName("DateTimeLocalTrxn")
    public String DateTimeLocalTrxn ;

}
