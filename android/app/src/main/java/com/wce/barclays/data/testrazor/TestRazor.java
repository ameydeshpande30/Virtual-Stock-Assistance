package com.wce.barclays.data.testrazor;

import com.wce.barclays.model.Word;

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

public class TestRazor {
    private static OkHttpClient client;
    TextRazorListener textRazorListener;

    private static TestRazorApi api;
    private static TestRazorApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl("https://api.aylien.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
                .create(TestRazorApi.class);
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

    public  void getCompanyName(String text, final TextRazorListener listener)
    {
        buildApi().getCompanyName(text).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {

                if(response.code()==200)
                listener.onSuccess(response.body());

                else  listener.onFailure(response.message());
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
        listener.onFailure(t.getMessage());
            }
        });
    }
    
    public interface TextRazorListener
    {
        void onSuccess(Word response);
        void onFailure(String t);
    }
}
