package io.paysky.data.model.request;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CheckTransactionStatusRequest extends BaseRequest{

	@SerializedName("SecureHash")
	public String secureHash;

	@SerializedName("DateTimeLocalTrxn")
	public String dateTimeLocalTrxn;

	@SerializedName("DisplayLength")
	public int displayLength;

	@SerializedName("DateFrom")
	public String dateFrom;

	@SerializedName("TerminalId")
	public String terminalId;

	@SerializedName("DisplayStart")
	public int displayStart;

	@SerializedName("MerchantId")
	public String merchantId;

	@SerializedName("DateTo")
	public String dateTo;
}