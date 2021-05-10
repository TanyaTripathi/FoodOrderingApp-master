package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants(){

        try {
            return this.entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByName(String reastaurantName){

        try {
            return this.entityManager.createNamedQuery("allRestaurantsByName", RestaurantEntity.class).setParameter("name", "%" + reastaurantName +"%").getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByCategoryId(String categoryUuid){
        try{

            return this.entityManager.createNamedQuery("allRestaurantBycategory", RestaurantEntity.class).setParameter("uuid", categoryUuid).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }
}
