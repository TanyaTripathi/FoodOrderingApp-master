package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(value = "Restaurant Controller",description = "end-points for getting restaurant related information")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AddressService addressService;

    @PutMapping("/{restaurant_id}")
    @ApiOperation(value="Update restaurant details based upon the Restaurant ID")
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@PathVariable("restaurant_id") String restaurantId){

        return new ResponseEntity<RestaurantUpdatedResponse>(HttpStatus.OK);
    }

    @GetMapping("/{restaurant_id}")
    @ApiOperation(value = "Retrieve restaurant details based upon the Restaurant ID")
        public ResponseEntity<RestaurantListResponse> getRestaurantDetailsById(@PathVariable("restaurant_id") String restaurantId){

        return new ResponseEntity<RestaurantListResponse>(HttpStatus.OK);
    }

    @GetMapping("restaurant/category/{category_id}")
    @ApiOperation(value = "Retrieve restaurant details based upon the Category ID")
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable("category_id") final String categoryUuid) throws CategoryNotFoundException {
        final List<RestaurantEntity> allRestaurant = restaurantService.getRestaurantByCategoryId(categoryUuid);
        RestaurantListResponse restaurantResponse = Restaurantlist(allRestaurant);
        return new ResponseEntity<RestaurantListResponse>(restaurantResponse, HttpStatus.OK);
    }


    @GetMapping("/restaurant/name/{restaurant_name}")
    @ApiOperation(value="Retrieve restaurant details based upon the Restaurant Name")
    public ResponseEntity<RestaurantListResponse> getRestaurantById(@PathVariable("reastaurant_name") final String reastaurantName) throws RestaurantNotFoundException {
        final List<RestaurantEntity> allRestaurant = restaurantService.getRestaurantsByName(reastaurantName);
        RestaurantListResponse restaurantResponse = Restaurantlist(allRestaurant);
        return new ResponseEntity<RestaurantListResponse>(restaurantResponse, HttpStatus.OK);
    }

    @GetMapping("/restaurant")
    @ApiOperation(value="Retrieve list of all the restaurants")
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(){
        List<RestaurantEntity> restaurantEntityList = restaurantService.getRestaurants();
        RestaurantListResponse restaurantResponse = Restaurantlist(restaurantEntityList);
        return new ResponseEntity<RestaurantListResponse>(restaurantResponse, HttpStatus.OK);
    }


    public RestaurantListResponse Restaurantlist(List<RestaurantEntity> allRestaurant){

        final RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        for (RestaurantEntity restaurantEntity : allRestaurant) {
            final RestaurantList restaurantList = new RestaurantList();
            restaurantList.id(UUID.fromString(restaurantEntity.getUuid()));
            restaurantList.restaurantName(restaurantEntity.getRestaurantName());
            restaurantList.photoURL(restaurantEntity.getPhotoUrl());
            restaurantList.customerRating(restaurantEntity.getCustomerRating());
            restaurantList.averagePrice(restaurantEntity.getAveragePriceForTwo());
            restaurantList.numberCustomersRated(restaurantEntity.getNumberOfCustomersRated());

            final AddressEntity addressEntity = addressService.getAddressById(restaurantEntity.getAddress().getId());
            final RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
            restaurantDetailsResponseAddress.city(addressEntity.getCity());
            restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNumber());
            restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
            restaurantDetailsResponseAddress.pincode(addressEntity.getPinCode());

            final StateEntity stateEntity =addressService.getStateById(restaurantEntity.getAddress().getState().getId());
            final RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
            restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
            restaurantList.setAddress(restaurantDetailsResponseAddress);


            List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            CategoryList categoryList = new CategoryList();
            List<String> arrayList = new ArrayList<>();
            for(CategoryEntity categoryEntity : categoryEntityList){
                //categoryList.categoryName(categoryEntity.getCategoryName());
                //restaurantList.categories(categoryList.getCategoryName());
                categoryList.categoryName(categoryEntity.getCategoryName());
                arrayList.add(categoryList.getCategoryName());
            }

            restaurantList.categories(arrayList.toString());
            restaurantListResponse.addRestaurantsItem(restaurantList);
        }

        return restaurantListResponse;
    }

}
