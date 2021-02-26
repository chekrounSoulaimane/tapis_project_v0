package com.example.tapis_project_v0.serviceImpl.userMotifServiceImpl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tapis_project_v0.Common.utils.Consts;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.service.UserMotifService;
import com.example.tapis_project_v0.ui.AdminHomepageActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetUsersMotifsServiceImpl extends Service {
    private static final String TAG = "UserMotifSerice";
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Consts.API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static private final UserMotifService userMotifService = retrofit.create(UserMotifService.class);

    public void getAllUserMotifs(Long id) {
        Call<List<UserMotif>> call = userMotifService.getAllUserMotif(id);
        final List<UserMotif> fetchedUserMotif = new ArrayList<>();
        call.enqueue(new Callback<List<UserMotif>>() {
            @Override
            public void onResponse(Call<List<UserMotif>> call, Response<List<UserMotif>> response) {
                Log.i(TAG, "getAllUserMotif: " + response.body());
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("android.intent.action.MainActivityCustomReciever");

                Bundle bundle = new Bundle();
                if (response.isSuccessful()) {
                    List<UserMotif> userMotifs = response.body();
                    fetchedUserMotif.addAll(userMotifs);
                    bundle.putSerializable("motifs", (Serializable) fetchedUserMotif);
                    broadcastIntent.putExtras(bundle);
                    sendBroadcast(broadcastIntent);
                } else {
                    Log.d("Yo", "Boo!");
                }
            }

            @Override
            public void onFailure(Call<List<UserMotif>> call, Throwable t) {
                Log.i("TAGerr", t.getMessage());
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
        Long id = intent.getLongExtra("idUser", 0);
        Log.i("info OnStartCOmmande : ", id.toString());
        getAllUserMotifs(id);
        return START_NOT_STICKY;
    }
}
