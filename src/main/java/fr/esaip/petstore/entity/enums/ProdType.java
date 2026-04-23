package fr.esaip.petstore.entity.enums;

/**
 * Type d'un produit vendu en animalerie.
 *
 * <p>Utilisé par {@code Product.type} — persisté en base sous forme de chaîne
 * (annotation {@code @Enumerated(EnumType.STRING)}) pour que le renommage ou
 * la ré-ordonnancement des valeurs ici ne casse pas les données existantes.</p>
 */
public enum ProdType {

    /** Nourriture (croquettes, pâtées, friandises...). */
    FOOD,

    /** Accessoire (laisse, collier, jouet, cage...). */
    ACCESSORY,

    /** Produit d'entretien (litière, désinfectant, shampooing...). */
    CLEANING
}
