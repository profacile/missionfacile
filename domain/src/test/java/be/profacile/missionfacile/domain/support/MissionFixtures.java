package be.profacile.missionfacile.domain.support;

import be.profacile.missionfacile.domain.model.*;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

/** Données fictives fournies. Les prérequis sont persistés avant l'objet testé. */
public final class MissionFixtures {
    private MissionFixtures() {
    }

    private static Map<String, Object> utilisateur() {
        Map<String, Object> valeurs = new HashMap<>();
        valeurs.put("email", UUID.randomUUID() + "@example.invalid");
        valeurs.put("motDePasse", "HASH_FICTIF_NON_UTILISABLE");
        valeurs.put("dateInscription", LocalDateTime.of(2026, 11, 20, 9, 0));
        return valeurs;
    }

    public static Object entreprise(EntityManager em) throws Exception {
        Map<String, Object> valeurs = utilisateur();
        valeurs.put("nom", "Entreprise fictive");
        valeurs.put("numeroTva", "TVA-FICTIVE-" + UUID.randomUUID());
        valeurs.put("secteur", "Informatique");
        return nouveau(Entreprise.class, valeurs);
    }

    public static Object gestionnaire(EntityManager em) throws Exception {
        return nouveau(Gestionnaire.class, utilisateur());
    }

    public static Object competence(EntityManager em) throws Exception {
        return nouveau(Competence.class, Map.of("libelle", "Java"));
    }

    public static Object consultant(EntityManager em) throws Exception {
        Object competence = competence(em);
        em.persist(competence);
        Map<String, Object> valeurs = utilisateur();
        valeurs.put("nom", "Consultant fictif");
        valeurs.put("prenom", "Alex");
        valeurs.put("tjm", new BigDecimal("500.00"));
        valeurs.put("disponibleLe", LocalDate.of(2027, 1, 4));
        valeurs.put("competences", new HashSet<>(Set.of(competence)));
        return nouveau(Consultant.class, valeurs);
    }

    public static Object offre(EntityManager em) throws Exception {
        Object entreprise = entreprise(em);
        Object gestionnaire = gestionnaire(em);
        Object competence = competence(em);
        em.persist(entreprise);
        em.persist(gestionnaire);
        em.persist(competence);
        return nouveau(Offre.class, Map.of(
            "titre", "Mission Java fictive", "description", "Description fictive",
            "lieu", "Hybride", "dureeJours", 20, "tjmMax", new BigDecimal("600.00"),
            "statut", Enum.valueOf(StatutOffre.class, "EN_VALIDATION"),
            "entreprise", entreprise, "gestionnaire", gestionnaire,
            "competences", new HashSet<>(Set.of(competence))));
    }

    public static Object candidature(EntityManager em) throws Exception {
        Object offre = offre(em);
        em.persist(offre);
        // Réutiliser la compétence existante pour éviter un doublon de libellé.
        Map<String, Object> valeurs = utilisateur();
        valeurs.put("nom", "Consultant fictif");
        valeurs.put("prenom", "Alex");
        valeurs.put("tjm", new BigDecimal("500.00"));
        valeurs.put("disponibleLe", LocalDate.of(2027, 1, 4));
        valeurs.put("competences", new HashSet<>((Set<?>) lire(offre, "competences")));
        Object consultant = nouveau(Consultant.class, valeurs);
        em.persist(consultant);
        return nouveau(Candidature.class, Map.of(
            "message", "Motivation fictive",
            "dateCandidature", LocalDateTime.of(2026, 11, 20, 10, 0),
            "statut", Enum.valueOf(StatutCandidature.class, "RECUE"),
            "offre", offre, "consultant", consultant));
    }
}
