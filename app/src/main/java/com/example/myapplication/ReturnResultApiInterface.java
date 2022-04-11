package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ReturnResultApiInterface {
    @GET("ParkingCar/public/get_parking")
    Call<List<ReturnResult>> getResultList();
}
