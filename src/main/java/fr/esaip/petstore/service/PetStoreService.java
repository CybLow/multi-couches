package fr.esaip.petstore.service;

import fr.esaip.petstore.dao.PetStoreDao;
import fr.esaip.petstore.entity.Animal;
import fr.esaip.petstore.entity.PetStore;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service métier exposant les cas d'usage autour de {@link PetStore}.
 * Pour l'instant, simple delegate vers {@link PetStoreDao}, mais permet
 * d'ajouter plus tard de la validation, du logging ou du cache métier
 * sans casser les appelants.
 */
public class PetStoreService {

    private final PetStoreDao petStoreDao;

    public PetStoreService(EntityManager em) {
        this.petStoreDao = new PetStoreDao(em);
    }

    /**
     * Retourne l'animalerie identifiée par {@code id}, ou {@code null} si
     * elle n'existe pas.
     */
    public PetStore getById(Long id) {
        return petStoreDao.find(id);
    }

    /**
     * Retourne tous les animaux (toutes sous-classes JOINED confondues)
     * vendus par l'animalerie donnée.
     *
     * <p>Correspond à la requête imposée par le sujet.</p>
     */
    public List<Animal> findAnimalsOfStore(Long petStoreId) {
        return petStoreDao.findAllAnimalsByPetStore(petStoreId);
    }
}
