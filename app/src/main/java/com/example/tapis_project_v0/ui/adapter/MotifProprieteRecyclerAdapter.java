package com.example.tapis_project_v0.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.model.Propriete;
import com.example.tapis_project_v0.ui.AjouterMotifProprieteActivity;
import com.example.tapis_project_v0.ui.MainActivity;

import java.util.List;

public class MotifProprieteRecyclerAdapter extends RecyclerView.Adapter<MotifProprieteRecyclerAdapter.ViewHolder> {

    private List<Propriete> proprietes;
    private MainActivity mainActivity;

    public MotifProprieteRecyclerAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView proprieteLibelle, proprieteDescription;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            proprieteLibelle = (TextView) view.findViewById(R.id.proprieteLibelle);
            proprieteDescription = (TextView) view.findViewById(R.id.proprieteDescription);
        }

        public TextView getProprieteLibelle() {
            return proprieteLibelle;
        }

        public TextView getProprieteDescription() {
            return proprieteDescription;
        }
    }


    @NonNull
    @Override
    public MotifProprieteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.propriete_sheet_layout, parent, false);

        return new ViewHolder(view);
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MotifProprieteRecyclerAdapter.ViewHolder holder, int position) {
        Propriete propriete = proprietes.get(position);
        holder.getProprieteLibelle().setText(propriete.getLibelle());
        holder.getProprieteDescription().setText(propriete.getDescription());
    }

    @Override
    public int getItemCount() {
        if (proprietes == null) {
            return 0;
        } else {
            return proprietes.size();
        }
    }

    public void setTasks(List<Propriete> proprietes) {
        this.proprietes = proprietes;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return mainActivity;
    }

    public void deleteItem(int position) {
        proprietes.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        Propriete propriete = proprietes.get(position);
        Bundle bundle = new Bundle();
        bundle.putLong("id", propriete.getId());
        bundle.putString("libelle", propriete.getLibelle());
        bundle.putString("description", propriete.getDescription());
        AjouterMotifProprieteActivity fragment = new AjouterMotifProprieteActivity();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AjouterMotifProprieteActivity.TAG);
    }
}
