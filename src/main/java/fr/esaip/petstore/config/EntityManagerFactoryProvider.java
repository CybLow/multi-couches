package fr.esaip.petstore.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Fournit un {@link EntityManagerFactory} unique et thread-safe pour toute
 * l'application.
 *
 * <p>Le nom de la persistence-unit ({@value #PERSISTENCE_UNIT_NAME}) est
 * défini dans {@code META-INF/persistence.xml}. L'EMF est créé de façon
 * paresseuse à la première demande (Initialization-on-demand Holder Idiom),
 * et doit être fermé explicitement par l'appelant via {@link #close()} à
 * la fin du traitement.</p>
 *
 * <p>Utilisation typique :</p>
 * <pre>
 *   EntityManager em = EntityManagerFactoryProvider.getEntityManager();
 *   try {
 *       em.getTransaction().begin();
 *       // ...
 *       em.getTransaction().commit();
 *   } finally {
 *       em.close();
 *       EntityManagerFactoryProvider.close();
 *   }
 * </pre>
 */
public final class EntityManagerFactoryProvider {

    private static final String PERSISTENCE_UNIT_NAME = "petstore-pu";

    /** Instance unique, initialisée paresseusement (holder pattern). */
    private static final class Holder {
        private static final EntityManagerFactory INSTANCE =
                Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
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

    /**
     * Ferme l'EMF partagé. À appeler une fois le traitement terminé.
     *
     * <p>On évite volontairement un shutdown hook JVM : sous
     * {@code exec-maven-plugin}, le plugin détruit son {@code URLClassLoader}
     * dès la fin de {@code main()}, ce qui rend inaccessibles les classes
     * Hibernate nécessaires à la fermeture si elle est déclenchée par le
     * hook. Un close explicite depuis le {@code Main} est plus propre et
     * évite toute stack trace.</p>
     */
    public static void close() {
        if (Holder.INSTANCE.isOpen()) {
            Holder.INSTANCE.close();
        }
    }
}
