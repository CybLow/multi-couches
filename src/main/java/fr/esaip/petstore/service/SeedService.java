package fr.esaip.petstore.service;

import fr.esaip.petstore.dao.AddressDao;
import fr.esaip.petstore.dao.AnimalDao;
import fr.esaip.petstore.dao.PetStoreDao;
import fr.esaip.petstore.dao.ProductDao;
import fr.esaip.petstore.entity.Address;
import fr.esaip.petstore.entity.Animal;
import fr.esaip.petstore.entity.Cat;
import fr.esaip.petstore.entity.Fish;
import fr.esaip.petstore.entity.PetStore;
import fr.esaip.petstore.entity.Product;
import fr.esaip.petstore.entity.enums.FishLivEnv;
import fr.esaip.petstore.entity.enums.ProdType;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Insère un jeu de données de démonstration — couvre la consigne du sujet :
 * <em>« Insérer au moins trois enregistrements dans chacune des tables via
 * EntityManager »</em>.
 *
 * <p>Tout le seed se fait dans une seule transaction pilotée par l'appelant
 * (le {@code Main}). Le service n'ouvre pas de transaction : il invoque
 * uniquement les DAO.</p>
 */
public class SeedService {

    private final EntityManager em;
    private final AddressDao addressDao;
    private final PetStoreDao petStoreDao;
    private final AnimalDao animalDao;
    private final ProductDao productDao;

    public SeedService(EntityManager em) {
        this.em = em;
        this.addressDao = new AddressDao(em);
        this.petStoreDao = new PetStoreDao(em);
        this.animalDao = new AnimalDao(em);
        this.productDao = new ProductDao(em);
    }

    /**
     * Insère ≥ 3 enregistrements dans chaque table.
     *
     * <p>Ordre d'insertion choisi pour minimiser les `em.flush()` explicites :</p>
     * <ol>
     *     <li>Addresses (aucune dépendance)</li>
     *     <li>PetStores (réfèrent les Addresses, cascade = PERSIST sur Address suffirait d'ailleurs)</li>
     *     <li>Animals (réfèrent les PetStores via helper {@code addAnimal})</li>
     *     <li>Products (liés aux PetStores via le {@code @ManyToMany})</li>
     * </ol>
     */
    public void seedAll() {
        // 1. Addresses (≥ 3)
        Address a1 = new Address("12", "rue des Lilas",       "75011", "Paris");
        Address a2 = new Address("5",  "avenue des Platanes", "69003", "Lyon");
        Address a3 = new Address("28", "boulevard du Port",   "44000", "Nantes");
        addressDao.persist(a1);
        addressDao.persist(a2);
        addressDao.persist(a3);

        // 2. PetStores (≥ 3)
        PetStore s1 = new PetStore("Animalis Bastille",   "Alice MARTIN",     a1);
        PetStore s2 = new PetStore("Pattes & Plumes",     "Bernard DUPONT",   a2);
        PetStore s3 = new PetStore("Océan Nantais",       "Chloé ROBERT",     a3);
        petStoreDao.persist(s1);
        petStoreDao.persist(s2);
        petStoreDao.persist(s3);

        // 3. Animals (≥ 3) — mix Fish + Cat réparti entre les stores
        Cat  c1 = new Cat(dateOf(2023,  5, 12), "noir",  "250269606123456");
        Cat  c2 = new Cat(dateOf(2024,  2, 28), "roux",  "250269606987654");
        Fish f1 = new Fish(dateOf(2025,  1,  3), "bleu",  FishLivEnv.FRESH_WATER);
        Fish f2 = new Fish(dateOf(2025,  3, 17), "doré",  FishLivEnv.SEA_WATER);

        s1.addAnimal(c1);
        s1.addAnimal(f1);
        s2.addAnimal(c2);
        s3.addAnimal(f2);
        // Le cascade=ALL sur PetStore.animals fait le persist.

        // 4. Products (≥ 3) — + affectation aux PetStores via @ManyToMany
        Product p1 = new Product("CROQ-CHT-01", "Croquettes chat premium 3kg", ProdType.FOOD,      24.90);
        Product p2 = new Product("JOU-CHT-02",  "Arbre à chat 1m20",           ProdType.ACCESSORY, 59.00);
        Product p3 = new Product("LIT-MIN-01",  "Litière minérale 10L",        ProdType.CLEANING,   8.50);

        p1.addPetStore(s1);
        p1.addPetStore(s2);      // produit vendu dans 2 animaleries (démontre le N-N)
        p2.addPetStore(s2);
        p3.addPetStore(s1);
        p3.addPetStore(s3);

        productDao.persist(p1);
        productDao.persist(p2);
        productDao.persist(p3);

        em.flush(); // garantit que tout est bien écrit avant qu'on retourne
    }

    /** Petit helper : convertit (année, mois, jour) en {@link java.util.Date}. */
    private static Date dateOf(int year, int month, int day) {
        LocalDate local = LocalDate.of(year, month, day);
        return Date.from(local.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Les DAOs sont exposés aux tests / au Main si besoin d'inspection directe
    public List<Address> allAddresses() { return addressDao.findAll(); }
    public List<PetStore> allPetStores() { return petStoreDao.findAll(); }
    public List<Animal> allAnimals()    { return animalDao.findAll(); }
    public List<Product> allProducts()  { return productDao.findAll(); }
}
