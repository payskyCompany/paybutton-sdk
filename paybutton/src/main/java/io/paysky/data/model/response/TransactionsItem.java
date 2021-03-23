package io.paysky.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class TransactionsItem{

	@SerializedName("DateTotalAmount")
	public String dateTotalAmount;

	@SerializedName("DateTransactions")
	public List<DateTransactionsItem> dateTransactions;

	@SerializedName("Date")
	public String date;
}