package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<StateEntity> getAllStates(){

        try {
            return this.entityManager.createNamedQuery("allStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public StateEntity getStateByStateUuid(String StateUuid) {
        try {
            return (StateEntity)this.entityManager.createNamedQuery("stateByStateUuid", StateEntity.class).setParameter("uuid", StateUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public StateEntity getStateById(long id){
        try {
            return entityManager.createNamedQuery("getStateById" , StateEntity.class).setParameter("id", id).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
}
