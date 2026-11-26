package be.profacile.missionfacile.domain.repository;

import be.profacile.missionfacile.domain.model.Offre;
import be.profacile.missionfacile.domain.model.StatutOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/** Amandine — compléter le repository Spring Data selon le contrat de test. */
public interface OffreRepository extends JpaRepository<Offre, Long> {
    // TODO : JpaRepository et recherche indiquée dans docs/sprint-2.md.

    // Offres d'un statut donné (ex. EN_VALIDATION pour le gestionnaire, PUBLIEE pour les consultants)
    List<Offre> findByStatut(StatutOffre statut);

    // « Mes offres » d'une entreprise
    List<Offre> findByEntrepriseId(Long entrepriseId);

    // Offres publiées requérant une compétence donnée
    List<Offre> findByStatutAndCompetencesLibelleIgnoreCase(StatutOffre statut, String libelle);

    // Recherche avec filtres (UC2) : un paramètre null = filtre ignoré
    @Query("""
           SELECT DISTINCT o FROM Offre o
           LEFT JOIN o.competences c
           WHERE o.statut = be.profacile.missionfacile.domain.model.StatutOffre.PUBLIEE
             AND (:competence IS NULL OR LOWER(c.libelle) = LOWER(:competence))
             AND (:lieu IS NULL OR LOWER(o.lieu) LIKE LOWER(CONCAT('%', :lieu, '%')))
             AND (:tjmMin IS NULL OR o.tjmMax >= :tjmMin)
           """)
    List<Offre> rechercher(@Param("competence") String competence,
                           @Param("lieu") String lieu,
                           @Param("tjmMin") BigDecimal tjmMin);
}