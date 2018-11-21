package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GenerateQrCodeResponse{

	@SerializedName("TxnId")
	public long txnId;

	@SerializedName("Message")
	public String message;

	@SerializedName("ISOQR")
	public String iSOQR;

	@SerializedName("Success")
	public boolean success;
}