package com.wce.barclays.data;


import com.wce.barclays.model.Sample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestApi {

    @GET("hi")
    Call<List<Sample>> getChecksum();
}