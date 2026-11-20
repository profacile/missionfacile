# MissionFacile

Base pédagogique PROFACILE SRL pour Carine, Amandine et Bernard. Java 25, Spring Boot 4.1.2 et Maven Wrapper 3.9.16.

## Structure

```text
domain/
  model/           entités et énumérations vides à compléter
  repository/      interfaces Spring Data vides à compléter
  service/         services préparés pour les exercices suivants
  src/test/java/   RepositoryTest et infrastructure de test fournie
rest/
  dto/request/     futurs DTO d'entrée
  dto/response/    futurs DTO de sortie
bootstrap/         démarrage Spring Boot fourni
docs/sprint-2.md    consignes par étudiant et contrat des champs
```

Les packages Java sont sous be.profacile.missionfacile. L'organisation suit la base JPA validée pour PharmaShare. Le module bootstrap dépend de rest, qui dépend de domain.

## Travail en équipe

main conserve la base fournie. develop démarre sur le même commit. Chaque étudiant crée feature/<prenom>-jpa depuis develop puis ouvre sa pull request vers develop. Le dirigeant relit les contributions avant fusion.

- Carine complète Utilisateur, Entreprise, Gestionnaire et leurs repositories.
- Amandine complète Offre, Competence, StatutOffre et les repositories associés.
- Bernard complète Consultant, Candidature, StatutCandidature et les repositories associés.

Consulter [les consignes détaillées](docs/sprint-2.md) avant de coder. Les tests compilent même lorsque les classes sont vides. Ils inspectent les champs et annotations à l'exécution, puis utilisent Hibernate, Spring Data JPA et H2 pour vérifier la sauvegarde, la relecture et les recherches.

## Démarrer

Installer un JDK 25, définir JAVA_HOME et importer le pom.xml racine dans IntelliJ.

```powershell
.\mvnw.cmd --version
.\mvnw.cmd clean test-compile
.\mvnw.cmd -pl domain test -Dtest=EntrepriseRepositoryTest
.\mvnw.cmd verify
```

Sous Linux et macOS, utiliser sh mvnw. Le bouton Play sur les RepositoryTest est également utilisable.

La compilation doit réussir. Les tests pédagogiques sont initialement rouges, puisque les champs et repositories sont à remplir. La CI distingue « Squelette technique », qui doit passer, des « Exercices métier », qui deviennent verts au fur et à mesure des livraisons. Les rapports JUnit sont conservés sept jours.

Les tests techniques seuls se lancent avec

```powershell
.\mvnw.cmd -pl bootstrap -am test -Dtest=MissionFacileApplicationTest "-Dsurefire.failIfNoSpecifiedTests=false"
```

Le bootstrap est configuré sans serveur HTTP pour ce sprint. L'ancien backend consulting-service a servi de référence et reste dans son dossier historique. Ce dépôt contient la base pédagogique issue des diagrammes transmis le 20 novembre 2026.

Les fichiers de tests et de configuration désignent profacile dans CODEOWNERS. Une protection bloquante des branches est à configurer dans les paramètres GitHub.

## Périmètre

Cette semaine porte sur les entités JPA, l'héritage JOINED, les associations, les énumérations et les repositories. La logique des services, les contrôleurs, DTO, emails, authentification et modifications automatiques de statuts seront développés dans les sprints suivants. Les données et identifiants de test sont fictifs.
