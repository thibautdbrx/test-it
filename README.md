# Thibaut DEBREUX

# Test It - Prêt de livres

API REST Spring Boot (Java 25) de prêt de livres pour une bibliothèque.
**Le code est fourni : votre travail est d'écrire les tests, ce sont eux qui vous diront si tout va bien.**

## Le métier en 30 secondes

Un livre passe par 3 statuts : `DISPONIBLE` → `EMPRUNTE` → `RENDU`

| Action | Autorisée si | Mails envoyés |
|---|---|---|
| Emprunter | le livre est `DISPONIBLE` **et** le membre n'a pas déjà un livre `EMPRUNTE` | au membre |
| Rendre | le livre est `EMPRUNTE` **et** c'est ce membre qui l'a emprunté | au membre, et à son bibliothécaire référent s'il en a un |

Sinon, le service lève une exception (`IllegalArgumentException` ou `IllegalStateException`).

## Où est le code ?

```
controller/BookController     endpoints REST
service/BookService           règles métier (c'est le cœur du TD)
repository/BookRepository     accès base (JPA)
adapter/mail                  envoi de mails (fake : il écrit dans les logs)
adapter/member                membre connecté (fake : on choisit qui avec setCurrent)
```

## API

| Méthode | URL | Rôle |
|---|---|---|
| GET | `/books` | tous les livres |
| GET | `/books/{id}` | un livre (404 si absent) |
| GET | `/books/member/{memberId}` | livres d'un membre |
| POST | `/books` | créer `{title, author}`, les deux obligatoires (400 sinon) |
| PUT | `/books/{id}` | modifier (404 si absent) |
| DELETE | `/books/{id}` | supprimer |
| POST | `/books/{id}/borrow` | emprunter (400 si refusé) |
| POST | `/books/{id}/return` | rendre (400 si refusé) |

## Lancer

```
./gradlew test      # lance vos tests
./gradlew bootRun   # lance l'application sur http://localhost:8080
```

Base H2 en mémoire, console sur http://localhost:8080/h2-console

## Le TD

Écrivez les tests dans `src/test/java/com/example/testit/`, un fichier par exercice.
**Minimum attendu : 1 test par exercice.** Si vous avez du temps, ajoutez-en.

| # | Fichier | Type | Test minimum |
|---|---|---|---|
| 1 | `BookServiceTest` | unitaire, tout est mocké (Mockito), sans Spring ni base | Emprunter un livre disponible : vérifiez le statut, l'emprunteur **et les mails envoyés** |
| 2 | `BookRepositoryIT` | JPA, vraie base H2 | `findByMemberAndStatus` avec **deux membres** et plusieurs livres |
| 3 | `BookServiceIT` | contexte Spring complet, vraie base | Emprunter puis rendre un livre, et relire le résultat |
| 4 | `BookControllerIT` | MockMvc | Créer un livre puis l'emprunter, et vérifier les réponses |

Un test qui échoue n'est pas un problème : c'est **ce que vous cherchez**. Notez ce qu'il révèle (le code ou le test ?) puis corrigez.

## Bonus (si vous avez fini)

Dans l'ordre que vous voulez, un test par idée :

- **Cas d'erreur du service** : livre ou membre inconnu, livre déjà emprunté, membre qui a déjà un livre en cours.
- **Retour d'un livre** : par un autre membre que l'emprunteur, et avec ou sans référent (qui doit être prévenu ?).
- **Modifier un livre** (`PUT`) qui est déjà emprunté : que devient son statut ?
- **Supprimer** un livre qui n'existe pas.
- **Personne n'est connecté** et on emprunte un livre : que se passe-t-il ?
- **Validation** : `POST /books` avec un titre ou un auteur vide.
- **TDD** : écrivez le test d'abord, regardez-le échouer, puis corrigez le code.
- **Fake plutôt que mock** : remplacez le mock de `MailService` par un fake qui garde les mails envoyés dans une liste.

## Conseils

- Nommez vos tests comme des phrases : `refuseLEmpruntSiLeLivreEstDejaEmprunte`.
- Un test = un comportement. Structure *Given / When / Then*.
- Un test que vous n'avez jamais vu échouer ne prouve rien : cassez volontairement le service pour vérifier qu'il devient rouge.
- Ne testez pas vos mocks : testez une décision du code (un `if`, un calcul, un appel ou un non-appel).
