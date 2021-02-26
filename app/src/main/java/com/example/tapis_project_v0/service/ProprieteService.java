package com.example.tapis_project_v0.service;

import com.example.tapis_project_v0.model.Motif;
import com.example.tapis_project_v0.model.Propriete;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProprieteService {
    @POST("propriete/{id}/")
    Call<Propriete> saveProprite(@Body Propriete postprie, @Path("id") long id);

    @PUT("propriete/update")
    Call<Void> update(@Body Motif motif);

    @DELETE("propriete/delete/{id}")
    Call<Void> deletePropriete(@Path("id") long id);
}
