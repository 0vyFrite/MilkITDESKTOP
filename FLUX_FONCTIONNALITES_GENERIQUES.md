# Flux des fonctionnalités généralisées

Ce fichier décrit seulement la partie généralisée de l'interface : le formulaire générique et la liste générique.
L'objectif est d'expliquer le comportement du code du point de vue de l'utilisateur, étape par étape.

## 1. Entrée dans l'interface principale

Quand l'utilisateur arrive dans [Application.java](src/java/Interface/Application.java), il voit le menu latéral de l'application.

Dans ce menu, deux boutons concernent la partie généralisée :

- `Formulaire généralisé`
- `Liste généralisé`

Ces boutons ne contiennent pas la logique métier directement.
Ils servent seulement à ouvrir les vues réutilisables qui ont été conçues pour fonctionner avec différents objets.

## 2. Ce qui se passe quand l'utilisateur ouvre le formulaire généralisé

Quand l'utilisateur clique sur `Formulaire généralisé`, la méthode `ouvrirFormulaireGenerique()` est appelée dans [Application.java](src/java/Interface/Application.java).

Cette méthode prépare trois choses :

- la liste des champs à afficher,
- la règle qui transforme les valeurs du formulaire en objet,
- l'action à exécuter quand la saisie est validée.

Dans le cas actuel, le formulaire est construit pour un objet `Utilisateur`.

Les champs demandés sont :

- `email`
- `motDePasse`
- `role`

Le mot de passe est affiché comme champ secret, donc l'utilisateur ne voit pas ce qu'il tape.

## 3. Comment le formulaire est construit

La classe [FormulaireGenerique.java](src/java/Generique/FormulaireGenerique.java) reçoit la liste des champs et construit automatiquement la fenêtre.

Pour chaque champ fourni :

- un libellé est affiché à gauche,
- un champ de saisie est créé à droite,
- le champ est stylé avec le thème commun,
- le champ est enregistré dans une structure interne pour pouvoir récupérer sa valeur plus tard.

Cela veut dire que le formulaire n'est pas écrit à la main champ par champ pour chaque objet.
Il est généré à partir de la description des champs.

## 4. Saisie et validation du formulaire

L'utilisateur remplit les champs puis clique sur `Valider`.

À ce moment-là, [FormulaireGenerique.java](src/java/Generique/FormulaireGenerique.java) exécute le flux suivant :

- il lit toutes les valeurs saisies,
- il les range dans une carte `Map<String, String>`,
- il envoie cette carte à la fonction de conversion,
- il récupère l'objet créé.

Si la conversion échoue ou renvoie `null`, une boîte de dialogue d'erreur est affichée.
Le formulaire reste alors ouvert pour laisser l'utilisateur corriger sa saisie.

Si la conversion réussit, l'action de validation est exécutée, puis la fenêtre se ferme.

## 5. Transformation des inputs en objet

La transformation est définie dans [Application.java](src/java/Interface/Application.java).

Pour le formulaire généralisé actuel, la carte de valeurs est convertie en `Utilisateur`.

Le comportement est simple :

- `email` devient l'email de l'objet,
- `motDePasse` devient le mot de passe de l'objet,
- `role` devient le rôle de l'utilisateur,
- si `role` est vide, la valeur `user` est utilisée par défaut.

Donc le formulaire ne sert pas juste à afficher des champs.
Il fabrique un objet concret à partir des données saisies.

## 6. Ce qui se passe après la validation

Une fois l'objet créé, [Application.java](src/java/Interface/Application.java) exécute l'action après validation.

Dans l'état actuel du code :

- l'objet est ajouté à la liste en mémoire `utilisateursGeneriques`,
- un message de succès s'affiche,
- le formulaire se ferme.

Le résultat est visible immédiatement dans la session courante.
L'utilisateur n'a pas besoin de relancer l'application.

## 7. Ce qui se passe quand l'utilisateur ouvre la liste généralisée

Quand l'utilisateur clique sur `Liste généralisé`, la méthode `ouvrirTableauGenerique()` est appelée dans [Application.java](src/java/Interface/Application.java).

Cette méthode prépare :

- les colonnes du tableau,
- la liste des objets à afficher.

Dans le cas actuel, le tableau affiche les objets `Utilisateur` présents dans `utilisateursGeneriques`.

Les colonnes définies sont :

- `Email`
- `Rôle`

## 8. Comment le tableau est construit

La classe [TableauGenerique.java](src/java/Generique/TableauGenerique.java) reçoit la liste des colonnes et la liste des objets.

Pour chaque colonne, elle connaît :

- le titre à afficher,
- la fonction qui permet d'extraire la bonne valeur depuis un objet.

Ensuite, elle remplit automatiquement le tableau ligne par ligne.

Cela permet d'afficher différentes listes d'objets sans réécrire un nouveau tableau pour chaque type.

## 9. Navigation de l'utilisateur dans la liste

Quand la fenêtre du tableau s'ouvre :

- le tableau apparaît dans une nouvelle fenêtre,
- les données sont déjà remplies,
- l'utilisateur peut simplement consulter la liste.

Le tableau est en lecture seule.
L'objectif ici est l'affichage, pas l'édition.

## 10. Design commun

Le style visuel partagé est centralisé dans [ThemeGenerique.java](src/java/Generique/ThemeGenerique.java).

Il sert à garder la même logique visuelle sur les vues généralisées :

- fond sombre,
- texte clair,
- champs lisibles,
- boutons arrondis,
- tableau cohérent avec le reste de l'application.

## 11. Résumé du flux complet

### Pour le formulaire généralisé

1. L'utilisateur clique sur `Formulaire généralisé`.
2. L'application ouvre [FormulaireGenerique.java](src/java/Generique/FormulaireGenerique.java).
3. Les champs définis dans [Application.java](src/java/Interface/Application.java) sont affichés.
4. L'utilisateur remplit les champs.
5. Le formulaire récupère les valeurs.
6. Les valeurs sont transformées en objet.
7. Si tout est correct, l'objet est ajouté à la liste et la fenêtre se ferme.

### Pour la liste généralisée

1. L'utilisateur clique sur `Liste généralisé`.
2. L'application ouvre [TableauGenerique.java](src/java/Generique/TableauGenerique.java).
3. Les colonnes définies dans [Application.java](src/java/Interface/Application.java) sont utilisées.
4. Le tableau affiche les objets présents dans la liste.

## 12. Idée importante à retenir

La partie généralisée fonctionne comme un moteur réutilisable :

- on décrit les champs,
- on décrit la conversion en objet,
- on décrit les colonnes à afficher.

Le même code peut donc servir pour plusieurs objets différents sans réécrire toute l'interface.
