package com.example.tapis_project_v0.model;

import java.io.Serializable;

public class Propriete implements Serializable {
    private long id;
    private String libelle;
    private String description;
    private Motif motif;

    public Propriete() {
    }

    public Propriete(long id, String description, String libelle) {
        this.id = id;
        this.description = description;
        this.libelle = libelle;
    }

    public Propriete(long id, String libelle, String description, Motif motif) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
        this.motif = motif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Motif getMotif(Motif motif) {
        return this.motif;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
    }
}

