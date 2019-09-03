package com.wce.barclays.data.BuyOutput;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BuyApi {

    @GET("buy/{name}")
    Call<BuyOp> getResult(@Path("name") String name);

    @GET("sell/{name}/{number}/{price}")
    Call<BuyOp> getSellResult(@Path("name")String name, @Path("number")String number, @Path("price")String price);

}
