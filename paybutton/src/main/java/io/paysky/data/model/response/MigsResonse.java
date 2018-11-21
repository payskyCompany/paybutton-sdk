package io.paysky.data.model.response;

/**
 * Created by PaySky-3 on 8/8/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MigsResonse implements Serializable {

    @SerializedName("PAN")
    private String pAN;

    @SerializedName("MWActionCode")
    private String MWActionCode;

    @SerializedName("MWMessage")
    private String MWMessage;


    @SerializedName("SystemTraceNr")
    private String systemTraceNr;

    @SerializedName("RetrievalRefNr")
    private String retrievalRefNr;

    @SerializedName("ApprovalCode")
    private String approvalCode;

    @SerializedName("ActionCode")
    private String actionCode;

    @SerializedName("Message")
    private String message;

    @SerializedName("TxnResponseCode")
    private String txnResponseCode;

    @SerializedName("TransactionNo")
    private String transactionNo;

    public String getPAN() {
        return pAN;
    }

    public void setPAN(String pAN) {
        this.pAN = pAN;
    }

    public String getSystemTraceNr() {
        return systemTraceNr;
    }

    public void setSystemTraceNr(String systemTraceNr) {
        this.systemTraceNr = systemTraceNr;
    }

    public String getRetrievalRefNr() {
        return retrievalRefNr;
    }

    public void setRetrievalRefNr(String retrievalRefNr) {
        this.retrievalRefNr = retrievalRefNr;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTxnResponseCode() {
        return txnResponseCode;
    }

    public void setTxnResponseCode(String txnResponseCode) {
        this.txnResponseCode = txnResponseCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getpAN() {
        return pAN;
    }

    public void setpAN(String pAN) {
        this.pAN = pAN;
    }

    public String getMWActionCode() {
        return MWActionCode;
    }

    public void setMWActionCode(String MWActionCode) {
        this.MWActionCode = MWActionCode;
    }

    public String getMWMessage() {
        return MWMessage;
    }

    public void setMWMessage(String MWMessage) {
        this.MWMessage = MWMessage;
    }


}
