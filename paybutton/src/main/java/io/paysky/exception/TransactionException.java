package io.paysky.exception;

public class TransactionException extends Exception {
    public int errorCode;
    public String errorMessage;

    public TransactionException(){

    }

    public TransactionException(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
