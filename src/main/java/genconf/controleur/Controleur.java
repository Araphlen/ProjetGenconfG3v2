package genconf.controleur;

import genconf.modele.Conference;
import genconf.modele.GenConf;
import genconf.modele.Utilisateur;
import genconf.vue.IHM;

import java.util.Map;

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
                this.ihm.creerCommunication();
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
        genconf.creerTypeCom(nom, conference);
    }

}
