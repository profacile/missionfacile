package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class CandidatureRepositoryTest {
    @Test
    void refuseUneDeuxiemeCandidatureDuMemeConsultantPourLaMemeOffre() throws Exception {
        MissionContract.verifierCandidature();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Entreprise.class, Gestionnaire.class,
                Offre.class, Competence.class, Consultant.class, Candidature.class)) {
            var repository = base.creerRepository(CandidatureRepository.class);
            base.entityManager.getTransaction().begin();
            Object premiere = MissionFixtures.candidature(base.entityManager);
            repository.saveAndFlush(premiere);
            Object doublon = nouveau(Candidature.class, Map.of(
                "message", "Deuxième candidature fictive",
                "dateCandidature", java.time.LocalDateTime.of(2026, 11, 20, 11, 0),
                "statut", Enum.valueOf(StatutCandidature.class, "RECUE"),
                "offre", lire(premiere, "offre"), "consultant", lire(premiere, "consultant")));
            assertThrows(RuntimeException.class, () -> repository.saveAndFlush(doublon));
        }
    }

    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierCandidature();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierCandidature();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Entreprise.class, Gestionnaire.class, Offre.class, Competence.class, Consultant.class, Candidature.class)) {
            var repository = base.creerRepository(CandidatureRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.candidature(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "message"), lire(recharge, "message"));
            assertEquals(lire(objet, "dateCandidature"), lire(recharge, "dateCandidature"));
            assertEquals(lire(objet, "statut"), lire(recharge, "statut"));
Long offreId = (Long) lire(lire(objet, "offre"), "id");
            List<?> resultat = (List<?>) rechercher(repository, "findByOffreId", Long.class, offreId);
            assertEquals(1, resultat.size());
            assertEquals(id, lire(resultat.getFirst(), "id"));
            assertTrue(((List<?>) rechercher(repository, "findByOffreId", Long.class, Long.MAX_VALUE)).isEmpty());
            base.entityManager.getTransaction().commit();
        }
    }
}
