package io.paysky.data.network;

/**
 * Created by Paysky-202 on 5/13/2018.
 */

public class ApiLinks {

    private static final String CUBE_GATEWAY = "cube/PayLink.svc/";
    private static final String API = "api/";

    public static final String EXECUTE_PAYMENT = CUBE_GATEWAY + API + "PayByCard";
    public static final String SEND_RECEIPT_BY_MAIL = CUBE_GATEWAY + API + "SendReceiptToEmail";
    public static final String GENERATE_QRCODE = CUBE_GATEWAY + API + "GenerateQR";
    public static final String CHECK_PAYMENT_STATUS = CUBE_GATEWAY + API + "CheckTxnStatus";
    public static final String SMS_PAYMENT = CUBE_GATEWAY + API + "RequestToPay";
    public static final String MERCHANT_INFO = CUBE_GATEWAY + API + "CheckPaymentMethod";
    public static final String CHECK_TRANSACTION_STATUS = CUBE_GATEWAY + API + "FilterTransactions";
    public static final String Get_Session_For_Customer_Token = CUBE_GATEWAY + API + "GetSessionForCustomerToken";
    public static final String LIST_SAVED_CARDS_FOR_CUSTOMER = CUBE_GATEWAY + API + "GetAllCardsForCustomerToken";
    public static final String DELETE_TOKENIZED_CARD = CUBE_GATEWAY + API + "RemoveToken";
    public static final String CHANGE_DEFAULT_CARD = CUBE_GATEWAY + API + "ChangeDefaultToken";

    public static final String GRAY_LINK = "https://grey.paysky.io/";
    public static final String CUBE = "https://cube.paysky.io/";

    public static String PAYMENT_LINK = CUBE;
}

