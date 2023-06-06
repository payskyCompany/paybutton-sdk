package io.paysky.data.network;

/**
 * Created by Paysky-202 on 5/13/2018.
 */

public class ApiLinks {

    public static final String CUBE_GATEWAY = "cube/PayLink.svc/";

    public static final String EXECUTE_PAYMENT = CUBE_GATEWAY + "api/PayByCard";
    public static final String SEND_RECEIPT_BY_MAIL = CUBE_GATEWAY + "api/SendReceiptToEmail";
    public static final String GENERATE_QRCODE = CUBE_GATEWAY + "api/GenerateQR";
    public static final String CHECK_PAYMENT_STATUS = CUBE_GATEWAY + "api/CheckTxnStatus";
    public static final String SMS_PAYMENT = CUBE_GATEWAY + "api/RequestToPay";
    public static final String MERCHANT_INFO = CUBE_GATEWAY + "api/CheckPaymentMethod";
    public static final String CHECK_TRANSACTION_STATUS = CUBE_GATEWAY + "api/FilterTransactions";

    public static final String Get_Session_For_Customer_Token = CUBE_GATEWAY + "api/GetSessionForCustomerToken";

    public static final String GRAY_LINK = "https://grey.paysky.io/";
    public static final String CUBE = "https://cube.paysky.io/";

    public static String PAYMENT_LINK = CUBE;
}

