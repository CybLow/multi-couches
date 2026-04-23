package fr.esaip.petstore.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Fournit un {@link EntityManagerFactory} unique et thread-safe pour toute
 * l'application.
 *
 * <p>Le nom de la persistence-unit (« petstore-pu ») est défini dans
 * {@code META-INF/persistence.xml}. L'EMF est créé de façon paresseuse à la
 * première demande et fermé proprement via le {@linkplain Runtime#addShutdownHook
 * shutdown hook} enregistré dans le bloc statique.</p>
 *
 * <p>Utilisation typique :</p>
 * <pre>
 *   EntityManager em = EntityManagerFactoryProvider.getEntityManager();
 *   em.getTransaction().begin();
 *   // ...
 *   em.getTransaction().commit();
 *   em.close();
 * </pre>
 */
public final class EntityManagerFactoryProvider {

    private static final String PERSISTENCE_UNIT_NAME = "petstore-pu";

    /** Instance unique, initialisée paresseusement (holder pattern). */
    private static final class Holder {
        private static final EntityManagerFactory INSTANCE =
                Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    static {
        // Fermeture propre de l'EMF à l'arrêt de la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Holder.INSTANCE.isOpen()) {
                Holder.INSTANCE.close();
            }
        }, "emf-shutdown-hook"));
    }

    private EntityManagerFactoryProvider() {
        // utility class — instantiation is forbidden
    }

    /** @return l'EntityManagerFactory partagé (créé au 1er appel). */
    public static EntityManagerFactory getEntityManagerFactory() {
        return Holder.INSTANCE;
    }

    /** @return un nouvel EntityManager prêt à l'emploi. À fermer par l'appelant. */
    public static EntityManager getEntityManager() {
        return Holder.INSTANCE.createEntityManager();
    }
}
