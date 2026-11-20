package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class ConsultantRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierConsultant();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierConsultant();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Consultant.class, Competence.class)) {
            var repository = base.creerRepository(ConsultantRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.consultant(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "nom"), lire(recharge, "nom"));
            assertEquals(lire(objet, "prenom"), lire(recharge, "prenom"));
            assertEquals(0, ((java.math.BigDecimal) lire(objet, "tjm")).compareTo((java.math.BigDecimal) lire(recharge, "tjm")));
            assertEquals(lire(objet, "disponibleLe"), lire(recharge, "disponibleLe"));
Optional<?> resultat = (Optional<?>) rechercher(repository, "findByEmail", String.class, lire(objet, "email"));
            assertEquals(id, lire(resultat.orElseThrow(), "id"));
            base.entityManager.getTransaction().commit();
        }
    }
}
