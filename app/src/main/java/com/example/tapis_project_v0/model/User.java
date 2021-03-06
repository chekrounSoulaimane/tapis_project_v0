package com.example.tapis_project_v0.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private Long id;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    private Role role;
    private byte[] profile_image;
    private List<Motif> motifs;

    public User(Long id, String nom, String prenom, String login, String password, Role role, byte[] profile_image, List<Motif> motifs) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
        this.role = role;
        this.profile_image = profile_image;
        this.motifs = motifs;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public User(Long id, String nom, String prenom, String login, String password, Role role, byte[] profile_image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
        this.role = role;
        this.profile_image = profile_image;
    }

    public User() { }


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public byte[] getProfile_image() {
        return profile_image;
    }

    public List<Motif> getMotifs() {
        return motifs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setProfile_image(byte[] profile_image) {
        this.profile_image = profile_image;
    }

    public void setMotifs(List<Motif> motifs) {
        this.motifs = motifs;
    }
}
