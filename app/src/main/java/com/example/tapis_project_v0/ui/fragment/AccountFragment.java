package com.example.tapis_project_v0.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tapis_project_v0.Common.database.DatabaseHelper;
import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.User;
import com.example.tapis_project_v0.serviceImpl.userServiceImpl.UserServiceImpl;
import com.example.tapis_project_v0.ui.LoginActivity;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper databaseHelper;
    private CustomPopup popup;
    private CustomSpinner spinner;
    private View rootView;
    private TextInputEditText nom, prenom, password, newPassword, cNewPassword;
    private TextInputLayout nomInput;
    private Button enregister,logout;
    private TextView login;
    private Button update_prf;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        User user;
        user = databaseHelper.getCurrentUser();
        Log.i("info", user.getLogin());
        nom = rootView.findViewById(R.id.nom);
        prenom = rootView.findViewById(R.id.prenom);
        login = rootView.findViewById(R.id.login);
        newPassword = rootView.findViewById(R.id.newPassword);
        cNewPassword = rootView.findViewById(R.id.cNewPassword);
        password = rootView.findViewById(R.id.password);
        enregister = rootView.findViewById(R.id.save);
        logout = rootView.findViewById(R.id.logout);
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        login.setText(user.getLogin());
        logout.setOnClickListener(v -> {
            databaseHelper.delete(user);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

        });
        String passwordValue = password.getText().toString();
        popup = new CustomPopup(getContext());
        spinner = new CustomSpinner(getContext());
        popup.getButton().setOnClickListener(v -> {
            popup.dismiss();
            password.setText("");
            newPassword.setText("");
            cNewPassword.setText("");
        });
        enregister.setOnClickListener(v -> {
            String nomValue, prenomValue;
            nomValue = nom.getText().toString();
            prenomValue = prenom.getText().toString();
            if (!nomValue.equals(user.getNom()) || !prenomValue.equals(user.getPrenom())) {
                user.setNom(nomValue);
                user.setPrenom(prenomValue);

                // save user in the remote database
            }
            if (!password.getText().toString().equals("")) {
                if (!password.getText().toString().equals(user.getPassword())) {
                    popup.setTitle("Ouuuups");
                    popup.setContent("Mot de passe actuel erroné");
                    popup.build();
                } else {
                    if (!newPassword.getText().toString().equals(cNewPassword.getText().toString()) && !newPassword.getText().equals("")) {
                        popup.setTitle("Ouuuups");
                        popup.setContent("Nouveau mot de passe et mot de passe de confirmation sont différents ");
                        popup.build();
                    } else {
                        // traitement
                        user.setPassword(newPassword.getText().toString());

                    }
                }
            }
            databaseHelper.update(user);
            UserServiceImpl.update(user,popup,spinner);
            Log.i("user info",databaseHelper.getCurrentUser().getPassword());
            // traitement backend
        });
        if (!passwordValue.equals(user.getPassword())) {

        }
        return rootView;
    }
}