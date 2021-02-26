package com.example.tapis_project_v0.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.Propriete;
import com.example.tapis_project_v0.ui.listner.DialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AjouterMotifProprieteActivity extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText editTextLibellePropriete, editTextDescriptionPropriete;
    private Button buttonAddNewTask;
    private static Context mContext;

    public static AjouterMotifProprieteActivity newInstance(){
        return new AjouterMotifProprieteActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_ajouter_motif_propriete, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextLibellePropriete = requireView().findViewById(R.id.editTextLibelleMotif);
        editTextDescriptionPropriete = requireView().findViewById(R.id.editTextDescriptionMotif);
        buttonAddNewTask = getView().findViewById(R.id.cirButtonAjouterMotif);

        mContext = getActivity().getApplicationContext();
        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String libelle = bundle.getString("libelle");
            String description = bundle.getString("description");
            editTextLibellePropriete.setText(libelle);
            editTextDescriptionPropriete.setText(description);
            assert libelle != null;
            if(libelle.length()>0)
                buttonAddNewTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }

        editTextLibellePropriete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    buttonAddNewTask.setEnabled(false);
                    buttonAddNewTask.setTextColor(Color.GRAY);
                }
                else{
                    buttonAddNewTask.setEnabled(true);
                    buttonAddNewTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.whiteTextColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        buttonAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libelle = editTextLibellePropriete.getText().toString();
                String descrition = editTextLibellePropriete.getText().toString();
                if(finalIsUpdate){
                }
                else {
                    Propriete propriete = new Propriete();
                    propriete.setLibelle(libelle);
                    propriete.setDescription(descrition);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.getProprietes().add(propriete);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }


    public static Context getmContext() {
        return mContext;
    }
}