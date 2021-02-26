package com.example.tapis_project_v0.serviceImpl.proprieteServiceImpl;

import android.app.ActionBar;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tapis_project_v0.Common.utils.Consts;
import com.example.tapis_project_v0.model.Propriete;
import com.example.tapis_project_v0.service.ProprieteService;
import com.example.tapis_project_v0.ui.AjouterMotifProprieteActivity;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropeiterServiceImpl extends Service {
    private static ProprieteService propreiteService;
    private static final String TAG = "AddMotifService";
    static Retrofit retrofit;
    private CustomSpinner spinner;
    private CustomPopup popup;

    public static void getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Consts.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        propreiteService = retrofit.create(ProprieteService.class);
    }

    public Propriete savePropeiter(Propriete proprie, long id) {
        getClient();
        Call<Propriete> call = propreiteService.saveProprite(proprie, id);
        Toast.makeText(getApplicationContext(), "bda save", Toast.LENGTH_SHORT).show();
        spinner.show();
        Window window = spinner.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        call.enqueue(new Callback<Propriete>() {
            @Override
            public void onResponse(Call<Propriete> call, Response<Propriete> response) {
                if (response.isSuccessful()) {
                    spinner.dismiss();
                    Propriete proprieteSaved = response.body();
                    Log.i(TAG, "onResponse: " + proprieteSaved.getLibelle());
                    Intent intent = new Intent(AjouterMotifProprieteActivity.getmContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Log.d("resultat requette", "Boo!");
                    return;
                }
            }

            @Override
            public void onFailure(Call<Propriete> call, Throwable t) {
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
        Bundle bundle = intent.getExtras();
        Propriete proprie = new Propriete();
        String libelle = bundle.getString("libelle");
        String description = bundle.getString("description");
        long idMotif = bundle.getLong("idMotif");
        popup = new CustomPopup(MainActivity.getContext());
        popup.getButton().setOnClickListener((v) ->
        {
            popup.dismiss();
        });
//        Log.i(TAG, "onStartCommand: " + getApplicationContext().);
        spinner = new CustomSpinner(MainActivity.getContext());
        proprie.setDescription(description);
        proprie.setLibelle(libelle);
        savePropeiter(proprie, idMotif);
        return START_NOT_STICKY;
    }
}
