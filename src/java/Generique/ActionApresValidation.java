package src.java.Generique;

/**
 * Décrit l'action à exécuter après la validation du formulaire.
 */
@FunctionalInterface
public interface ActionApresValidation<T> {
    void executer(T objet);
}