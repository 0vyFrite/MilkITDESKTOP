package src.java.Generique;

/**
 * Décrit un champ simple utilisé par le formulaire générique.
 */
public class ChampFormulaire {
    private final String cle;
    private final String libelle;
    private final boolean secret;

    public ChampFormulaire(String cle, String libelle) {
        this(cle, libelle, false);
    }

    public ChampFormulaire(String cle, String libelle, boolean secret) {
        this.cle = cle;
        this.libelle = libelle;
        this.secret = secret;
    }

    public String getCle() {
        return cle;
    }

    public String getLibelle() {
        return libelle;
    }

    public boolean isSecret() {
        return secret;
    }
}