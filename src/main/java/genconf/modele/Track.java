package genconf.modele;

import java.io.Serializable;

public class Track implements Serializable
{
    private String libelle, couleur;

    public Track(String libelle, String couleur) {
        this.libelle = libelle;
        this.couleur = couleur;
    }

    // getters and setters
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
