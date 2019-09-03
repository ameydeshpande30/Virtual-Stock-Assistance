package com.wce.barclays.data;

import com.wce.barclays.model.Sample;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test {
    private static OkHttpClient client;


    private static TestApi api;

    private static TestApi buildApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl("http://192.168.43.154:3000/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build()
                .create(TestApi.class);
        }
        return api;
    }


    private static OkHttpClient buildClient()
    {
        if(client==null)
            client = new OkHttpClient.Builder().build();

        return client;
    }
    public void getChecksum(final onChecksumGenerated onChecksumGenerated)
    {

        buildApi().getChecksum().enqueue(new Callback<List<Sample>>() {
            @Override
            public void onResponse(Call<List<Sample>> call, Response<List<Sample>> response) {
                onChecksumGenerated.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Sample>> call, Throwable t) {
                onChecksumGenerated.onFailure(t.toString());
            }
        });
    }
    public interface onChecksumGenerated
    {
        void onSuccess(List<Sample> checksum);
        void onFailure(String t);
    }
}
