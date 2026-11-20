package be.profacile.missionfacile.domain.support;

import be.profacile.missionfacile.domain.model.*;
import be.profacile.missionfacile.domain.repository.*;
import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.missionfacile.domain.support.JpaExerciseSupport.*;

/** Contrats de mapping issus du diagramme fourni, à conserver dans les tests. */
public final class MissionContract {
    private MissionContract() {
    }

    public static void verifierUtilisateur() {
        entite(Utilisateur.class, "utilisateur");
        assertTrue(java.lang.reflect.Modifier.isAbstract(Utilisateur.class.getModifiers()));
        Inheritance heritage = Utilisateur.class.getAnnotation(Inheritance.class);
        assertNotNull(heritage, "Mapper l'héritage utilisateur");
        assertEquals(InheritanceType.JOINED, heritage.strategy());
        colonne(Utilisateur.class, "email", String.class, "email", false);
        assertTrue(champ(Utilisateur.class, "email", String.class).getAnnotation(Column.class).unique());
        colonne(Utilisateur.class, "motDePasse", String.class, "mot_de_passe", false);
        colonne(Utilisateur.class, "dateInscription", LocalDateTime.class, "date_inscription", false);
        repository(UtilisateurRepository.class, Utilisateur.class);
    }

    public static void verifierEntreprise() {
        entite(Entreprise.class, "entreprise");
        assertEquals(Utilisateur.class, Entreprise.class.getSuperclass(), "Héritage du diagramme");
        colonne(Entreprise.class, "nom", String.class, "nom", false);
        colonne(Entreprise.class, "numeroTva", String.class, "numero_tva", false);
        assertTrue(champ(Entreprise.class, "numeroTva", String.class).getAnnotation(Column.class).unique());
        colonne(Entreprise.class, "secteur", String.class, "secteur", false);
        repository(EntrepriseRepository.class, Entreprise.class);
    }

    public static void verifierGestionnaire() {
        entite(Gestionnaire.class, "gestionnaire");
        assertEquals(Utilisateur.class, Gestionnaire.class.getSuperclass(), "Héritage du diagramme");
        repository(GestionnaireRepository.class, Gestionnaire.class);
    }

    public static void verifierCompetence() {
        entite(Competence.class, "competence");
        colonne(Competence.class, "libelle", String.class, "libelle", false);
        assertTrue(champ(Competence.class, "libelle", String.class).getAnnotation(Column.class).unique());
        repository(CompetenceRepository.class, Competence.class);
    }

    public static void verifierOffre() {
        entite(Offre.class, "offre");
        colonne(Offre.class, "titre", String.class, "titre", false);
        colonne(Offre.class, "description", String.class, "description", false);
        colonne(Offre.class, "lieu", String.class, "lieu", false);
        colonne(Offre.class, "dureeJours", int.class, "duree_jours", false);
        colonne(Offre.class, "tjmMax", BigDecimal.class, "tjm_max", false);
        colonne(Offre.class, "statut", StatutOffre.class, "statut", false);
        Enumerated enumeration = champ(Offre.class, "statut", StatutOffre.class).getAnnotation(Enumerated.class);
        assertNotNull(enumeration);
        assertEquals(EnumType.STRING, enumeration.value());
        assertEquals(List.of("BROUILLON", "EN_VALIDATION", "PUBLIEE", "POURVUE", "CLOTUREE"), Arrays.stream(StatutOffre.values()).map(Enum::name).toList());
        relation(Offre.class, "entreprise", Entreprise.class, "entreprise_id");
        Field gestionnaire = champ(Offre.class, "gestionnaire", Gestionnaire.class);
        assertNotNull(gestionnaire.getAnnotation(ManyToOne.class));
        assertTrue(gestionnaire.getAnnotation(ManyToOne.class).optional());
        assertNotNull(gestionnaire.getAnnotation(JoinColumn.class));
        assertEquals("gestionnaire_id", gestionnaire.getAnnotation(JoinColumn.class).name());
        assertTrue(gestionnaire.getAnnotation(JoinColumn.class).nullable());
        Field competences = champ(Offre.class, "competences", Set.class);
        assertNotNull(competences.getAnnotation(ManyToMany.class));
        JoinTable jointure = competences.getAnnotation(JoinTable.class);
        assertNotNull(jointure, "Déclarer la table de jointure des compétences");
        assertEquals("offre_competence", jointure.name());
        assertEquals("offre_id", jointure.joinColumns()[0].name());
        assertEquals("competence_id", jointure.inverseJoinColumns()[0].name());
        repository(OffreRepository.class, Offre.class);
    }

    public static void verifierConsultant() {
        entite(Consultant.class, "consultant");
        assertEquals(Utilisateur.class, Consultant.class.getSuperclass(), "Héritage du diagramme");
        colonne(Consultant.class, "nom", String.class, "nom", false);
        colonne(Consultant.class, "prenom", String.class, "prenom", false);
        colonne(Consultant.class, "tjm", BigDecimal.class, "tjm", false);
        colonne(Consultant.class, "disponibleLe", LocalDate.class, "disponible_le", false);
        Field competences = champ(Consultant.class, "competences", Set.class);
        assertNotNull(competences.getAnnotation(ManyToMany.class));
        JoinTable jointure = competences.getAnnotation(JoinTable.class);
        assertNotNull(jointure, "Déclarer la table de jointure des compétences");
        assertEquals("consultant_competence", jointure.name());
        assertEquals("consultant_id", jointure.joinColumns()[0].name());
        assertEquals("competence_id", jointure.inverseJoinColumns()[0].name());
        repository(ConsultantRepository.class, Consultant.class);
    }

    public static void verifierCandidature() {
        entite(Candidature.class, "candidature");
        boolean contrainteTrouvee = Arrays.stream(Candidature.class.getAnnotation(Table.class).uniqueConstraints())
            .anyMatch(contrainte -> Set.of(contrainte.columnNames()).equals(Set.of("offre_id", "consultant_id")));
        assertTrue(contrainteTrouvee, "Ajouter la contrainte unique sur offre_id et consultant_id");
        colonne(Candidature.class, "message", String.class, "message", false);
        colonne(Candidature.class, "dateCandidature", LocalDateTime.class, "date_candidature", false);
        colonne(Candidature.class, "statut", StatutCandidature.class, "statut", false);
        Enumerated enumeration = champ(Candidature.class, "statut", StatutCandidature.class).getAnnotation(Enumerated.class);
        assertNotNull(enumeration);
        assertEquals(EnumType.STRING, enumeration.value());
        assertEquals(List.of("RECUE", "PRESELECTIONNEE", "RETENUE", "REFUSEE"), Arrays.stream(StatutCandidature.values()).map(Enum::name).toList());
        relation(Candidature.class, "offre", Offre.class, "offre_id");
        relation(Candidature.class, "consultant", Consultant.class, "consultant_id");
        repository(CandidatureRepository.class, Candidature.class);
    }

}
