package io.paysky.data.network;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paysky.data.model.request.CheckTransactionStatusRequest;
import io.paysky.data.model.request.GetSessionRequest;
import io.paysky.data.model.request.ListSavedCardsRequest;
import io.paysky.data.model.request.ManualPaymentRequest;
import io.paysky.data.model.request.MerchantInfoRequest;
import io.paysky.data.model.request.QrGeneratorRequest;
import io.paysky.data.model.request.RequestToPayRequest;
import io.paysky.data.model.request.SendReceiptByMailRequest;
import io.paysky.data.model.request.TransactionStatusRequest;
import io.paysky.data.model.response.CheckTransactionStatusResponse;
import io.paysky.data.model.response.DateTransactionsItem;
import io.paysky.data.model.response.GenerateQrCodeResponse;
import io.paysky.data.model.response.GetSessionResponse;
import io.paysky.data.model.response.ListSavedCardsResponse;
import io.paysky.data.model.response.ManualPaymentResponse;
import io.paysky.data.model.response.MerchantInfoResponse;
import io.paysky.data.model.response.RequestToPayResponse;
import io.paysky.data.model.response.SendReceiptByMailResponse;
import io.paysky.data.model.response.TransactionStatusResponse;
import io.paysky.data.model.response.TransactionsItem;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Paysky-202 on 5/14/2018.createConnection
 */

public class ApiConnection {

    private static ApiInterface createConnection() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(100, TimeUnit.SECONDS);
        httpClient.connectTimeout(40, TimeUnit.SECONDS);
        httpClient.writeTimeout(100, TimeUnit.SECONDS);
        httpClient.addInterceptor(new ErrorInterceptor());
        httpClient.addInterceptor(new LoggingInterceptor.Builder()
                .loggable(true)
                .setLevel(Level.BODY)
                .request("Request")
                .response("Response")
                .build());

        OkHttpClient client = httpClient.build();

        return new Retrofit.Builder()

                .baseUrl(ApiLinks.PAYMENT_LINK)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public static void executePayment(ManualPaymentRequest manualPaymentRequest,
                                      final ApiResponseListener<ManualPaymentResponse> listener) {
        createConnection().executeManualPayment(manualPaymentRequest)
                .enqueue(new Callback<ManualPaymentResponse>() {
                    @Override
                    public void onResponse(Call<ManualPaymentResponse> call,
                                           Response<ManualPaymentResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            onFailure(call, new Exception("fail to connect response code = " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ManualPaymentResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }

    public static void sendReceiptByMail(SendReceiptByMailRequest mailRequest,
                                         final ApiResponseListener<SendReceiptByMailResponse> listener) {
        createConnection().sendReceiptByMail(mailRequest)
                .enqueue(new Callback<SendReceiptByMailResponse>() {
                    @Override
                    public void onResponse(Call<SendReceiptByMailResponse> call,
                                           Response<SendReceiptByMailResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            onFailure(call, new Exception("fail to connect response code = " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SendReceiptByMailResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }

    public static void generateQrCode(QrGeneratorRequest request,
                                      final ApiResponseListener<GenerateQrCodeResponse> listener) {
        createConnection().generateQrCode(request)
                .enqueue(new Callback<GenerateQrCodeResponse>() {
                    @Override
                    public void onResponse(Call<GenerateQrCodeResponse> call,
                                           Response<GenerateQrCodeResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            onFailure(call, new Exception("fail to connect response code = " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<GenerateQrCodeResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }

    public static void checkTransactionPaymentStatus(TransactionStatusRequest request,
                                                     final ApiResponseListener<TransactionStatusResponse> listener) {
        createConnection().checkTransactionStatus(request)
                .enqueue(new Callback<TransactionStatusResponse>() {
                    @Override
                    public void onResponse(Call<TransactionStatusResponse> call,
                                           Response<TransactionStatusResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            onFailure(call, new Exception("fail to connect response code = " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionStatusResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }


    public static void requestToPay(RequestToPayRequest requestToPayRequest,
                                    final ApiResponseListener<RequestToPayResponse> listener) {
        createConnection().requestToPay(requestToPayRequest)
                .enqueue(new Callback<RequestToPayResponse>() {
                    @Override
                    public void onResponse(Call<RequestToPayResponse> call, Response<RequestToPayResponse> response) {
                        listener.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<RequestToPayResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }


    public static void getMerchantInfo(MerchantInfoRequest request,
                                       final ApiResponseListener<MerchantInfoResponse> listener) {
        createConnection().getMerchantInfo(request)
                .enqueue(new Callback<MerchantInfoResponse>() {
                    @Override
                    public void onResponse(Call<MerchantInfoResponse> call, Response<MerchantInfoResponse> response) {
                        listener.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<MerchantInfoResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }

    public static void getSession(GetSessionRequest getSessionRequest,
                                  final ApiResponseListener<GetSessionResponse> listener) {
        createConnection().getSession(getSessionRequest)
                .enqueue(new Callback<GetSessionResponse>() {
                             @Override
                             public void onResponse(Call<GetSessionResponse> call,
                                                    Response<GetSessionResponse> response) {
                                 if (response.isSuccessful()) {
                                     listener.onSuccess(response.body());
                                 }
                             }

                             @Override
                             public void onFailure(Call<GetSessionResponse> call, Throwable t) {
                                 listener.onFail(t);
                             }
                         }
                );
    }

    public static void listSavedCards(ListSavedCardsRequest request,
                                      final ApiResponseListener<ListSavedCardsResponse> listener) {
        createConnection().listSavedCards(request).enqueue(new Callback<ListSavedCardsResponse>() {
            @Override
            public void onResponse(Call<ListSavedCardsResponse> call, Response<ListSavedCardsResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ListSavedCardsResponse> call, Throwable t) {
                listener.onFail(t);
            }
        });

    }

//    public static void compose3dsTransaction(Compose3dsTransactionRequest request, final ApiResponseListener<Compose3dsTransactionResponse> listener) {
//        createConnection().compose3dpsTransaction(request)
//                .enqueue(new Callback<Compose3dsTransactionResponse>() {
//                    @Override
//                    public void onResponse(Call<Compose3dsTransactionResponse> call, Response<Compose3dsTransactionResponse> response) {
//                        listener.onSuccess(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<Compose3dsTransactionResponse> call, Throwable t) {
//                        listener.onFail(t);
//                    }
//                });
//    }
//
//    public static void process3dTransaction(Process3dTransactionRequest request, final ApiResponseListener<Process3dTransactionResponse> listener) {
//        createConnection().process3dTransaction(request).enqueue(new Callback<Process3dTransactionResponse>() {
//            @Override
//            public void onResponse(Call<Process3dTransactionResponse> call, Response<Process3dTransactionResponse> response) {
//                listener.onSuccess(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<Process3dTransactionResponse> call, Throwable t) {
//                listener.onFail(t);
//            }
//        });
//    }


    public static void checkTransactionStatus(final String transactionId, final CheckTransactionStatusRequest request, final CheckTransactionListener listener) {
        createConnection().checkTransaction(request)
                .enqueue(new Callback<CheckTransactionStatusResponse>() {
                    @Override
                    public void onResponse(Call<CheckTransactionStatusResponse> call, Response<CheckTransactionStatusResponse> response) {
                        CheckTransactionStatusResponse body = response.body();
                        if (body != null && body.success) {
                            boolean transactionSuccess = false;
                            List<TransactionsItem> transactions = body.transactions;
                            for (TransactionsItem item : transactions) {
                                for (DateTransactionsItem transactionsItem : item.dateTransactions) {
                                    if (transactionsItem.merchantReference.equals(transactionId)) {
                                        transactionSuccess = true;
                                        listener.transactionSuccess(transactionsItem);
                                    }
                                }
                            }
                            if (!transactionSuccess) {
                                listener.transactionFailed();
                            }
                        } else {
                            listener.onError(new Exception("error in server"));
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckTransactionStatusResponse> call, Throwable t) {
                        listener.onError(t);
                    }
                });
    }

    private static class ErrorInterceptor implements Interceptor {


        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response;

            response = chain.proceed(chain.request());

            if (response.code() == 401) {
                EventBus.getDefault().post(new NoConnectivityException());
            }


            return response;
        }
    }

    public static class NoConnectivityException extends IOException {
    }
}
