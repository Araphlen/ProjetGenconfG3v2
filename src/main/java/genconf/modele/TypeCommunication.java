/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genconf.modele;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author gaudetb
 */
public class TypeCommunication implements Serializable
{
    private String nom;
    private boolean pdf, video, lienVideoConf;
    
    // constructeur
    public TypeCommunication(String nom)
    {
        this.nom = nom;
        this.pdf = false;
        this.video = false;
        this.lienVideoConf = false;
   }

   // getters and setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isPdf() {
        return pdf;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isLienVideoConf() {
        return lienVideoConf;
    }

    public void setLienVideoConf(boolean lienVideoConf) {
        this.lienVideoConf = lienVideoConf;
    }
}
