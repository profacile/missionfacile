package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class EntrepriseRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierEntreprise();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierEntreprise();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Entreprise.class)) {
            var repository = base.creerRepository(EntrepriseRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.entreprise(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "nom"), lire(recharge, "nom"));
            assertEquals(lire(objet, "numeroTva"), lire(recharge, "numeroTva"));
            assertEquals(lire(objet, "secteur"), lire(recharge, "secteur"));
Optional<?> resultat = (Optional<?>) rechercher(repository, "findByNumeroTva", String.class, lire(objet, "numeroTva"));
            assertEquals(id, lire(resultat.orElseThrow(), "id"));
            base.entityManager.getTransaction().commit();
        }
    }
}
