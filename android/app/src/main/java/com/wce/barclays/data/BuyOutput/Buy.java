package com.wce.barclays.data.BuyOutput;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Buy {
    private static OkHttpClient client;
    BuyListener textRazorListener;

    private static BuyApi api;
    private static BuyApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl("http://192.168.43.154:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
                .create(BuyApi.class);
        }
        return api;
    }


    private static OkHttpClient buildClient()
    {
        if(client==null)
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-AYLIEN-TextAPI-Application-Key","3f5f0bc779ca61a2801894707ee82e08" )
                            .addHeader("X-AYLIEN-TextAPI-Application-ID","f91c6ba2")
                            .build();
                    return chain.proceed(request);

                }
            })
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS)
                    .build();

        return client;
    }

    public  void getResult(String text, final BuyListener listener)
    {
        buildApi().getResult(text).enqueue(new Callback<BuyOp>() {
            @Override
            public void onResponse(Call<BuyOp> call, Response<BuyOp> response) {

                if(response.code()==200)
                    listener.onSuccess(response.body());

                else  listener.onFailure(response.message());
            }

            @Override
            public void onFailure(Call<BuyOp> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void getSellResult(String name, String number, String price, final SellListener listener)
    {
        Log.d("HELLO", "getSellResult: ");
        buildApi().getSellResult(name,number,price).enqueue(new Callback<BuyOp>() {
            @Override
            public void onResponse(Call<BuyOp> call, Response<BuyOp> response) {
                if(response.code()==200)
                {
                    listener.onSellSuccess(response.body());

                }
                else listener.onSellFailure(response.message());
            }

            @Override
            public void onFailure(Call<BuyOp> call, Throwable t) {
                listener.onSellFailure(t.getMessage());
            }
        });
    }

    public interface BuyListener
    {
        void onSuccess(BuyOp response);
        void onFailure(String t);
    }

    public interface SellListener
    {
        void onSellSuccess(BuyOp response);
        void onSellFailure(String t);
    }
}
