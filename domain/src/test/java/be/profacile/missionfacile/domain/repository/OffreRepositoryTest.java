package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class OffreRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierOffre();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierOffre();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Entreprise.class, Gestionnaire.class, Offre.class, Competence.class)) {
            var repository = base.creerRepository(OffreRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.offre(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "titre"), lire(recharge, "titre"));
            assertEquals(lire(objet, "description"), lire(recharge, "description"));
            assertEquals(lire(objet, "lieu"), lire(recharge, "lieu"));
            assertEquals(lire(objet, "dureeJours"), lire(recharge, "dureeJours"));
            assertEquals(0, ((java.math.BigDecimal) lire(objet, "tjmMax")).compareTo((java.math.BigDecimal) lire(recharge, "tjmMax")));
            assertEquals(lire(objet, "statut"), lire(recharge, "statut"));
Long entrepriseId = (Long) lire(lire(objet, "entreprise"), "id");
            List<?> resultat = (List<?>) rechercher(repository, "findByEntrepriseId", Long.class, entrepriseId);
            assertEquals(1, resultat.size());
            assertEquals(id, lire(resultat.getFirst(), "id"));
            assertTrue(((List<?>) rechercher(repository, "findByEntrepriseId", Long.class, Long.MAX_VALUE)).isEmpty());
            base.entityManager.getTransaction().commit();
        }
    }
}
