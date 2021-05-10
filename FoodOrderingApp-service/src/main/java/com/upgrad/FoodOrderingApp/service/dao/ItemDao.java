package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;



    public List<ItemEntity> getAllItems(){

        try {
            return this.entityManager.createNamedQuery("getAllItems", ItemEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public ItemEntity getStateByItemUuid(String itemUuid) {
        try {
            return (ItemEntity)this.entityManager.createNamedQuery("itemByItemUuid", ItemEntity.class).setParameter("uuid", itemUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
