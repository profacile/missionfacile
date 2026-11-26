package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Amandine — compléter le repository Spring Data selon le contrat de test. */
public interface CompetenceRepository extends JpaRepository<Competence, Long> {
    // TODO : JpaRepository et recherche indiquée dans docs/sprint-2.md.

    // Retrouver une compétence par son libellé exact, sans tenir compte des majuscules ("java" = "Java")
    Optional<Competence> findByLibelleIgnoreCase(String libelle);

    // Vérifier qu'une compétence existe avant d'en créer une nouvelle (évite les doublons)
    boolean existsByLibelleIgnoreCase(String libelle);

    // Autocomplétion : compétences dont le libellé contient le texte saisi ("jav" → "Java", "JavaScript")
    List<Competence> findByLibelleContainingIgnoreCaseOrderByLibelleAsc(String texte);
}
