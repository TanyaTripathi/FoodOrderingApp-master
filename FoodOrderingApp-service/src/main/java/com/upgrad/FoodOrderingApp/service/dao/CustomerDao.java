package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;


    //saves the customer information of the created customer in the database
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        this.entityManager.persist(customerEntity);
        return customerEntity;
    }
    //returns customer with a particular contact number
    public CustomerEntity getCustomerByContactNumber(String contactNumber) {
        try {
            return (CustomerEntity)this.entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    //creates a record in CustomerAuthEntity
    public CustomerAuthEntity createAuthToken(CustomerAuthEntity customerAuthEntity) {
        this.entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }
    //updates a CustomerEntity
    public void updateCustomer(CustomerEntity updatedCustomerEntity) {
        this.entityManager.merge(updatedCustomerEntity);
    }
    //retrives a matching record in CustomerAuthEntity according to a particular accessToken
    public CustomerAuthEntity getCustomerAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("customerAuthTokenByAccessToken", CustomerAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


}
