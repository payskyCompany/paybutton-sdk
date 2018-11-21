package io.paysky.data.model.response;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CheckTransactionStatusResponse{

	@SerializedName("Transactions")
	public List<TransactionsItem> transactions;

	@SerializedName("Message")
	public String message;

	@SerializedName("TotalCountAllTransaction")
	public int totalCountAllTransaction;

	@SerializedName("Success")
	public boolean success;
}