package io.paysky.data.network;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public interface ApiResponseListener<T> {

    void onSuccess(T response);

    void onFail(Throwable error);
}
