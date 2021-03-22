package genconf.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;

public class Session implements Serializable
{
    private String nom, salle;
    private LocalDate jour;
    private int heureDebut, heureFin;
    private final HashSet<Track> tracks;
    private final HashSet<Communication> communications;

    public Session(String nom, String salle, LocalDate jour, int heureDebut, int heureFin)
    {
        this.nom = nom;
        this.salle = salle;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.tracks = new HashSet<>();
        this.communications = new HashSet<>();
    }

    // methodes
    public void addTrack(Track track)
    {
        tracks.add(track);
    }

    public void addCom(Communication communication)
    {
        communications.add(communication);
    }

    // setters and getters
    public HashSet<Track> getTracks()
    {
        return tracks;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public LocalDate getJour() {
        return jour;
    }

    public void setJour(LocalDate jour) {
        this.jour = jour;
    }

    public int getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(int heureDebut) {
        this.heureDebut = heureDebut;
    }

    public int getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(int heureFin) {
        this.heureFin = heureFin;
    }
}
