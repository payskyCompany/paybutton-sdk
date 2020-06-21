package io.paysky.util;

import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.PayButton;

public class TransactionManager {


    private static SuccessfulCardTransaction cardTransaction;
    private static SuccessfulWalletTransaction walletTransaction;
    private static TransactionException transactionException;
    private static TransactionStatus transactionStatus = TransactionStatus.NOT_COMPLETED;
    private static TransactionType transactionType;

    public static enum TransactionStatus {
        NOT_COMPLETED, COMPLETED, FAILED
    }


    public static enum TransactionType {
        QR, MANUAL
    }

    public static void setTransactionType(TransactionType transactionType) {
        TransactionManager.transactionType = transactionType;
    }

    public static void setWalletTransaction(SuccessfulWalletTransaction walletTransaction) {
        transactionStatus = TransactionStatus.COMPLETED;
        TransactionManager.walletTransaction = walletTransaction;
    }

    public static void setCardTransaction(SuccessfulCardTransaction cardTransaction) {
        transactionStatus = TransactionStatus.COMPLETED;
        TransactionManager.cardTransaction = cardTransaction;
    }

    public static void setTransactionException(TransactionException transactionException) {
        transactionStatus = TransactionStatus.FAILED;
        TransactionManager.transactionException = transactionException;
    }

    public static void sendTransactionEvent() {
        switch (transactionStatus) {
            case FAILED:
                PayButton.transactionCallback.onError(transactionException);
                break;
            case COMPLETED:
                if (transactionType == TransactionType.MANUAL) {
                    PayButton.transactionCallback.onCardTransactionSuccess(cardTransaction);
                } else {
                    PayButton.transactionCallback.onWalletTransactionSuccess(walletTransaction);
                }

                break;
            case NOT_COMPLETED:
                PayButton.transactionCallback.onError(new TransactionException("transaction not completed"));
                break;
        }

        clearStaticData();
    }

    private static void clearStaticData() {
        cardTransaction = null;
        walletTransaction = null;
        transactionException = null;
        transactionStatus = TransactionStatus.NOT_COMPLETED;
        transactionType = null;
        PayButton.transactionCallback = null;
    }

}
