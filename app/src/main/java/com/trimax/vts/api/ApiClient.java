package com.trimax.vts.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.trimax.vts.utils.CommonClass.apiKey;
import static com.trimax.vts.utils.CommonClass.auth;

/**
 * Created by genuus on 31/1/18.
 */

public class ApiClient {

    //live url  https://rp.roadpulse.net/
    //Stageing url http://test.roadpulse.net/
    public static final String BASE_URL = "http://test.roadpulse.net/";
    public static final String ONESIGNAL_URL = "https://onesignal.com/";

     private static Retrofit retrofit = null, retrofit1=null,retrofitOneSignal=null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            try {
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                        .addInterceptor(logging)
                        .build();
                Gson gs = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gs))
                        .build();
            }catch (NoSuchAlgorithmException | KeyManagementException ex){
                ex.printStackTrace();
            }
        }
        return retrofit;
    }

    public static RetrofitInterface getApiClient() {
        if (retrofit1==null) {
            try {
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                Interceptor basicAuth = new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                
                                .addHeader("X-API-KEY",apiKey)
                                .addHeader("Authorization", auth.replace("\n",""))
                                .build();
                            return chain.proceed(request);
                    }
                };

                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                        .addInterceptor(basicAuth)
                        .addInterceptor(logging)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                Gson gs = new GsonBuilder().setLenient().create();
                retrofit1 = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gs))
                        .build();

            }catch (NoSuchAlgorithmException | KeyManagementException ex){
                ex.printStackTrace();
            }
        }
        return retrofit1.create(RetrofitInterface.class);
    }

    public static RetrofitInterface getApiClientOfOneSignal(){
        retrofitOneSignal=null;
            try {
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                Interceptor header = new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type","application/json")
                                .addHeader("Authorization","Basic ZjU3ZTQ2OTEtZDFkOC00ZTM1LWIzMWEtYjMyNGQ3ODkxZjcz")
                                .build();
                        return chain.proceed(request);
                    }
                };

                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                        .addInterceptor(header)
                        .addInterceptor(logging)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                Gson gs = new GsonBuilder().setLenient().create();
                retrofitOneSignal = new Retrofit.Builder()
                        .baseUrl(ONESIGNAL_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gs))
                        .build();

            }catch (NoSuchAlgorithmException | KeyManagementException ex){
                ex.printStackTrace();
            }
        return retrofitOneSignal.create(RetrofitInterface.class);
    }


    public static Retrofit getClientForReplay() {
        Retrofit retrofit=null;
            try {
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(260,TimeUnit.SECONDS)
                        .readTimeout(180, TimeUnit.SECONDS)
                        .connectionPool(new ConnectionPool(3, 10, TimeUnit.MINUTES))
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                        .build();
                Gson gs = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gs))
                        .build();
            }catch (NoSuchAlgorithmException | KeyManagementException ex){
                ex.printStackTrace();
            }
        return retrofit;
    }


    final static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

}
