package fr.esaip.petstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Animalerie — entité pivot du modèle.
 *
 * <p>Relations (à câbler au fil des commits) :</p>
 * <ul>
 *   <li>1 — 1 {@link Address} via {@code @ManyToOne(unique=true)} (consigne sujet)</li>
 *   <li>1 — N {@link Animal} via {@code @OneToMany(mappedBy="petStore")}</li>
 *   <li>N — N {@link Product} via {@code @ManyToMany(mappedBy="petStores")}</li>
 * </ul>
 */
@Entity
@Table(name = "pet_store")
public class PetStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "manager_name", nullable = false, length = 80)
    private String managerName;

    /**
     * Adresse de l'animalerie. On utilise {@code @ManyToOne(unique=true)}
     * plutôt que {@code @OneToOne} pour respecter la consigne du sujet
     * ("@OneToMany, @ManyToMany et @ManyToOne" seulement). Le {@code unique=true}
     * sur la FK garantit la cardinalité 1:1 côté DB.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    /**
     * Animaux vendus dans cette animalerie. Le côté propriétaire est
     * {@link Animal#getPetStore()} (FK {@code petstore_id}).
     * {@code cascade=ALL} + {@code orphanRemoval=true} : supprimer un animal
     * du set suffit à le supprimer en base.
     */
    @OneToMany(mappedBy = "petStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();

    public PetStore() {
    }

    public PetStore(String name, String managerName, Address address) {
        this.name = name;
        this.managerName = managerName;
        this.address = address;
    }

    /** Helper bidirectionnel : ajoute l'animal et configure sa back-reference. */
    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setPetStore(this);
    }

    /** Helper bidirectionnel : retire l'animal et nettoie sa back-reference. */
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.setPetStore(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "PetStore#" + id + " '" + name + "' (manager=" + managerName + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetStore that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
