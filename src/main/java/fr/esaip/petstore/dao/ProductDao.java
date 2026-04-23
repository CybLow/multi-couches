package fr.esaip.petstore.dao;

import fr.esaip.petstore.entity.Product;
import jakarta.persistence.EntityManager;

/** DAO CRUD pour {@link Product}. */
public class ProductDao extends GenericDao<Product, Long> {

    public ProductDao(EntityManager em) {
        super(em, Product.class);
    }
}
