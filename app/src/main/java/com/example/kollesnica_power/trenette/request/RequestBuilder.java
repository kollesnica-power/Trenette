package com.example.kollesnica_power.trenette.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kollesnica1337 on 10.08.2017.
 * Building requests.
 */

public class RequestBuilder {


    private static Retrofit build(String url){

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static GetImages imagesRequest(String url){

        return build(url).create(GetImages.class);

    }

}
