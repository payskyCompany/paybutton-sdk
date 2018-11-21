package io.paysky.data.network;

import io.paysky.data.model.response.DateTransactionsItem;

public interface CheckTransactionListener {

    void transactionSuccess(DateTransactionsItem transactionsItem);

    void transactionFailed();

    void transactionNotFound();

    void onError(Throwable throwable);
}
