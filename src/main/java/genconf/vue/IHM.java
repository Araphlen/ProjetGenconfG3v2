package genconf.vue;

import genconf.controleur.Commande;
import genconf.controleur.Controleur;
import genconf.modele.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import org.apache.commons.validator.routines.EmailValidator;


/**
 * La classe CLI est responsable des interactions avec l'utilisa·teur/trice en
 * mode texte.
 * <p>
 * C'est une classe qui n'est associée à aucun état : elle ne contient aucun
 * attribut d'instance.
 * <p>
 * Aucun méthode de cette classe n'est pas censée modifier ses paramètres,
 * c'est pourquoi les paramètres des méthodes sont tous marqués comme `final`.
 *
 */
public class IHM  {

    /**
     * Nombre maximum d'essais pour la lecture d'une saisie utilisa·teur/trice.
     */
    private static final int MAX_ESSAIS = 3;
    private final Controleur controleur;

    public IHM(Controleur controleur) {
        this.controleur = controleur;
    }

    /** Classes conteneurs
    
     * Classe conteneur pour les informations saisies à propos d'un
     * {@link /fr.uga.iut2.genconf.modele.Utilisateur}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosUtilisateur {
        public final String email;
        public final String nom;
        public final String prenom;

        public InfosUtilisateur(final String email, final String nom, final String prenom) {
            this.email = email;
            this.nom = nom;
            this.prenom = prenom;
        }
    }

    /**
     * Classe conteneur pour les informations saisies pour une nouvelle
     * {@link /fr.uga.iut2.genconf.modele.Conference}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosNouvelleConference {
        public final String nom;
        public final LocalDate dateDebut;
        public final LocalDate dateFin;
        public final InfosUtilisateur admin;

        public InfosNouvelleConference(final String nom, final LocalDate dateDebut, final LocalDate dateFin, final InfosUtilisateur admin) {
            assert !dateDebut.isAfter(dateFin);
            this.nom = nom;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.admin = admin;
        }
    }
    
//    public static class InfosNouvelleCommuniaction{
//        public final int numComm;
//        final String titre;
//        public final InfosUtilisateur correspondant;
//        public InfosNouvelleCommuniaction(final int numComm, final String titre, final InfosUtilisateur correspondant){
//
//        }
//
//    }

   
//-----  Éléments du dialogue  -------------------------------------------------

    private Commande dialogueSaisirCommande() {
        IHM.afficher("===== GenConf: Générateur Site Conférence =====");
        IHM.afficher(IHM.synopsisCommandes());
        IHM.afficher("===============================================");
        IHM.afficher("Saisir l'identifiant de l'action choisie :");
        return IHM.lireAvecErreurs(IHM::parseCommande);
    }

    private InfosUtilisateur dialogueSaisirUtilisateur() {
        String email, nom, prenom;

        IHM.afficher("== Saisie d'un·e utilisa·teur/trice ==");
        email = IHM.lireEmail();
        IHM.afficher("Saisir le nom :");
        nom = IHM.lireNom();
        IHM.afficher("Saisir le prénom :");
        prenom = IHM.lireNom();

        return new InfosUtilisateur(email, nom, prenom);
    }

    private InfosNouvelleConference dialogueSaisirNouvelleConference(final Set<String> nomsExistants) {
        String nom;
        LocalDate dateDebut, dateFin;
        InfosUtilisateur admin;

        IHM.afficher("== Saisie d'une nouvelle conférence ==");
        IHM.afficher("Saisir le nom de la conférence :");
        nom = IHM.lireNom(nomsExistants, true);
        IHM.afficher("Date de début: ");
        dateDebut = IHM.lireDate();
        IHM.afficher("Date de fin: ");
        dateFin = IHM.lireDate(dateDebut);

        IHM.afficher("Saisir les informations à propos de l'administra·teur/trice de la conférence.");
        IHM.afficher("Un·e nouvel·lle utilisa·teur/trice sera créé·e si nécessaire.");
        admin = this.dialogueSaisirUtilisateur();

        return new InfosNouvelleConference(nom, dateDebut, dateFin, admin);
    }
    
//    private InfosNouvelleCommuniaction dialogueSaisirNouvelleCommunication(){
//
//        return new InfosNouvelleCommuniaction();
//
//    }

//-----  Implémentation des méthodes pubiques appelées par le controleur  -------------------------------

  
    public void afficherInterface() {
        Commande cmd;
        do {
            cmd = dialogueSaisirCommande();
            controleur.gererDialogue(cmd);
        } while (cmd != Commande.QUITTER);
    }


    public void informerUtilisateur(final String msg, final boolean succes) {
        IHM.afficher((succes ? "[OK]" : "[KO]") + " " + msg);
    }

    public void saisirUtilisateur() {
        InfosUtilisateur infos = dialogueSaisirUtilisateur();
        controleur.creerUtilisateur(infos);
    }
    
    public void saisirNouvelleConference(final Set<String> nomsExistants) {
        InfosNouvelleConference infos = dialogueSaisirNouvelleConference(nomsExistants);
        controleur.creerConference(infos);
    }
   

//-----  Primitives d'affichage  -----------------------------------------------

    /**
     * Construit le synopsis des commandes.
     *
     * @return Une chaîne de caractères contenant le synopsis des commandes.
     */
    private static String synopsisCommandes() {
        StringBuilder builder = new StringBuilder();

        for (Commande cmd: Commande.values()) {
            builder.append("  ");  // légère indentation
            builder.append(cmd.synopsis());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    /**
     * Affiche un message à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     */
    private static void afficher(final String msg) {
        System.out.println(msg);
        System.out.flush();
    }

//-----  Primitives de lecture  ------------------------------------------------

    /**
     * Essaie de lire l'entrée standard avec la fonction d'interprétation.
     * <p>
     * En cas d'erreur, la fonction essaie au plus {@value #MAX_ESSAIS} fois de
     * lire l'entrée standard.
     *
     * @param <T> Le type de l'élément lu une fois interprété.
     *
     * @param parseFunction La fonction d'interprétation: elle transforme un
     *     token de type chaîne de caractère un un objet de type T.
     *     <p>
     *     La fonction doit renvoyer l'option vide en cas d'erreur, et une
     *     option contenant l'objet interprété en cas de succès.
     *     <p>
     *     La fonction d'interprétation est responsable d'afficher les messages
     *     d'erreur et de guidage utilisa·teur/trice.
     *
     * @return L'interprétation de la lecture de l'entrée standard.
     */
    private static <T> T lireAvecErreurs(final Function<String, Optional<T>> parseFunction) {
        Optional<T> result = Optional.empty();
        Scanner in = new Scanner(System.in);
        String token;

        for (int i = 0; i < IHM.MAX_ESSAIS && result.isEmpty(); ++i) {
            token = in.next();
            result = parseFunction.apply(token);
        }
//le return : si result!= null (ie: si le prog a bien lu qq chose) la fonctoin return la valeur lu dans uns string ?
//            sinon la fonction crée une erreur.
        return result.orElseThrow(() -> new Error("Erreur de lecture (" + IHM.MAX_ESSAIS + " essais infructueux)."));
    }

    /**
     * Interprète un token entier non signé comme une {@link Commande}.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @return Une option contenant la {@link Commande} en cas de succès,
     *     l'option vide en cas d'erreur.
     */
    private static Optional<Commande> parseCommande(final String token) {
        Optional<Commande> result;
        try {
            int cmdId = Integer.parseUnsignedInt(token);  // may throw NumberFormatException
            Commande cmd = Commande.valueOfCode(cmdId);  // may throw IllegalArgumentException
            result = Optional.of(cmd);
        }
        // NumberFormatException est une sous-classe de IllegalArgumentException
        catch (IllegalArgumentException ignored) {
            IHM.afficher("Choix non valide : merci d'entrer un identifiant existant.");
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Interprète un token comme une chaîne de caractère et vérifie que la
     * chaîne n'existe pas déjà.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param nomsConnus L'ensemble de chaîne de caractères qui ne sont plus
     *     disponibles.
     *
     * @return Une option contenant la chaîne de caractère en cas de succès,
     *     l'option vide en cas d'erreur.
     */
    private static Optional<String> parseNouveauNom(final String token, final Set<String> nomsConnus) {
        Optional<String> result;
        if (nomsConnus.contains(token)) {
            IHM.afficher("Le nom existe déjà dans l'application.");
            result = Optional.empty();
        } else {
            result = Optional.of(token);
        }
        return result;
    }

    /**
     * Interprète un token comme une chaîne de caractère et vérifie que la
     * chaîne existe déjà.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param nomsConnus L'ensemble de chaîne de caractères valides.
     *
     * @return Une option contenant la chaîne de caractère en cas de succès,
     *     l'option vide en cas d'erreur.
     */
    private static Optional<String> parseNomExistant(final String token, final Set<String> nomsConnus) {
        Optional<String> result;
        if (nomsConnus.contains(token)) {
            result = Optional.of(token);
        } else {
            IHM.afficher("Le nom n'existe pas dans l'application.");
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Lit sur l'entrée standard un nom en fonction des noms connus.
     *
     * @param nomsConnus L'ensemble des noms connus dans l'application.
     *
     * @param nouveau Le nom lu doit-il être un nom connu ou non.
     *     Si {@code true}, le nom lu ne doit pas exister dans
     *     {@code nomConnus}; sinon le nom lu doit exister dans
     *     {@code nomsConnus}.
     *
     * @return Le nom saisi par l'utilisa·teur/trice.
     */
    private static String lireNom(final Set<String> nomsConnus, final boolean nouveau) {
        if (nouveau) {
            if (!nomsConnus.isEmpty()) {
                IHM.afficher("Les noms suivants ne sont plus disponibles :");
                IHM.afficher("  " + String.join(", ", nomsConnus) + ".");
            }
            return IHM.lireAvecErreurs(token -> IHM.parseNouveauNom(token, nomsConnus));
        } else {
            assert !nomsConnus.isEmpty();
            IHM.afficher("Choisir un nom parmi les noms suivants :");
            IHM.afficher("  " + String.join(", ", nomsConnus) + ".");
            return IHM.lireAvecErreurs(token -> IHM.parseNomExistant(token, nomsConnus));
        }
    }

    /**
     * Lit sur l'entrée standard un nom.
     *
     * @return Le nom saisi par l'utilisa·teur/trice.
     */
    private static String lireNom() {
        return IHM.lireNom(Collections.EMPTY_SET, true);
    }

    /**
     * Interprète un token comme une chaîne de caractère et vérifie que la
     * chaîne est une adresse mél valide.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @return Une option contenant la chaîne de caractère si token est une
     *     adresse mél valide, l'option vide en cas d'erreur.
     */
    private static Optional<String> parseEmail(final String token) {
        Optional<String> result;
        EmailValidator validator = EmailValidator.getInstance(false, false);
        if (validator.isValid(token)) {
            result = Optional.of(token.toLowerCase());
        } else {
            IHM.afficher("L'adresse mél n'est pas valide.");
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Lit une adresse mél.
     *
     * @return L'adresse mél saisie par l'utilisa·teur/trice.
     */
    private static String lireEmail() {
        IHM.afficher("Saisir une adresse mél :");
        return IHM.lireAvecErreurs(IHM::parseEmail);
    }

    /**
     * Interprète un token comme une {@link LocalDate}.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param apres Si l'option contient une valeur, la date lue doit être
     *     ultérieure à {@code apres}; sinon aucune contrainte n'est présente.
     *
     * @return Une option contenant la {@link LocalDate} en cas de succès,
     *     l'option vide en cas d'erreur.
     */
    private static Optional<LocalDate> parseDate(final String token, final Optional<LocalDate> apres) {
        Optional<LocalDate> result;
        try {
            LocalDate date = LocalDate.parse(token);
            if (apres.isPresent() && apres.get().isAfter(date)) {
                // `apres.get()` est garanti de fonctionner grâce à la garde `apres.isPresent()`
                IHM.afficher("La date saisie n'est pas ultérieure à " + apres.get().toString());
                result = Optional.empty();
            } else {
                result = Optional.of(date);
            }
        }
        catch (DateTimeParseException ignored) {
            IHM.afficher("La date saisie n'est pas valide.");
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Lit une date au format ISO-8601.
     *
     * @param apres Si l'option contient une valeur, la date lue doit être
     *     ultérieure à {@code apres}; sinon aucune contrainte n'est présente.
     *
     * @see java.time.format.DateTimeFormatter#ISO_LOCAL_DATE
     *
     * @return La date saisie par l'utilisa·teur/trice.
     */
    private static LocalDate lireDate(final Optional<LocalDate> apres) {
        IHM.afficher("Saisir une date au format ISO-8601 :");
        apres.ifPresent(date -> IHM.afficher("La date doit être ultérieure à " + date.toString())
        );
        return IHM.lireAvecErreurs(token -> IHM.parseDate(token, apres));
    }

    /**
     * Lit une date au format ISO-8601.
     * <p>
     * Alias pour {@code lireDate(Optional.empty())}.
     *
     * @return La date saisie par l'utilisa·teur/trice.
     *
     * @see #lireDate(java.util.Optional)
     */
    private static LocalDate lireDate() {
        return IHM.lireDate(Optional.empty());
    }

    /**
     * Lit une date au format ISO-8601.
     * <p>
     * Alias pour {@code lireDate(Optional.of(apres))}.
     *
     * @param apres La date saisie doit être ultérieure à {@code apres}.
     *
     * @return La date saisie par l'utilisa·teur/trice.
     *
     * @see #lireDate(java.util.Optional)
     */
    private static LocalDate lireDate(final LocalDate apres) {
        return IHM.lireDate(Optional.of(apres));
    }



    /**
    *
    * Partie d'ajout des méthodes experimentales de IHM
    *
     */
    public void afficheUtilisateurs()
    {
        Map<String, Utilisateur> utilisateurs = controleur.getMapUtilisateurs();
        Set listKeys = utilisateurs.keySet();

        System.out.println("** Affichage de la liste des utilisateurs **");
        for (Object key : listKeys) {
            Utilisateur utilisateur = utilisateurs.get(key);
            System.out.println("Nom : " + utilisateur.getNom() + ", prénom : " + utilisateur.getPrenom() + ", email : " + utilisateur.getEmail());
        }
        System.out.println("** Fin de l'affichage **\n");
    }
    
    public void afficherConferences(){
        
        Map<String, Conference> conferences =controleur.getMapConferences();
        Set listKeys = conferences.keySet();
        
        System.out.println("*** Affichage de la liste des conférences : ***");
        for (Object key : listKeys) {
            Conference conf=conferences.get(key);
            System.out.println("* Nom de la conférences : "+conf.getNom()
                    + "\n* Date de début : "+conf.getDateDebut()
                    + "\n* Date de fin : "+conf.getDateFin()
                    +"\n* Dont les admins sont  : "
            );
            Collection<Utilisateur> admins=conf.getAdmin();
            for (Utilisateur admin : admins) {
                System.out.println("\t- " + admin.getNom()
                         +" "+ admin.getPrenom()
                        + ", email : " + admin.getEmail()
                );
            }
            System.out.println("* Dont les type de communications sont : ");
            Map<String, TypeCommunication> typesCom = conf.getTypesCom();
            Set<String> nomsTypeCom = typesCom.keySet();
            for (String nomTypeCom: nomsTypeCom)
            {
                System.out.println("\t- " + nomTypeCom);
            }
            System.out.println("===============================================");
        }
        System.out.println("** Fin de l'affichage **\n");

    }
    
    // fonction typeCom
    public void saisirTypeCom()
    {
        Scanner input = new Scanner(System.in);
        String nom, nomConf;
        // Demander de choisir une conference dans lmaquelle ajouter le type de com
        Map<String, Conference> conferences = controleur.getMapConferences();
        Set<String> listKeys = conferences.keySet();
        if (listKeys.size() > 0)
        {
            System.out.println("*** Liste des conférences ***");
            for (String key: listKeys)
            {
                System.out.println("\t- " + key);
            }
            System.out.println("Saisir le nom de la conférence dans laquelle ajouter le type de communication :");
            nomConf = input.nextLine();

            while (!conferences.containsKey(nomConf))
            {
                System.out.println("Nom de conférence incorrect, saisir un nom de conférence :");
                nomConf = input.nextLine();
            }

            // ajouter un type de com relié à la com choisit
            System.out.println("\nSaisir un nom de type de communication : ");
            nom = input.nextLine();
            controleur.creerTypeCom(nom, conferences.get(nomConf));
        }
        else
        {
            System.out.println("Il n'y a pas de conférence dans laquelle ajouter un type de conférence.");
        }

    }
    
    
        public void creerCommunication() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
        
        
        
}
