package fr.esaip.petstore.entity;

import fr.esaip.petstore.entity.enums.ProdType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Produit vendu en animalerie.
 *
 * <p>Chaque produit peut être disponible dans plusieurs animaleries (relation
 * {@code @ManyToMany} avec {@link PetStore}), configurée dans une PR future.</p>
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "label", nullable = false, length = 120)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ProdType type;

    @Column(name = "price", nullable = false)
    private double price;

    /**
     * Animaleries qui vendent ce produit. {@code Product} est le côté
     * propriétaire du {@code @ManyToMany} — c'est lui qui définit la table
     * de jointure {@code product_petstore}.
     */
    @ManyToMany
    @JoinTable(
            name = "product_petstore",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "petstore_id")
    )
    private Set<PetStore> petStores = new HashSet<>();

    public Product() {
    }

    public Product(String code, String label, ProdType type, double price) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.price = price;
    }

    /** Helper bidirectionnel : ajoute l'animalerie et met à jour l'autre côté. */
    public void addPetStore(PetStore petStore) {
        petStores.add(petStore);
        petStore.getProducts().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ProdType getType() {
        return type;
    }

    public void setType(ProdType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<PetStore> getPetStores() {
        return petStores;
    }

    public void setPetStores(Set<PetStore> petStores) {
        this.petStores = petStores;
    }

    @Override
    public String toString() {
        return "Product#" + id + " [" + code + "] " + label + " (" + type + ", " + price + "€)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
