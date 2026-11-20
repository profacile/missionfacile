package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class CompetenceRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierCompetence();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierCompetence();
        try (BaseJpa base = new BaseJpa(Competence.class)) {
            var repository = base.creerRepository(CompetenceRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.competence(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "libelle"), lire(recharge, "libelle"));
Optional<?> resultat = (Optional<?>) rechercher(repository, "findByLibelleIgnoreCase", String.class, "JAVA");
            assertEquals(id, lire(resultat.orElseThrow(), "id"));
            base.entityManager.getTransaction().commit();
        }
    }
}
