package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.support.*;
import be.profacile.missionfacile.domain.support.JpaExerciseSupport.BaseJpa;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

class UtilisateurRepositoryTest {
    @Test
    void leMappingEtLeRepositoryRespectentLeContrat() {
        MissionContract.verifierUtilisateur();
    }

    @Test
    void sauvegardeRelitEtRechercheLesDonnees() throws Exception {
        MissionContract.verifierUtilisateur();
        try (BaseJpa base = new BaseJpa(Utilisateur.class, Entreprise.class)) {
            var repository = base.creerRepository(UtilisateurRepository.class);
            base.entityManager.getTransaction().begin();
            Object objet = MissionFixtures.entreprise(base.entityManager);
            repository.saveAndFlush(objet);
            Long id = (Long) lire(objet, "id");
            assertNotNull(id);
            base.entityManager.clear();
            Object recharge = repository.findById(id).orElseThrow();
            assertEquals(lire(objet, "email"), lire(recharge, "email"));
            assertEquals(lire(objet, "motDePasse"), lire(recharge, "motDePasse"));
            assertEquals(lire(objet, "dateInscription"), lire(recharge, "dateInscription"));
Optional<?> resultat = (Optional<?>) rechercher(repository, "findByEmail", String.class, lire(objet, "email"));
            assertEquals(id, lire(resultat.orElseThrow(), "id"));
            base.entityManager.getTransaction().commit();
        }
    }
}
