package fr.esaip.petstore.dao;

import fr.esaip.petstore.entity.Address;
import jakarta.persistence.EntityManager;

/** DAO CRUD pour {@link Address}. */
public class AddressDao extends GenericDao<Address, Long> {

    public AddressDao(EntityManager em) {
        super(em, Address.class);
    }
}
