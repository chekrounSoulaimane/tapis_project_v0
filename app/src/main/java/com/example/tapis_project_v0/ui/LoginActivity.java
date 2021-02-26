package com.example.tapis_project_v0.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tapis_project_v0.Common.database.DatabaseHelper;
import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.Role;
import com.example.tapis_project_v0.model.User;
import com.example.tapis_project_v0.serviceImpl.userMotifServiceImpl.UserMotifServiceImpl;
import com.example.tapis_project_v0.serviceImpl.userServiceImpl.UserServiceImpl;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin, editTextPassword;
    private CircularProgressButton loginCircularProgressButton;
    private CustomPopup popup;
    private CustomSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        this.getSupportActionBar().hide();

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginCircularProgressButton = findViewById(R.id.cirLoginButton);
        popup = new CustomPopup(this);
        spinner = new CustomSpinner(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        User user = databaseHelper.getCurrentUser();


        if (user != null) {
            Log.i("TAG", "onCreate: user id" + user.getRole());
//            if (user.getRole().equals(Role.ADMIN)) {
//                Intent i = new Intent(LoginActivity.this, UserMotifServiceImpl.class);
//                Long id = user.getId();
//                i.putExtra("idUser", String.valueOf(id));
//                LoginActivity.this.startService(i);
//            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

        } else {
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
            // redirection après la connexion
            Intent intent = new Intent(this, RegisterActivity.class);
            popup.getButton().setOnClickListener(v1 -> {
                popup.dismiss();
                editTextLogin.setText("");
                editTextPassword.setText("");
            });

            loginCircularProgressButton.setOnClickListener(v -> {
                String loginValue = editTextLogin.getText().toString();
                String passwordValue = editTextPassword.getText().toString();
                if (loginValue.equals("") || passwordValue.equals("")) {
                    showPopup("Ouups", "tous les champs sont obligatoires");
                } else {
                    UserServiceImpl.login(loginValue, passwordValue, spinner, popup, intent1, this);
                    finish();
                }
            });
        }
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);

    }

    public void showPopup(String title, String content) {
        popup.setTitle(title);
        popup.setContent(content);

        popup.build();
        popup.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }
}
