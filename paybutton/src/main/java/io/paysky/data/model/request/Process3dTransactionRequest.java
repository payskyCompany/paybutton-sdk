package io.paysky.data.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Process3dTransactionRequest{

	@SerializedName("SecureHash")
	public String secureHash;

	@SerializedName("DateTimeLocalTrxn")
	public String dateTimeLocalTrxn;

	@SerializedName("ThreeDSResponseData")
	public String threeDSResponseData;

	@SerializedName("TerminalId")
	public String terminalId;

	@SerializedName("GatewayType")
	public int gatewayType;

	@SerializedName("MerchantId")
	public String merchantId;
}