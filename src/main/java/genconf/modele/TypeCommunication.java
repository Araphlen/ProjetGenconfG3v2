/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genconf.modele;

import java.util.HashSet;

/**
 *
 * @author gaudetb
 */
public class TypeCommunication
{
    private String nom;
    private boolean pdf, video, lienVideoConf;
    private Conference conference;
    
    private HashSet<Conference> conferences;
    
    // constructeur
    public TypeCommunication(String nom, Conference conference)
    {
        this.nom = nom;
        this.pdf = false;
        this.video = false;
        this.lienVideoConf = false;
        this.conference = conference;
        this.conference.addTypeCom(nom, this);
   }
    
    // setter
    public void setConf(Conference conference)
    {
        this.conferences.add(conference);
    }
    
}
