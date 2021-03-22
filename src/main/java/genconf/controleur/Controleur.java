package genconf.controleur;

import genconf.modele.*;
import genconf.vue.IHM;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class Controleur {

    private final GenConf genconf;
    private final IHM ihm;

    public Controleur(GenConf genconf) {
        this.genconf = genconf;

        // choisir la classe CLI ou GUI
//        this.ihm = new CLI(this);
        this.ihm = new IHM(this);
    }

    public void demarrer() {
        this.ihm.afficherInterface();
    }

    public void gererDialogue(Commande cmd) {
        switch (cmd) {
            case QUITTER:
                break;
            case CREER_UTILISATEUR:
                this.ihm.saisirUtilisateur();
                break;
            case CREER_CONFERENCE:
                this.ihm.saisirNouvelleConference(this.genconf.getConferences().keySet());
                break;
            case CREER_TYPECOM:
                this.ihm.saisirTypeCom();
                break;
            case AFFICHER_UTILISATEUR:
                this.ihm.afficheUtilisateurs();
                break;
            case AFFICHER_CONFERENCES:
                this.ihm.afficherConferences();
                break;
            case CREER_COMMUNICATION:
                this.ihm.saisirCommunication();
                break;
            case CREER_TRACK:
                this.ihm.saisirTrack();
                break;
            case CREER_SESSION:
                this.ihm.saisirSession();
                break;
            case LIER_SESSION_A_TRACK:
                this.ihm.lierSessionATrack();
                break;
            case LIER_COM_A_SESSION:
                this.ihm.lierComASession();
                break;
            default:
                assert false : "Commande inconnue.";
        }
    }

    public void creerUtilisateur(IHM.InfosUtilisateur infos) {
        boolean nouvelUtilisateur = this.genconf.ajouteUtilisateur(
                infos.email,
                infos.nom,
                infos.prenom
        );
        if (nouvelUtilisateur) {
            this.ihm.informerUtilisateur(
                    "Nouvel·le utilisa·teur/trice : " + infos.prenom + " " + infos.nom + " <" + infos.email + ">",
                    true
            );
        } else {
            this.ihm.informerUtilisateur(
                    "L'utilisa·teur/trice " + infos.email + " est déjà connu·e de GenConf.",
                    false
            );
        }
    }

    public void creerConference(IHM.InfosNouvelleConference infos) {
        // création d'un Utilisateur si nécessaire
        boolean nouvelUtilisateur = this.genconf.ajouteUtilisateur(
                infos.admin.email,
                infos.admin.nom,
                infos.admin.prenom
        );
        if (nouvelUtilisateur) {
            this.ihm.informerUtilisateur("Nouvel·le utilisa·teur/trice : " + infos.admin.prenom + " " + infos.admin.nom + " <" + infos.admin.email + ">",
                    true
            );
        }

        this.genconf.nouvelleConference(
                infos.nom,
                infos.dateDebut,
                infos.dateFin,
                infos.admin.email
        );
        this.ihm.informerUtilisateur(
                "Nouvelle conférence : " + infos.nom + ", administrée par " + infos.admin.email,
                true
        );
    }

    /**
     *
     * Partie d'ajout des méthodes experimentales de IHM
     *
     * @return 
     */

    public Map<String, Utilisateur> getMapUtilisateurs()
    {
        return this.genconf.getUtilisateurs();
    }
    
    public Map<String,Conference> getMapConferences()
    {
        return this.genconf.getConferences();
    }
    
    // fonction typeCom
    public void creerTypeCom(String nom, Conference conference)
    {
        String choix;
        Scanner input = new Scanner(System.in);
        TypeCommunication typeCommunication = new TypeCommunication(nom);

        System.out.println("Voulez-vous un pdf ? (o/n)");
        choix = input.nextLine();
        choix = verifChoixInput(choix);
        if (choix.equals("o")) typeCommunication.setPdf(true);

        System.out.println("Voulez-vous une video ? (o/n)");
        choix = input.nextLine();
        choix = verifChoixInput(choix);
        if (choix.equals("o")) typeCommunication.setVideo(true);

        System.out.println("Voulez-vous un lien de video-conférence ? (o/n)");
        choix = input.nextLine();
        choix = verifChoixInput(choix);
        if (choix.equals("o")) typeCommunication.setLienVideoConf(true);

        conference.addTypeCom(nom, typeCommunication);
    }

    public String verifChoixInput(String choix)
    {
        Scanner input = new Scanner(System.in);
        while (!choix.equals("o") && !choix.equals("n"))
        {
            System.out.println("Choix invalide, ressaisir votre choix :");
            choix = input.nextLine();
        }
        return choix;
    }


    // fonction track
    public void creerTrack(String libelle, String couleur, Conference conference)
    {
        Track track = new Track(libelle, couleur);
        if (!conference.existTrack(libelle))
        {
            conference.addTrack(track);
            this.ihm.informerUtilisateur("Le track a bien été ajouté", true);
        }
        else
        {
            this.ihm.informerUtilisateur("Erreur, ce libellé de track existe déjà.", false);
        }
    }

    // fonction session
    public void creerSession(String nomSession, LocalDate jour, int heureDebut, int heureFin, String salle, Conference conference)
    {
        Session session = new Session(nomSession, salle, jour, heureDebut, heureFin);
        conference.addSession(session);
    }

    // lier session a track
    public void lierSessionATrack(Session session, Track track)
    {
        session.addTrack(track);
    }

    // fonction communication
    public void creerCommunication(Integer numCom, String titre, String auteurs, Conference conference)
    {
        Communication communication = new Communication(numCom, titre, auteurs);
        conference.addCom(communication);
    }

    // lier com a session
    public void lierComASession(Session session, Communication communication)
    {
        session.addCom(communication);
    }
}
