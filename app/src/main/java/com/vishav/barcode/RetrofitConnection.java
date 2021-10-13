package com.vishav.barcode;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://vishav70870.pythonanywhere.com/api/";

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
