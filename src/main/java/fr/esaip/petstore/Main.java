package fr.esaip.petstore;

import fr.esaip.petstore.config.EntityManagerFactoryProvider;
import fr.esaip.petstore.entity.Animal;
import fr.esaip.petstore.entity.PetStore;
import fr.esaip.petstore.service.PetStoreService;
import fr.esaip.petstore.service.SeedService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

/**
 * Point d'entrée du TP Eval Pet Store.
 *
 * <p>Déroulé du {@code mvn exec:java} :</p>
 * <ol>
 *     <li>Hibernate crée le schéma (persistence.xml → hbm2ddl.auto = create)</li>
 *     <li>{@link SeedService#seedAll()} insère ≥ 3 enregistrements par table</li>
 *     <li>{@link PetStoreService#findAnimalsOfStore(Long)} extrait tous les
 *         animaux de la première animalerie (requête JPQL imposée par le sujet)</li>
 *     <li>Affichage des résultats dans un format lisible</li>
 * </ol>
 */
public final class Main {

    private Main() {
        // utility class — instantiation is forbidden
    }

    public static void main(String[] args) {
        System.out.println("=== TP Eval Pet Store — démarrage ===");

        EntityManager em = EntityManagerFactoryProvider.getEntityManager();
        try {
            seedDemoData(em);
            printCounts(em);
            printAnimalsOfFirstStore(em);
        } catch (RuntimeException e) {
            System.err.println("Erreur pendant l'exécution : " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
            EntityManagerFactoryProvider.close();
        }

        System.out.println("=== TP Eval Pet Store — terminé ===");
    }

    /** Insère les données de démonstration dans une transaction dédiée. */
    private static void seedDemoData(EntityManager em) {
        System.out.println("\n--- 1. Insertion des données de démonstration ---");
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            new SeedService(em).seedAll();
            tx.commit();
            System.out.println("✅ Seed terminé — commit OK.");
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    /** Affiche le nombre d'enregistrements par table pour validation. */
    private static void printCounts(EntityManager em) {
        System.out.println("\n--- 2. Vérification du nombre d'enregistrements ---");
        SeedService seed = new SeedService(em);
        System.out.printf("  • %d Addresses%n",  seed.allAddresses().size());
        System.out.printf("  • %d PetStores%n",  seed.allPetStores().size());
        System.out.printf("  • %d Animals%n",    seed.allAnimals().size());
        System.out.printf("  • %d Products%n",   seed.allProducts().size());
    }

    /**
     * Utilise le service pour retrouver les animaux de la 1re animalerie créée
     * et les affiche — démontre le polymorphisme JOINED (mix Fish + Cat).
     */
    private static void printAnimalsOfFirstStore(EntityManager em) {
        System.out.println("\n--- 3. Animaux de la 1re animalerie (requête JPQL imposée) ---");
        PetStoreService service = new PetStoreService(em);

        // On prend l'id le plus petit (= 1re animalerie insérée)
        List<PetStore> stores = new SeedService(em).allPetStores();
        if (stores.isEmpty()) {
            System.out.println("(aucune animalerie en base)");
            return;
        }
        PetStore firstStore = stores.stream()
                .min((a, b) -> a.getId().compareTo(b.getId()))
                .orElseThrow();

        System.out.println("🏪 " + firstStore);
        List<Animal> animals = service.findAnimalsOfStore(firstStore.getId());
        if (animals.isEmpty()) {
            System.out.println("  (aucun animal)");
        } else {
            animals.forEach(a -> System.out.println("  🐾 " + a));
        }
        System.out.printf("✅ Requête OK — %d animal(s) trouvé(s).%n", animals.size());
    }
}
