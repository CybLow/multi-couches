package fr.esaip.petstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.Objects;

/**
 * Classe mère abstraite de la hiérarchie des animaux vendus en animalerie.
 *
 * <h2>Stratégie d'héritage : {@link InheritanceType#JOINED JOINED}</h2>
 *
 * <p>Chaque entité concrète ({@code Fish}, {@code Cat}) possède sa propre table,
 * qui contient uniquement ses champs spécifiques. La PK de ces tables est aussi
 * une FK vers {@code animal.id} — l'équivalent SQL d'une "table par classe avec
 * jointure".</p>
 *
 * <p>Pour récupérer un {@code Fish}, Hibernate fait un {@code INNER JOIN} entre
 * {@code fish} et {@code animal}. Pour une requête polymorphe sur
 * {@code Animal} (ex: {@code SELECT a FROM Animal a}), Hibernate fait un
 * {@code LEFT OUTER JOIN} sur chaque table fille pour instancier le bon type
 * concret à la volée.</p>
 *
 * <h2>Pourquoi {@code @Entity} et pas {@code @MappedSuperclass} ?</h2>
 *
 * <p>{@code @MappedSuperclass} serait incompatible avec JOINED : cette
 * annotation indique que la classe n'a PAS de table propre — ses champs sont
 * copiés dans chaque sous-classe (équivalent de la stratégie
 * {@code TABLE_PER_CLASS}). JOINED exige au contraire une table parent pour
 * les colonnes communes.</p>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "animal")
public abstract class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth", nullable = false)
    private Date birth;

    @Column(name = "couleur", nullable = false, length = 40)
    private String couleur;

    protected Animal() {
    }

    protected Animal(Date birth, String couleur) {
        this.birth = birth;
        this.couleur = couleur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Animal that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
