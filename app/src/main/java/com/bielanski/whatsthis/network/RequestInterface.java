package com.bielanski.whatsthis.network;

import com.bielanski.whatsthis.network.data.WikiInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterface {
    @GET("api/rest_v1/page/summary/{title}")
    Call<WikiInfo> getJSON(@Path("title") String title);
}
