package com.example.tapis_project_v0.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tapis_project_v0.Common.utils.ValidationData;
import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.Motif;
import com.example.tapis_project_v0.model.Propriete;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.serviceImpl.motifServiceImpl.AddMotifServiceImpl;
import com.example.tapis_project_v0.serviceImpl.motifServiceImpl.MotifServiceImpl;
import com.example.tapis_project_v0.ui.AjouterMotifProprieteActivity;
import com.example.tapis_project_v0.ui.Helper.RecyclerItemTouchHelper;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.OpenCVCameraActivity;
import com.example.tapis_project_v0.ui.adapter.MotifProprieteRecyclerAdapter;
import com.example.tapis_project_v0.ui.view.CustomPopup;
import com.example.tapis_project_v0.ui.view.CustomSpinner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjouterMotifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjouterMotifFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Ajouter";
    private RecyclerView propritesRecyclerView;
    private CircularProgressButton circularProgressButton, cirButtonAjouterPhoto;
    private MotifProprieteRecyclerAdapter motifProprieteRecyclerAdapter;
    private EditText editTextLibelle, editTextDescription;
    private Button buttonSauvegarderMotif;
    private CustomPopup popup;
    private CustomSpinner spinner;
    public static Context context;
    private Bitmap bitmap;
    private ImageView imageAChange;
    private boolean updateMode = false;
    private UserMotif userMotif;
    private Motif motif;

    private List<Propriete> proprietes = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjouterMotifFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjouterMotifFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjouterMotifFragment newInstance(String param1, String param2) {
        AjouterMotifFragment fragment = new AjouterMotifFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ajouter_motif, container, false);


        propritesRecyclerView = view.findViewById(R.id.propritesRecyclerView);
        propritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        circularProgressButton = view.findViewById(R.id.cirButtonAjouterMotif);
        cirButtonAjouterPhoto = view.findViewById(R.id.cirButtonAjouterPhoto);
        buttonSauvegarderMotif = view.findViewById(R.id.buttonSaveMotif);
        editTextLibelle = view.findViewById(R.id.editTextLibelleMotif);
        editTextDescription = view.findViewById(R.id.editTextDescriptionMotif);
        imageAChange = view.findViewById(R.id.imageAChange);

        motifProprieteRecyclerAdapter = new MotifProprieteRecyclerAdapter((MainActivity) getActivity());
        propritesRecyclerView.setAdapter(motifProprieteRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(motifProprieteRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(propritesRecyclerView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userMotif = (UserMotif) bundle.getSerializable("userMotif");
            Picasso.get().load(userMotif.getFileUrl()).into(imageAChange);
            editTextLibelle.setText(userMotif.getMotif().getLibelle());
            editTextDescription.setText(userMotif.getMotif().getDescription());
            proprietes.addAll(userMotif.getMotif().getProprietes());
            cirButtonAjouterPhoto.setText("modifier image");
            buttonSauvegarderMotif.setText("modifier");
            updateMode = true;
        }

        MainActivity activity = (MainActivity) getActivity();
        proprietes.addAll(activity.getProprietes());
        motifProprieteRecyclerAdapter.setTasks(proprietes);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjouterMotifProprieteActivity.newInstance().show(getActivity().getSupportFragmentManager(), AjouterMotifProprieteActivity.TAG);
            }
        });

        cirButtonAjouterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        buttonSauvegarderMotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationData()) {
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] bytes = stream.toByteArray();
                        File pictureFile = OpenCVCameraActivity.getOutputMediaFile(OpenCVCameraActivity.MEDIA_TYPE_IMAGE);
                        Log.i("TAG", "onCameraFrame: " + pictureFile.toString());
                        try {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(bytes);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bundle.putSerializable("picture", pictureFile);
                    } else {

                        Picasso.get().load(userMotif.getFileUrl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap1, Picasso.LoadedFrom from) {
                                // Set it in the ImageView
                                bitmap = bitmap1;
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                                byte[] bytes = stream.toByteArray();
                                File pictureFile = OpenCVCameraActivity.getOutputMediaFile(OpenCVCameraActivity.MEDIA_TYPE_IMAGE);
                                Log.i("TAG", "onCameraFrame: " + pictureFile.toString());
                                try {
                                    FileOutputStream fos = new FileOutputStream(pictureFile);
                                    fos.write(bytes);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                bundle.putSerializable("picture", (Serializable) pictureFile);
                                bundle.putLong("idUserMotif", userMotif.getId());
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                }

                Motif motif = new Motif();
                motif.setId(userMotif.getMotif().getId());
                motif.setLibelle(editTextLibelle.getText().toString());
                motif.setDescription(editTextDescription.getText().toString());
                motif.setProprietes(proprietes);

                Intent intent;
                if (updateMode) {
                    intent = new Intent(getContext(), MotifServiceImpl.class);
                    motif.setProprietes(proprietes);
                    bundle.putSerializable("updatedMotif", (Serializable) motif);
                    intent.putExtras(bundle);
                    getContext().startService(intent);
                } else {
                    intent = new Intent(getContext(), AddMotifServiceImpl.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("libelle", motif.getLibelle());
                    bundle.putString("description", motif.getDescription());
                    intent.putExtras(bundle);
                    getContext().startService(intent);
                }
            }
        });

        return view;
    }

    private boolean validationData() {
        if (!ValidationData.fieldValidation(editTextLibelle.getText().toString())) {
            editTextLibelle.setError("Invalid data");
            editTextLibelle.requestFocus();
            return false;
        }
        if (!ValidationData.fieldValidation(editTextDescription.getText().toString())) {
            editTextDescription.setError("Invalid data");
            editTextDescription.requestFocus();
            return false;
        }
        if (bitmap == null && !updateMode) {
            popup.setTitle("Ouuups");
            popup.setContent("Merci de Choisir une photo");
            popup.build();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Uri chosenImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), chosenImageUri);
                imageAChange.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}