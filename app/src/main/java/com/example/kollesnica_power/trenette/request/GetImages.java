package com.example.kollesnica_power.trenette.request;

import com.example.kollesnica_power.trenette.model.ImageData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kollesnica1337 on 10.08.2017.
 * Request interface to get images from api.
 */

public interface GetImages {

    @GET("/viktor_jakovlev/")
    Call<List<ImageData>> getData();

}
