package io.paysky.data.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MerchantInfoRequest{

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