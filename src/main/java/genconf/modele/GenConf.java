package genconf.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class GenConf implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final Map<String, Utilisateur> utilisateurs;  // association qualifiée par l'email
    private final Map<String, Conference> conferences;  // association qualifiée par le nom

    public GenConf() {
        this.utilisateurs = new HashMap<>();
        this.conferences = new HashMap<>();
    }

    public boolean ajouteUtilisateur(String email, String nom, String prenom) {
        if (this.utilisateurs.containsKey(email)) {
            return false;
        } else {
            this.utilisateurs.put(email, new Utilisateur(email, nom, prenom));
            return true;
        }
    }

    public Map<String, Conference> getConferences() {
        return this.conferences;
    }
    
    public Map<String, Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public void nouvelleConference(String nom, LocalDate dateDebut, LocalDate dateFin, String adminEmail) {
        assert !this.conferences.containsKey(nom);
        assert this.utilisateurs.containsKey(adminEmail);
        Utilisateur admin = this.utilisateurs.get(adminEmail);
        Conference conf = Conference.initialiseConference(this, nom, dateDebut, dateFin, admin);
        this.conferences.put(nom, conf);
    }


    // fonction type de com
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

}
