package com.example.tapis_project_v0.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tapis_project_v0.Common.database.DatabaseHelper;
import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.Motif;
import com.example.tapis_project_v0.model.Propriete;
import com.example.tapis_project_v0.model.Role;
import com.example.tapis_project_v0.model.User;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.serviceImpl.proprieteServiceImpl.PropeiterServiceImpl;
import com.example.tapis_project_v0.serviceImpl.userMotifServiceImpl.GetUsersMotifsServiceImpl;
import com.example.tapis_project_v0.ui.fragment.AccountFragment;
import com.example.tapis_project_v0.ui.fragment.AjouterMotifFragment;
import com.example.tapis_project_v0.ui.fragment.HomeFragment;
import com.example.tapis_project_v0.ui.listner.DialogCloseListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private DatabaseHelper databaseHelper;
    private List<UserMotif> userMotifs;
    private User currentUser;
    private static Context context;
    private UserMotif userMotif = new UserMotif();
    private List<Propriete> proprietes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        // check if the user has granted the permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        context = MainActivity.this;

        databaseHelper = new DatabaseHelper(MainActivity.this);
        currentUser = databaseHelper.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.camFab);

        bottomNavigationView.setBackground(null);

        if (currentUser.getRole().equals(Role.ADMIN)) {
            bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_admin_panel_settings_24);
            bottomNavigationView.getMenu().getItem(0).setTitle("Admin");
            bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_baseline_add_24);
            bottomNavigationView.getMenu().getItem(1).setTitle("Ajouter");
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else {
            bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        }

        Intent intent = new Intent(MainActivity.this, GetUsersMotifsServiceImpl.class);
        intent.putExtra("idUser", currentUser.getId());
        startService(intent);

        bottomNavigationView.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_content, new HomeFragment())
                        .commit();
                if (currentUser.getRole().equals(Role.USER))
                    floatingActionButton.setVisibility(View.VISIBLE);
                return true;
            }
        });

        bottomNavigationView.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_content, new AjouterMotifFragment())
                        .commit();
                return true;
            }
        });

        bottomNavigationView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_content, new AccountFragment())
                        .commit();
                floatingActionButton.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpenCVCameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            userMotifs = (List<UserMotif>) bundle.getSerializable("motifs");
            Log.i("MainActivity TAG", "onReceive: " + userMotifs.size());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_content, new HomeFragment())
                    .commit();
        }
    };

    private final BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Motif motif = (Motif) bundle.getSerializable("storedMotif");
            for (Propriete propriete: proprietes) {
                propriete.getMotif(motif);
                intent = new Intent(MainActivity.this, PropeiterServiceImpl.class);
                bundle.putString("description", propriete.getDescription());
                bundle.putString("libelle", propriete.getLibelle());
                bundle.putLong("idMotif", motif.getId());
                intent.putExtras(bundle);
                startService(intent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.intent.action.MainActivityCustomReciever");
        registerReceiver(mReceiver, mIntentFilter);

        IntentFilter mIntentFilter2 = new IntentFilter();
        mIntentFilter2.addAction("android.intent.action.MainActivityCustomReciever2");
        registerReceiver(mReceiver, mIntentFilter);
    }

    public List<UserMotif> getUserMotifs() {
        return userMotifs;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static Context getContext() {
        return context;
    }

    public List<Propriete> getProprietes() {
        return proprietes;
    }

    public void setProprietes(List<Propriete> proprietes) {
        this.proprietes = proprietes;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Log.i("TAG", "handleDialogClose: " + proprietes.size());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_content, new AjouterMotifFragment())
                .commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}