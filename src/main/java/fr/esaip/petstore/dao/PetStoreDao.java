package fr.esaip.petstore.dao;

import fr.esaip.petstore.entity.Animal;
import fr.esaip.petstore.entity.PetStore;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * DAO pour {@link PetStore} — enrichi de la requête métier
 * {@link #findAllAnimalsByPetStore(Long)}.
 */
public class PetStoreDao extends GenericDao<PetStore, Long> {

    public PetStoreDao(EntityManager em) {
        super(em, PetStore.class);
    }

    /**
     * Récupère tous les animaux (toutes sous-classes confondues grâce à la
     * stratégie JOINED) vendus par une animalerie donnée.
     *
     * <p>Cette requête correspond à la consigne du sujet : <em>« Réaliser une
     * requête qui permet d'extraire tous les animaux d'une animalerie
     * donnée »</em>.</p>
     *
     * <p>La requête JPQL porte sur la classe mère {@code Animal}, Hibernate
     * instancie le bon type concret ({@code Fish} ou {@code Cat}) à la volée
     * via les colonnes discriminantes générées par JOINED.</p>
     *
     * @param petStoreId identifiant de l'animalerie
     * @return la liste des animaux, éventuellement vide
     */
    public List<Animal> findAllAnimalsByPetStore(Long petStoreId) {
        return em.createQuery(
                        "SELECT a FROM Animal a WHERE a.petStore.id = :petStoreId",
                        Animal.class)
                .setParameter("petStoreId", petStoreId)
                .getResultList();
    }
}
