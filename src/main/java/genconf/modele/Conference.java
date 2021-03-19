package genconf.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Conference implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final GenConf genconf;
    private final String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private final Map<String, Utilisateur> administrateurs;  // association qualifiée par l'email
    private final Map<String, TypeCommunication> typesCom;  // association par nom typeCom
    private final Map<String, Track> tracks;
    private final Map<String, Session> sessions;

    // Invariant de classe : !dateDebut.isAfter(dateFin)
    //     On utilise la négation ici pour exprimer (dateDebut <= dateFin), ce
    //     qui est équivalent à !(dateDebut > dateFin).

    public static Conference initialiseConference(GenConf genconf, String nom, LocalDate dateDebut, LocalDate dateFin, Utilisateur admin) {
        Conference conf = new Conference(genconf, nom, dateDebut, dateFin);
        conf.ajouteAdministrateur(admin);
        return conf;
    }

    public Conference(GenConf genconf, String nom, LocalDate dateDebut, LocalDate dateFin) {
        assert !dateDebut.isAfter(dateFin);
        this.genconf = genconf;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.administrateurs = new HashMap<>();
        this.typesCom = new HashMap<>();
        this.tracks = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    public String getNom() {
        return this.nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        assert !dateDebut.isAfter(this.dateFin);
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        assert !this.dateDebut.isAfter(dateFin);
        this.dateFin = dateFin;
    }
    
    
    public Collection <Utilisateur> getAdmin(){
        return this.administrateurs.values();
    } 

    public void ajouteAdministrateur(Utilisateur admin) {
        assert !this.administrateurs.containsKey(admin.getEmail());
        this.administrateurs.put(admin.getEmail(), admin);
        admin.ajouteConferenceAdministree(this);
    }
    
    // methodes typeComm
    public void addTypeCom(String nom, TypeCommunication typeCom)
    {
        typesCom.put(nom, typeCom);
    }

    public Map<String, TypeCommunication> getTypesCom()
    {
        return this.typesCom;
    }
    
    public boolean existTypeCom(String nom)
    {
        return typesCom.containsKey(nom);
    }


    // methodes track
    public void addTrack(Track track)
    {
        tracks.put(track.getLibelle(), track);
    }

    public boolean existTrack(String libelle)
    {
        return tracks.containsKey(libelle);
    }

    public Map<String, Track> getTracks()
    {
        return this.tracks;
    }

    // fonction session
    public void addSession(Session session)
    {
        this.sessions.put(session.getNom(), session);
    }

    public Map<String, Session> getSessions()
    {
        return this.sessions;
    }
}
