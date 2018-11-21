package io.paysky.data.model.response;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DateTransactionsItem{

	@SerializedName("Status")
	public String status;

	@SerializedName("MerchantReference")
	public String merchantReference;

	@SerializedName("FeeAmnt")
	public String feeAmnt;

	@SerializedName("SenderName")
	public String senderName;

	@SerializedName("AmountTrxn")
	public String amountTrxn;

	@SerializedName("STAN")
	public String sTAN;

	@SerializedName("IsRefundEnabled")
	public boolean isRefundEnabled;

	@SerializedName("TransType")
	public String transType;

	@SerializedName("TipAmnt")
	public String tipAmnt;

	@SerializedName("Amnt")
	public String amnt;

	@SerializedName("RRN")
	public String rRN;

	@SerializedName("TransactionId")
	public String transactionId;

	@SerializedName("CardNo")
	public String cardNo;

	@SerializedName("ExternalTxnId")
	public String externalTxnId;

	@SerializedName("ResCodeDesc")
	public String resCodeDesc;

	@SerializedName("TxnDateTime")
	public String txnDateTime;

	@SerializedName("MobileNumber")
	public String mobileNumber;

	@SerializedName("CardType")
	public String cardType;

	@SerializedName("Currency")
	public String currency;

	@SerializedName("ReceiptNo")
	public String receiptNo;

	@SerializedName("TransactionChannel")
	public String transactionChannel;

	@SerializedName("IsSend")
	public boolean isSend;
}