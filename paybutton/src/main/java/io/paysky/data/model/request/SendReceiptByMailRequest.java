package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SendReceiptByMailRequest extends BaseRequest{

	@SerializedName("EmailTo")
	public String emailTo;

	@SerializedName("SecureHash")
	public String secureHash;

	@SerializedName("ExternalReceiptNumber")
	public String externalReceiptNumber;

	@SerializedName("DateTimeLocalTrxn")
	public String dateTimeLocalTrxn;

	@SerializedName("ExternalReceiptNo")
	public String externalReceiptNo;

	@SerializedName("TerminalId")
	public String terminalId;

	@SerializedName("MerchantId")
	public String merchantId;

	@SerializedName("TransactionChannel")
	public String transactionChannel;

	@SerializedName("TransactionId")
	public String transactionId;
}