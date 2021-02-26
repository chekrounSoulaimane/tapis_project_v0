package com.example.tapis_project_v0.serviceImpl.motifServiceImpl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tapis_project_v0.Common.utils.Consts;
import com.example.tapis_project_v0.model.Motif;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.service.MotifService;
import com.example.tapis_project_v0.service.UserService;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Part;

public class SaveServiceImpl extends Service {
    static Retrofit retrofit;
    static private MotifService motifService;
    private CustomPopup popup;
    private CustomSpinner spinner;

    public static void getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Consts.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        motifService = retrofit.create(MotifService.class);
    }

    public static void save(File file, Motif motif) {
        getClient();
        Log.i("Motif info", "/////////////////////////");
        final List<UserMotif> fetchedMotifs = new ArrayList();

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpg"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<Motif> call = motifService.save(description, body, motif);
        call.enqueue(new Callback<Motif>() {
            @Override
            public void onResponse(Call<Motif> call, Response<Motif> response) {
                if (response.isSuccessful()) {
                    Motif motif1 = response.body();
                    Log.i("info", motif1.toString());
                } else {
                    Log.d("Yo", "Boo!");
                    return;
                }
            }

            @Override
            public void onFailure(Call<Motif> call, Throwable t) {
                Log.i("info", t.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "onStartCommand: start");
        Bundle bundle = intent.getExtras();
        File picture = (File) bundle.getSerializable("picture");
        Motif motif = (Motif) bundle.getSerializable("motif");
        popup = new CustomPopup(getApplicationContext());
        popup.getButton().setOnClickListener((v) ->
        {
            popup.dismiss();
        });
        spinner = new CustomSpinner(getApplicationContext());
        save(picture, motif);
        return START_NOT_STICKY;
    }
}
