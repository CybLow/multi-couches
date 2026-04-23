package fr.esaip.petstore.dao;

import fr.esaip.petstore.entity.Animal;
import jakarta.persistence.EntityManager;

/**
 * DAO pour {@link Animal}. Les sous-classes {@code Fish} et {@code Cat} sont
 * récupérées polymorphiquement via {@code findAll()} (JOINED).
 */
public class AnimalDao extends GenericDao<Animal, Long> {

    public AnimalDao(EntityManager em) {
        super(em, Animal.class);
    }
}
