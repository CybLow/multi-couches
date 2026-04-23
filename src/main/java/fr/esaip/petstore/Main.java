package fr.esaip.petstore;

/**
 * Point d'entrée de l'application Pet Store.
 *
 * <p>Le comportement complet (bootstrap de l'EntityManager, insertion des
 * données de démonstration via le {@code SeedService} et affichage du
 * résultat de la requête {@code findAllAnimalsByPetStore}) sera implémenté
 * dans une Pull Request dédiée (issue #11).</p>
 */
public final class Main {

    private Main() {
        // utility class — instantiation is forbidden
    }

    public static void main(String[] args) {
        System.out.println("Pet Store — scaffold OK. Orchestration à venir (issue #11).");
    }
}
