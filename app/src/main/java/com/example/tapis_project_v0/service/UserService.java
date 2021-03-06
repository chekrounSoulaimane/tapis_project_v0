package com.example.tapis_project_v0.service;

import com.example.tapis_project_v0.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("user/")
    Call<List> getUsers();

    @POST("user/register")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<User> register(@Body User user);

    @GET("user/login/{login}/password/{password}")
    Call<User> login(@Path("login") String login, @Path("password") String password);

    @PUT("user/update")
    Call<User> update(@Body User user);
}
