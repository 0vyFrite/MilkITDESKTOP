# Explication des ajouts génériques

Ce fichier explique comment les ajouts récents répondent aux 3 demandes visibles dans l’image.

## 1. Formulaire généralisé : créer un formulaire générique

La classe [src/java/Generique/FormulaireGenerique.java](src/java/Generique/FormulaireGenerique.java) sert de base commune pour créer n’importe quel formulaire.

Elle reçoit :

- une liste de champs à afficher,
- un titre de fenêtre,
- une fonction de conversion qui transforme les valeurs saisies en objet,
- une action à exécuter quand le formulaire est validé.

Le résultat est un formulaire réutilisable, sans recopier l’interface à chaque fois.

## 2. Formulaire généralisé : transformer les inputs en objet

Toujours dans [src/java/Generique/FormulaireGenerique.java](src/java/Generique/FormulaireGenerique.java), les valeurs saisies sont récupérées dans une `Map<String, String>`.

Ensuite, la fonction de conversion transforme cette map en objet métier.

Exemple dans l’application :

- les champs `email`, `motDePasse` et `role` sont lus,
- puis un objet `Utilisateur` est créé,
- enfin cet objet est ajouté à la liste en mémoire.

Donc le formulaire ne fait pas juste afficher des champs : il fabrique directement l’objet demandé.

## 3. Liste généralisé : créer un tableau générique pour des listes d’objets

La classe [src/java/Generique/TableauGenerique.java](src/java/Generique/TableauGenerique.java) sert à afficher une liste d’objets dans un tableau Swing.

Elle fonctionne avec :

- une liste de colonnes décrites par [src/java/Generique/ColonneTableau.java](src/java/Generique/ColonneTableau.java),
- une liste d’objets à afficher,
- une fonction pour extraire chaque valeur de colonne depuis un objet.

Résultat : le même tableau peut afficher n’importe quelle liste d’objets, tant qu’on décrit ses colonnes.

## Design commun

Le style visuel est centralisé dans [src/java/Generique/ThemeGenerique.java](src/java/Generique/ThemeGenerique.java).

Ça permet de garder :

- le fond sombre,
- les champs de saisie stylés,
- les boutons arrondis,
- et le tableau cohérent avec le reste de l’interface.

## Intégration dans l’application

La classe [src/java/Interface/Application.java](src/java/Interface/Application.java) ouvre :

- le formulaire généralisé,
- la liste généralisée.

Donc les deux fonctionnalités sont réellement branchées dans l’interface principale et pas seulement créées à part.

## Résumé simple

- `FormulaireGenerique` = créer un formulaire réutilisable.
- `FormulaireGenerique` + fonction de conversion = transformer les inputs en objet.
- `TableauGenerique` = afficher une liste d’objets dans un tableau générique.
