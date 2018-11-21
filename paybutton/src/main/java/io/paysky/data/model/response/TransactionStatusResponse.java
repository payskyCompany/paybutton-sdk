package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TransactionStatusResponse{


	@SerializedName("Message")
	  
	public String message;
	@SerializedName("Success")
	  
	public Boolean success;
	@SerializedName("AmountTrxn")
	  
	public Integer amountTrxn;
	@SerializedName("CubeTxnId")
	  
	public Integer cubeTxnId;
	@SerializedName("Date")
	  
	public String date;
	@SerializedName("ExternalTxnId")
	  
	public String externalTxnId;
	@SerializedName("IsAlreadyPaid")
	  
	public Boolean isAlreadyPaid;
	@SerializedName("IsCancelled")
	  
	public Boolean isCancelled;
	@SerializedName("IsMVisaPaid")
	  
	public Boolean isMVisaPaid;
	@SerializedName("IsPaid")
	  
	public Boolean isPaid;
	@SerializedName("IsReachedMaxNumOfPayment")
	  
	public Boolean isReachedMaxNumOfPayment;
	@SerializedName("IsTahweelPaid")
	  
	public Boolean isTahweelPaid;
	@SerializedName("MerchantReference")
	  
	public String merchantReference;
	@SerializedName("NetworkReference")
	  
	public String networkReference;
	@SerializedName("PaidThrough")
	
	public String paidThrough;
	@SerializedName("Payer")
	  
	public String payer;
	@SerializedName("PayerName")
	  
	public String payerName;
	@SerializedName("Reference")
	  
	public String reference;
	@SerializedName("Scheme")
	  
	public String scheme;
	@SerializedName("SystemReference")
	  
	public Integer systemReference;
	@SerializedName("SystemTxnId")
	  
	public String systemTxnId;
	@SerializedName("TxnDate")
	  
	public String txnDate;
}