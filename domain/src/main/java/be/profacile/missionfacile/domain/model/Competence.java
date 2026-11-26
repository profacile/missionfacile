package be.profacile.missionfacile.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Amandine — compléter le mapping JPA selon le diagramme et docs/sprint-2.md. */
@Entity
@Table(name = "competence")
public class Competence {
    // TODO : annotations, champs, héritage éventuel, relations et accesseurs.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    protected Competence() {
        // constructeur requis par JPA
    }

    public Competence(String libelle) {
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}