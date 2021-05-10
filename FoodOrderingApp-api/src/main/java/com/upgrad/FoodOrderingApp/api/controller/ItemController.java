package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(value = "Item Controller", description = "endpoint for getting all the items.")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping(value = "/item/restaurant/{restaurant_id}")
    @ApiOperation("Retrieve details for all the Restaurants")
    public ResponseEntity<ItemListResponse> getRestaurants(@PathVariable("restaurant_id") String restaurantId) throws RestaurantNotFoundException {



        return new ResponseEntity<ItemListResponse>(HttpStatus.OK);
    }

}
