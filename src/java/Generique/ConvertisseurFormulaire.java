package src.java.Generique;

import java.util.Map;

/**
 * Transforme les valeurs du formulaire en objet métier.
 */
@FunctionalInterface
public interface ConvertisseurFormulaire<T> {
    T convertir(Map<String, String> valeurs);
}