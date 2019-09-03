package com.wce.barclays.data.testrazor;

import com.wce.barclays.model.Word;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TestRazorApi {
   // @FormUrlEncoded
//    @Headers("X-TextRazor-Key: 27f8fa7eae35ec0211e312efec11463f1e6db0496b2443f19b309886")
    @POST("entities")
    Call<Word> getCompanyName(@Query("text") String text);
}
