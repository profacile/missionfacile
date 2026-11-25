package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.Candidature;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Bernard — compléter le repository Spring Data selon le contrat de test. */
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByOffreId(Long offreId);
}
