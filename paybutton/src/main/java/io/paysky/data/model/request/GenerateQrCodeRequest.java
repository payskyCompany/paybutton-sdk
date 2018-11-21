package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GenerateQrCodeRequest{

	@SerializedName("StoreLabel")
	public String storeLabel;

	@SerializedName("DateTimeLocalTrxn")
	public String dateTimeLocalTrxn;

	@SerializedName("Amount")
	public String amount;


	@SerializedName("MerchantId")
	public String merchantId;


	@SerializedName("SecureHash")
	public String secureHash;


	@SerializedName("MerchantReference")
	public long merchantReference;

	@SerializedName("TerminalId")
	public String terminalId;

}