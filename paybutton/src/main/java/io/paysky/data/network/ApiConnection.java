package io.paysky.data.network;

import android.content.Context;
import android.os.Build;

import com.example.paybutton.BuildConfig;
import com.example.paybutton.R;
import com.google.android.gms.security.ProviderInstaller;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.paysky.data.model.request.Compose3dsTransactionRequest;
import io.paysky.data.model.request.ManualPaymentRequest;
import io.paysky.data.model.request.MerchantInfoRequest;
import io.paysky.data.model.request.Process3dTransactionRequest;
import io.paysky.data.model.request.QrGeneratorRequest;
import io.paysky.data.model.request.RequestToPayRequest;
import io.paysky.data.model.request.SendReceiptByMailRequest;
import io.paysky.data.model.request.TransactionStatusRequest;
import io.paysky.data.model.response.Compose3dsTransactionResponse;
import io.paysky.data.model.response.GenerateQrCodeResponse;
import io.paysky.data.model.response.ManualPaymentResponse;
import io.paysky.data.model.response.MerchantInfoResponse;
import io.paysky.data.model.response.Process3dTransactionResponse;
import io.paysky.data.model.response.RequestToPayResponse;
import io.paysky.data.model.response.SendReceiptByMailResponse;
import io.paysky.data.model.response.TransactionStatusResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public class ApiConnection {


    public static void executePayment(Context context, ManualPaymentRequest manualPaymentRequest, final ApiResponseListener<ManualPaymentResponse> listener) {
        createConnection(context).executeManualPayment(manualPaymentRequest)
                .enqueue(new Callback<ManualPaymentResponse>() {
                    @Override
                    public void onResponse(Call<ManualPaymentResponse> call, Response<ManualPaymentResponse> response) {
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

    public static void sendReceiptByMail(Context context, SendReceiptByMailRequest mailRequest, final ApiResponseListener<SendReceiptByMailResponse> listener) {
        createConnection(context).sendReceiptByMail(mailRequest)
                .enqueue(new Callback<SendReceiptByMailResponse>() {
                    @Override
                    public void onResponse(Call<SendReceiptByMailResponse> call, Response<SendReceiptByMailResponse> response) {
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

    public static void generateQrCode(Context context, QrGeneratorRequest request, final ApiResponseListener<GenerateQrCodeResponse> listener) {
        createConnection(context).generateQrCode(request)
                .enqueue(new Callback<GenerateQrCodeResponse>() {
                    @Override
                    public void onResponse(Call<GenerateQrCodeResponse> call, Response<GenerateQrCodeResponse> response) {
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

    public static void checkTransactionPaymentStatus(Context context, TransactionStatusRequest request, final ApiResponseListener<TransactionStatusResponse> listener) {
        createConnection(context).checkTransactionStatus(request)
                .enqueue(new Callback<TransactionStatusResponse>() {
                    @Override
                    public void onResponse(Call<TransactionStatusResponse> call, Response<TransactionStatusResponse> response) {
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


    public static void requestToPay(Context context, RequestToPayRequest requestToPayRequest, final ApiResponseListener<RequestToPayResponse> listener) {
        createConnection(context).requestToPay(requestToPayRequest)
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


    private static ApiInterface createConnection(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        // add certificate.

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            CertificateFactory certificateFactory;
            try {
                certificateFactory = CertificateFactory.getInstance("X.509");
                InputStream inputStream = context.getResources().openRawResource(R.raw.certificate); //(.crt)
                Certificate certificate = certificateFactory.generateCertificate(inputStream);
                inputStream.close();

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", certificate);

                // Create a TrustManager that trusts the CAs in our KeyStore.
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
                trustManagerFactory.init(keyStore);

                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
                ProviderInstaller.installIfNeeded(context);
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslContext.getSocketFactory());
                clientBuilder.sslSocketFactory(NoSSLv3Factory, x509TrustManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Retrofit.Builder()
                .baseUrl(ApiLinks.GRAY_LINK)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public static void getMerchantInfo(Context context, MerchantInfoRequest request, final ApiResponseListener<MerchantInfoResponse> listener) {
        createConnection(context).getMerchantInfo(request)
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

    public static void compose3dsTransaction(Context context, Compose3dsTransactionRequest request, final ApiResponseListener<Compose3dsTransactionResponse> listener) {
        createConnection(context).compose3dpsTransaction(request)
                .enqueue(new Callback<Compose3dsTransactionResponse>() {
                    @Override
                    public void onResponse(Call<Compose3dsTransactionResponse> call, Response<Compose3dsTransactionResponse> response) {
                        listener.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<Compose3dsTransactionResponse> call, Throwable t) {
                        listener.onFail(t);
                    }
                });
    }

    public static void process3dTransaction(Context context, Process3dTransactionRequest request, final ApiResponseListener<Process3dTransactionResponse> listener) {
        createConnection(context).process3dTransaction(request).enqueue(new Callback<Process3dTransactionResponse>() {
            @Override
            public void onResponse(Call<Process3dTransactionResponse> call, Response<Process3dTransactionResponse> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Process3dTransactionResponse> call, Throwable t) {
                listener.onFail(t);
            }
        });
    }

}
