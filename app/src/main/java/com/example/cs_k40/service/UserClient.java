package com.example.cs_k40.service;

import com.example.cs_k40.model.Login;
import com.example.cs_k40.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface UserClient {
    @POST("login")
    Call<User>login(@Body Login login);

    @POST("register")
    Call<User>register(@Body User user);

    @GET("user-info")
    Call<User> getUserInfo(@Header("Authorization") String authorToken);
}