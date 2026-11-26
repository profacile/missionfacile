package be.profacile.missionfacile.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/** Amandine — compléter le mapping JPA selon le diagramme et docs/sprint-2.md. */
@Entity
@Table(name = "offre")
public class Offre {
    // TODO : annotations, champs, héritage éventuel, relations et accesseurs.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String lieu;

    private int dureeJours;

    @Column(precision = 10, scale = 2)
    private BigDecimal tjmMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutOffre statut = StatutOffre.BROUILLON;

    // « publie » : Entreprise 1 —— * Offre
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    // « valide » : Gestionnaire 0..1 —— * Offre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    // « requiert » : Offre * —— * Competence
    @ManyToMany
    @JoinTable(
            name = "offre_competence",
            joinColumns = @JoinColumn(name = "offre_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    private Set<Competence> competences = new HashSet<>();

    protected Offre() {
        // constructeur requis par JPA
    }

    public Offre(String titre, String description, String lieu,
                 int dureeJours, BigDecimal tjmMax, Entreprise entreprise) {
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dureeJours = dureeJours;
        this.tjmMax = tjmMax;
        this.entreprise = entreprise;
    }

    // ---- Transitions du diagramme d'états « stm Offre » ----

    public void soumettre() {
        verifierStatut(StatutOffre.BROUILLON);
        this.statut = StatutOffre.EN_VALIDATION;
    }

    public void publier() {
        verifierStatut(StatutOffre.EN_VALIDATION);
        this.statut = StatutOffre.PUBLIEE;
    }

    public void refuser() {
        verifierStatut(StatutOffre.EN_VALIDATION);
        this.statut = StatutOffre.BROUILLON;
    }

    public void pourvoir() {
        verifierStatut(StatutOffre.PUBLIEE);
        this.statut = StatutOffre.POURVUE;
    }

    public void cloturer() {
        verifierStatut(StatutOffre.PUBLIEE);
        this.statut = StatutOffre.CLOTUREE;
    }

    private void verifierStatut(StatutOffre attendu) {
        if (this.statut != attendu) {
            throw new IllegalStateException(
                    "Transition impossible : l'offre est " + statut + ", attendu " + attendu);
        }
    }

    // ---- Compétences ----

    public void ajouterCompetence(Competence competence) {
        competences.add(competence);
    }

    public void retirerCompetence(Competence competence) {
        competences.remove(competence);
    }

    // ---- Accesseurs ----

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }

    public BigDecimal getTjmMax() {
        return tjmMax;
    }

    public void setTjmMax(BigDecimal tjmMax) {
        this.tjmMax = tjmMax;
    }

    public StatutOffre getStatut() {
        return statut;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public Gestionnaire getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public Set<Competence> getCompetences() {
        return competences;
    }
}
