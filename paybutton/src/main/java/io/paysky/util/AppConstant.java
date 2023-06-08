package io.paysky.util;


public interface AppConstant {

    interface BundleKeys {
        String SECURE_HASH_KEY = "secure_hash";
        String TERMINAL_ID = "terminal_id";
        String MERCHANT_ID = "merchant_id";
        String PAY_AMOUNT = "pay_amount";
        String PAYMENT_DATA = "payment_data";
        String RECEIPT = "receipt";
        String CURRENCY_CODE = "currency_code";
        String TRANSACTION_REFERENCE_NUMBER = "transaction_reference_number";
        String URL_ENUM_KEY = "enumKey";
        String CARD_DATA = "card_data";
        String DECLINE_CAUSE = "decline_cause";
        String TOKENIZED_CARD = "tokenized_card";
    }


    interface TransactionChannelName {
        String CARD = "Card";
        String TAHWEEL = "Tahweel";
    }

}
