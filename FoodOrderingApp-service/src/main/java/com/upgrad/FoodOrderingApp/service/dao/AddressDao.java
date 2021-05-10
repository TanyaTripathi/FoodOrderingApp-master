package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    //saves the customer address information of the created address record  in the database
    public AddressEntity createAddress(AddressEntity addressEntity) {
        this.entityManager.persist(addressEntity);
        return addressEntity;
    }
    //creates a customerAddress record
    public CustomerAddressEntity createCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        this.entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }
    //gets address record of a particular address uuid
    public AddressEntity getAddressByAddressUuid(String addressUuid) {
        try {
            return this.entityManager.createNamedQuery("addressByAddressUuid", AddressEntity.class).setParameter("uuid", addressUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    //gets address record by addressId
    public AddressEntity getAddressById(long id) {
        try {
            return this.entityManager.createNamedQuery("addressById", AddressEntity.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    //deletes a particular address record
    public String deleteAddress(AddressEntity addressEntity){
        String Uuid=addressEntity.getUuid();
        this.entityManager.remove(addressEntity);
        return Uuid;
    }
}
