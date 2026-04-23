package fr.esaip.petstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public PetStore() {
    }

    public PetStore(String name, String managerName, Address address) {
        this.name = name;
        this.managerName = managerName;
        this.address = address;
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
