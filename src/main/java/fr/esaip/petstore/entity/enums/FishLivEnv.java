package fr.esaip.petstore.entity.enums;

/**
 * Environnement de vie d'un poisson.
 *
 * <p>Utilisé par {@code Fish.livingEnv}, persisté en {@code EnumType.STRING}.</p>
 */
public enum FishLivEnv {

    /** Eau douce (rivière, lac, aquarium d'eau douce). */
    FRESH_WATER,

    /** Eau salée (océan, mer, aquarium marin). */
    SEA_WATER
}
