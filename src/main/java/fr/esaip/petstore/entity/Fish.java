package fr.esaip.petstore.entity;

import fr.esaip.petstore.entity.enums.FishLivEnv;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Poisson vendu en animalerie. Hérite de {@link Animal} via la stratégie
 * {@link jakarta.persistence.InheritanceType#JOINED JOINED} : la table
 * {@code fish} ne contient que le champ {@code living_env} et sa PK
 * référence {@code animal.id}.
 */
@Entity
@Table(name = "fish")
public class Fish extends Animal {

    @Enumerated(EnumType.STRING)
    @Column(name = "living_env", nullable = false, length = 20)
    private FishLivEnv livingEnv;

    public Fish() {
    }

    public Fish(Date birth, String couleur, FishLivEnv livingEnv) {
        super(birth, couleur);
        this.livingEnv = livingEnv;
    }

    public FishLivEnv getLivingEnv() {
        return livingEnv;
    }

    public void setLivingEnv(FishLivEnv livingEnv) {
        this.livingEnv = livingEnv;
    }

    @Override
    public String toString() {
        return "Fish#" + getId() + " (" + livingEnv + ", " + getCouleur() + ")";
    }
}
