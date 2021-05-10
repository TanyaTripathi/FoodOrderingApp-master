package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    //Respective Data access object has been autowired to access the method defined in respective Dao
    @Autowired
    private RestaurantDao restaurantDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurants() { return restaurantDao.getAllRestaurants(); }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantsByName(String reastaurantName) throws RestaurantNotFoundException {

        List<RestaurantEntity> restaurantsByName =  restaurantDao.getRestaurantsByName(reastaurantName);

        if (restaurantsByName == null) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        return restaurantsByName;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getRestaurantByCategoryId(String categoryUuid) throws CategoryNotFoundException {

        List<RestaurantEntity> restaurantsByCategoryId =  restaurantDao.getRestaurantsByCategoryId(categoryUuid);

        if (categoryUuid.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }else if(restaurantsByCategoryId == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        return restaurantsByCategoryId;
    }
}
