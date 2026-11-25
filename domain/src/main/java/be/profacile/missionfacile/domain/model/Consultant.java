package be.profacile.missionfacile.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** Bernard — compléter le mapping JPA selon le diagramme et docs/sprint-2.md. */
@Entity
@Table(name = "consultant")
public class Consultant extends Utilisateur {

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "tjm", nullable = false)
    private BigDecimal tjm;

    @Column(name = "disponible_le", nullable = false)
    private LocalDate disponibleLe;

    @ManyToMany
    @JoinTable(name = "consultant_competence",
        joinColumns = @JoinColumn(name = "consultant_id"),
        inverseJoinColumns = @JoinColumn(name = "competence_id"))
    private Set<Competence> competences = new HashSet<>();

    public Consultant() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getTjm() {
        return tjm;
    }

    public void setTjm(BigDecimal tjm) {
        this.tjm = tjm;
    }

    public LocalDate getDisponibleLe() {
        return disponibleLe;
    }

    public void setDisponibleLe(LocalDate disponibleLe) {
        this.disponibleLe = disponibleLe;
    }

    public Set<Competence> getCompetences() {
        return competences;
    }

    public void setCompetences(Set<Competence> competences) {
        this.competences = competences;
    }
}
