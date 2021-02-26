package com.example.tapis_project_v0.serviceImpl.motifServiceImpl;

import android.app.ActionBar;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;

import com.example.tapis_project_v0.Common.utils.Consts;
import com.example.tapis_project_v0.model.Motif;
import com.example.tapis_project_v0.service.MotifService;
import com.example.tapis_project_v0.ui.AjouterMotifProprieteActivity;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.fragment.AjouterMotifFragment;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;

import java.io.File;
import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMotifServiceImpl extends Service {
    private static MotifService motifService;
    private static final String TAG = "AddMotifService";
    private CustomSpinner spinner;
    private CustomPopup popup;

    static Retrofit retrofit;


    public static void getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Consts.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        motifService = retrofit.create(MotifService.class);
    }

    public Motif saveMotif(Motif motif, File file) {
        getClient();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//        User u = new DatabaseHelper(getApplicationContext()).getCurrentUser();
        Call<Motif> call = motifService.saveMotif(motif.getLibelle(), motif.getDescription(), body, 1);
        spinner.show();
        Window window = spinner.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        call.enqueue(new Callback<Motif>() {
            @Override
            public void onResponse(Call<Motif> call, Response<Motif> response) {
                if (response.isSuccessful()) {
                    spinner.dismiss();
                    Motif motifSaved = response.body();
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("android.intent.action.CustomReciever");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("value", (Serializable) motifSaved);
                    broadcastIntent.putExtras(bundle);
                    sendBroadcast(broadcastIntent);
                } else {
                    Log.d("resultat requette", "Boo!");
                    return;
                }
            }

            @Override
            public void onFailure(Call<Motif> call, Throwable t) {
                Log.i("info", t.getMessage());
                spinner.dismiss();
                popup.setTitle("Ouuups");
                popup.setContent("Erreur 500");
                popup.build();

            }
        });
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: start");
        Bundle bundle = intent.getExtras();
        String libelle = bundle.getString("libelle");
        String description = bundle.getString("description");
        File picture = (File) bundle.getSerializable("picture");
        popup = new CustomPopup(MainActivity.getContext());
        popup.getButton().setOnClickListener((v) ->
        {
            popup.dismiss();
        });
//        Log.i(TAG, "onStartCommand: " + getApplicationContext().);
        spinner = new CustomSpinner(MainActivity.getContext());
        saveMotif(new Motif(libelle, description), picture);
        return START_NOT_STICKY;
    }
}
