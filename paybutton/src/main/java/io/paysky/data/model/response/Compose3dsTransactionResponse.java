package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Compose3dsTransactionResponse {

	@SerializedName("PaymentServerURL")
	public String paymentServerURL;

	@SerializedName("Message")
	public String message;

	@SerializedName("MerchantAccount")
	public String merchantAccount;

	@SerializedName("MerchantReference")
	public String merchantReference;

	@SerializedName("Amount")
	public int amount;

	@SerializedName("AccessCode")
	public String accessCode;

	@SerializedName("GatewayType")
	public int gatewayType;

	@SerializedName("SecureHashType")
	public String secureHashType;

	@SerializedName("CardNum")
	public String cardNum;

	@SerializedName("OrderInfo")
	public String orderInfo;

	@SerializedName("ReturnURL")
	public String returnURL;

	@SerializedName("Success")
	public boolean success;

	@SerializedName("CardSecurityCode")
	public String cardSecurityCode;

	@SerializedName("CardExp")
	public String cardExp;

	@SerializedName("SecureHash")
	public String secureHash;

	@SerializedName("MerchantName")
	public String merchantName;

	@SerializedName("Command")
	public String command;

	@SerializedName("Version")
	public String version;

	@SerializedName("CardType")
	public String cardType;

	@SerializedName("Gateway")
	public String gateway;

	@SerializedName("MerchTxnRef")
	public String merchTxnRef;

	@SerializedName("Currency")
	public String currency;
}