package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.Consultant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Bernard — compléter le repository Spring Data selon le contrat de test. */
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    Optional<Consultant> findByEmail(String email);
}
