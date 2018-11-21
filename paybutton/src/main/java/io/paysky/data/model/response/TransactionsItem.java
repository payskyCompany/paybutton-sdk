package io.paysky.data.model.response;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class TransactionsItem{

	@SerializedName("DateTotalAmount")
	public String dateTotalAmount;

	@SerializedName("DateTransactions")
	public List<DateTransactionsItem> dateTransactions;

	@SerializedName("Date")
	public String date;
}