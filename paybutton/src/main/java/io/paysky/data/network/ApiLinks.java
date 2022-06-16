package io.paysky.data.network;

/**
 * Created by Paysky-202 on 5/13/2018.
 */

public class ApiLinks {

    public static final String EXECUTE_PAYMENT = "Cube/PayLink.svc/api/PayByCard";
    public static final String SEND_RECEIPT_BY_MAIL = "Cube/PayLink.svc/api/SendReceiptToEmail";
    public static final String GENERATE_QRCODE = "Cube/PayLink.svc/api/GenerateQR";
    public static final String CHECK_PAYMENT_STATUS = "Cube/PayLink.svc/api/CheckTxnStatus";
    public static final String SMS_PAYMENT = "Cube/PayLink.svc/api/RequestToPay";
    public static final String MERCHANT_INFO = "Cube/PayLink.svc/api/CheckPaymentMethod";
    public static final String CHECK_TRANSACTION_STATUS = "Cube/PayLink.svc/api/FilterTransactions";


    public static final String GRAY_LINK = "https://grey.paysky.io/";
    public static final String CUBE = "https://cube.paysky.io/";

    public static String PAYMENT_LINK = CUBE;
}

