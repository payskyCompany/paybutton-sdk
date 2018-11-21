package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RequestToPayResponse {

	@SerializedName("TxnId")
	public long txnId;

	@SerializedName("Message")
	public String message;

	@SerializedName("Success")
	public boolean success;
}