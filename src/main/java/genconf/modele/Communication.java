/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genconf.modele;

import java.io.Serializable;

/**
 *
 * @author araphlen
 */
public class Communication implements Serializable
{
    private int numCom;
    private String titre, auteurs, pdf, video, lienVideoConf;

    public Communication(int numCom, String titre, String auteurs)
    {
        this.numCom = numCom;
        this.titre = titre;
        this.auteurs = auteurs;
        this.pdf = null;
        this.video = null;
        this.lienVideoConf = null;
    }





    // getters and getters
    public int getNumCom() {
        return numCom;
    }

    public void setNumCom(int numCom) {
        this.numCom = numCom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(String auteurs) {
        this.auteurs = auteurs;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getLienVideoConf() {
        return lienVideoConf;
    }

    public void setLienVideoConf(String lienVideoConf) {
        this.lienVideoConf = lienVideoConf;
    }
}
