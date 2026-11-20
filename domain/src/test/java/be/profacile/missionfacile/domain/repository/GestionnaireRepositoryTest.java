package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class GestionnaireRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierGestionnaire();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierGestionnaire();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Gestionnaire.class)) {
            var repository = base.creerRepository(GestionnaireRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.gestionnaire(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();

Optional<?> resultat = (Optional<?>) rechercher(repository, "findByEmail", String.class, lire(objet, "email"));
            assertEquals(id, lire(resultat.orElseThrow(), "id"));
            base.entityManager.getTransaction().commit();
        }
    }
}
