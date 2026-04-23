package fr.esaip.petstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Chat vendu en animalerie. Hérite de {@link Animal} via la stratégie
 * {@link jakarta.persistence.InheritanceType#JOINED JOINED}.
 */
@Entity
@Table(name = "cat")
public class Cat extends Animal {

    @Column(name = "chip_id", nullable = false, length = 32, unique = true)
    private String chipId;

    public Cat() {
    }

    public Cat(Date birth, String couleur, String chipId) {
        super(birth, couleur);
        this.chipId = chipId;
    }

    public String getChipId() {
        return chipId;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    @Override
    public String toString() {
        return "Cat#" + getId() + " (chip=" + chipId + ", " + getCouleur() + ")";
    }
}
