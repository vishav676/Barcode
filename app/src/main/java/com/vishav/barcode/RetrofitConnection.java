package com.vishav.barcode;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://ec2-3-13-208-251.us-east-2.compute.amazonaws.com:8000/api/";

    public static Retrofit getRetroFitInstance()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
