package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MerchantInfoRequest extends BaseRequest{

	@SerializedName("SecureHash")
	public String secureHash;

	@SerializedName("DateTimeLocalTrxn")
	public String dateTimeLocalTrxn;

	@SerializedName("TerminalId")
	public String terminalId;

	@SerializedName("PaymentMethod")
	public Integer paymentMethod;

	@SerializedName("MerchantId")
	public String merchantId;
}