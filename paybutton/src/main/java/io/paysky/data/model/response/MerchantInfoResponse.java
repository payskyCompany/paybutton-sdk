package io.paysky.data.model.response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MerchantInfoResponse{

	@SerializedName("Is3DS")
	public boolean is3DS;

	@SerializedName("MerchantCurrency")
	public String merchantCurrency;

	@SerializedName("Message")
	public String message;

	@SerializedName("MerchantName")
	public String merchantName;

	@SerializedName("MerchantBankLogo")
	public String merchantBankLogo;

	@SerializedName("IsmVisa")
	public boolean ismVisa;

	@SerializedName("PaymentMethod")
	public int paymentMethod;

	@SerializedName("TerminalPublicKey")
	public String terminalPublicKey;

	@SerializedName("IsTahweel")
	public boolean isTahweel;

	@SerializedName("Success")
	public boolean success;

	@SerializedName("IsValidPayByCardFromWeb")
	public boolean isValidPayByCardFromWeb;
}