# Sprint 2 — Modèle JPA et repositories MissionFacile

## Objectif et répartition

Compléter les classes et interfaces vides à partir du diagramme de classes fourni. Les tests sont déjà écrits et s'exécutent dans IntelliJ avec le bouton Play.

| Étudiant | Classes et énumérations | Suites de tests |
| --- | --- | --- |
| Carine | Utilisateur, Entreprise, Gestionnaire | UtilisateurRepositoryTest, EntrepriseRepositoryTest, GestionnaireRepositoryTest |
| Amandine | Offre, Competence, StatutOffre | OffreRepositoryTest, CompetenceRepositoryTest |
| Bernard | Consultant, Candidature, StatutCandidature | ConsultantRepositoryTest, CandidatureRepositoryTest |

Durée prévue d'une semaine. L'étudiant crée sa branche feature/<prenom>-jpa depuis develop, complète son code, exécute ses tests et ouvre une pull request vers develop. Le dirigeant relit avant fusion. main et develop contiennent initialement la même base.

## Règles communes du mapping

Les classes sont dans domain/model, les repositories dans domain/repository, les services dans domain/service. Les DTO request et response sont réservés au module rest.

Chaque classe persistante reçoit @Entity et @Table avec le nom SQL indiqué. Les champs sont privés. Les accesseurs publics suivent getChamp et setChamp. Prévoir un constructeur sans argument public ou protégé pour JPA.

Utilisateur est abstrait et reçoit @Inheritance(strategy = InheritanceType.JOINED). Entreprise, Consultant et Gestionnaire héritent de Utilisateur. L'identifiant id de type Long, @Id, @GeneratedValue(strategy = GenerationType.IDENTITY) est déclaré une seule fois dans Utilisateur et hérité par ces trois classes. Offre, Competence et Candidature possèdent leur propre id Long généré de la même manière. Fournir getId().

Les champs simples reçoivent @Column(name = "...", nullable = false). Les colonnes uniques sont indiquées dans les tableaux. Le champ motDePasse contient une valeur de hachage fictive dans les tests. L'authentification et le hachage réel feront l'objet d'un exercice de sécurité distinct.

## Carine — Utilisateur

Table SQL `utilisateur`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| email | String | email | Unique |
| motDePasse | String | mot_de_passe | Obligatoire |
| dateInscription | LocalDateTime | date_inscription | Obligatoire |

Compléter `UtilisateurRepository` avec extends JpaRepository<Utilisateur, Long> et la méthode suivante.

```java
Optional<Utilisateur> findByEmail(String email);
```

La suite `UtilisateurRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Carine — Entreprise

Table SQL `entreprise`, héritage de `Utilisateur`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| nom | String | nom | Obligatoire |
| numeroTva | String | numero_tva | Unique |
| secteur | String | secteur | Obligatoire |

Compléter `EntrepriseRepository` avec extends JpaRepository<Entreprise, Long> et la méthode suivante.

```java
Optional<Entreprise> findByNumeroTva(String numeroTva);
```

La suite `EntrepriseRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Carine — Gestionnaire

Table SQL `gestionnaire`, héritage de `Utilisateur`.

Cette entité n'ajoute aucun champ au parent pour ce sprint. Elle représente l'acteur qui pourra valider les offres. Ses propriétés communes proviennent de Utilisateur.

Compléter `GestionnaireRepository` avec extends JpaRepository<Gestionnaire, Long> et la méthode suivante.

```java
Optional<Gestionnaire> findByEmail(String email);
```

La suite `GestionnaireRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Amandine — Competence

Table SQL `competence`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| libelle | String | libelle | Unique |

Compléter `CompetenceRepository` avec extends JpaRepository<Competence, Long> et la méthode suivante.

```java
Optional<Competence> findByLibelleIgnoreCase(String libelle);
```

La suite `CompetenceRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Amandine — Offre

Table SQL `offre`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| titre | String | titre | Obligatoire |
| description | String | description | Obligatoire |
| lieu | String | lieu | Obligatoire |
| dureeJours | int | duree_jours | Obligatoire |
| tjmMax | BigDecimal | tjm_max | Obligatoire |
| statut | StatutOffre | statut | Obligatoire |

Relation `entreprise` de type `Entreprise`, avec @ManyToOne(optional = false) et @JoinColumn(name = "entreprise_id", nullable = false). La relation est obligatoire.

Relation `gestionnaire` de type `Gestionnaire`, avec @ManyToOne(optional = true) et @JoinColumn(name = "gestionnaire_id", nullable = true). Le gestionnaire peut être absent tant que l'offre n'a pas été traitée.

Collection `competences` de type Set<Competence>, annotée @ManyToMany et @JoinTable(name = "offre_competence", joinColumns = @JoinColumn(name = "offre_id"), inverseJoinColumns = @JoinColumn(name = "competence_id")). Fournir getter et setter, et initialiser la collection vide pour les nouveaux objets.

Compléter StatutOffre avec BROUILLON, EN_VALIDATION, PUBLIEE, POURVUE, CLOTUREE, dans cet ordre. Mapper statut avec @Enumerated(EnumType.STRING). Le champ dureeJours est de type int, conformément au diagramme. Les règles de publication feront l'objet des services.

Compléter `OffreRepository` avec extends JpaRepository<Offre, Long> et la méthode suivante.

```java
List<Offre> findByEntrepriseId(Long entrepriseId);
```

La suite `OffreRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Bernard — Consultant

Table SQL `consultant`, héritage de `Utilisateur`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| nom | String | nom | Obligatoire |
| prenom | String | prenom | Obligatoire |
| tjm | BigDecimal | tjm | Obligatoire |
| disponibleLe | LocalDate | disponible_le | Obligatoire |

Collection `competences` de type Set<Competence>, annotée @ManyToMany et @JoinTable(name = "consultant_competence", joinColumns = @JoinColumn(name = "consultant_id"), inverseJoinColumns = @JoinColumn(name = "competence_id")). Fournir getter et setter, et initialiser la collection vide pour les nouveaux objets.

Compléter `ConsultantRepository` avec extends JpaRepository<Consultant, Long> et la méthode suivante.

```java
Optional<Consultant> findByEmail(String email);
```

La suite `ConsultantRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives.

## Bernard — Candidature

Table SQL `candidature`.

| Champ | Type Java | Colonne SQL | Particularité |
| --- | --- | --- | --- |
| message | String | message | Obligatoire |
| dateCandidature | LocalDateTime | date_candidature | Obligatoire |
| statut | StatutCandidature | statut | Obligatoire |

Relation `offre` de type `Offre`, avec @ManyToOne(optional = false) et @JoinColumn(name = "offre_id", nullable = false). La relation est obligatoire.

Relation `consultant` de type `Consultant`, avec @ManyToOne(optional = false) et @JoinColumn(name = "consultant_id", nullable = false). La relation est obligatoire.

Compléter StatutCandidature avec RECUE, PRESELECTIONNEE, RETENUE, REFUSEE, dans cet ordre. Mapper statut avec @Enumerated(EnumType.STRING). Ajouter sur la table candidature une @UniqueConstraint portant sur offre_id et consultant_id. Elle empêche la candidature en double, même si deux requêtes concurrentes tentent de l'insérer.

Compléter `CandidatureRepository` avec extends JpaRepository<Candidature, Long> et la méthode suivante.

```java
List<Candidature> findByOffreId(Long offreId);
```

La suite `CandidatureRepositoryTest` vérifie le mapping et le repository, puis sauvegarde, vide le contexte JPA, relit l'objet depuis H2 et exécute la recherche. Les données sont fictives. Un test supplémentaire tente de sauvegarder deux candidatures avec la même offre et le même consultant et attend un refus de la base.

## Dépendances et ordre d'intégration

Carine prépare d'abord Utilisateur et Entreprise. Ces classes sont nécessaires aux tests de persistance des offres et consultants. Gestionnaire complète ensuite le parcours de validation.

Amandine peut travailler indépendamment sur Competence, puis sur le mapping Offre. Bernard peut travailler sur le mapping Consultant et Candidature en parallèle. Leurs tests de structure s'exécutent dès le départ. Les tests de persistance nécessitent les entités associées indiquées dans chaque test.

Intégrer Utilisateur/Entreprise, puis Competence/Gestionnaire, puis Offre/Consultant et enfin Candidature dans develop. Garder les tests fournis. Une évolution du contrat nécessite la revue du dirigeant.

## Lancer les tests

```powershell
.\mvnw.cmd clean test-compile
.\mvnw.cmd -pl domain test -Dtest=EntrepriseRepositoryTest
.\mvnw.cmd verify
```

Remplacer le nom de la suite par celui de son exercice. Les classes sont vides au départ et les tests doivent être rouges avec des messages indiquant les annotations ou champs manquants. La compilation, elle, doit réussir.

Les tests utilisent une base H2 isolée et jetable. Les requêtes PostgreSQL, migrations et contrôles sur PostgreSQL seront vérifiés lors du sprint de persistance de production. Les services et les DTO restent des étapes suivantes.

## Cohérence de l'analyse

Le texte UC1 prévoit la validation du gestionnaire avant publication. Le diagramme de séquence devra montrer cette étape avant la notification aux consultants. Ce sprint mappe les états et relations, sans implémenter ces notifications.

Le retour d'une offre refusée vers BROUILLON est représenté dans le diagramme d'états. Le motif de refus reste à définir. Le refus automatique des autres candidatures lors d'une sélection reste à confirmer. Aucun automatisme correspondant n'est inclus dans les exercices JPA.

Les langues, le CV, la séniorité et la date de début figurent dans les scénarios mais pas dans les champs du diagramme fourni. Ils feront l'objet d'une évolution explicitée du modèle avant les exercices concernés.

## Livraison

Chaque étudiant fournit ses entités et repositories, les tests ciblés verts après intégration des prérequis, une pull request vers develop et une explication d'un test qui échouait puis passe grâce à sa correction. Les types et noms ci-dessus constituent le contrat pédagogique adapté du diagramme fourni.
