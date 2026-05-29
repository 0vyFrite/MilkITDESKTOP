package src.java.Generique;

import java.util.function.Function;

/**
 * Décrit une colonne du tableau générique.
 */
public class ColonneTableau<T> {
    private final String titre;
    private final Function<T, Object> extracteur;

    public ColonneTableau(String titre, Function<T, Object> extracteur) {
        this.titre = titre;
        this.extracteur = extracteur;
    }

    public String getTitre() {
        return titre;
    }

    public Function<T, Object> getExtracteur() {
        return extracteur;
    }
}