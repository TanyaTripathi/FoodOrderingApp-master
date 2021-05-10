package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.requestmodal.OrderListCustomer;
import com.upgrad.FoodOrderingApp.api.requestmodal.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.requestmodal.SaveOrderResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "Order Controller: ",description = "end-points for orders related details")
public class OrderController {


    @GetMapping("/order")
    @ApiOperation(value = "Get Past Order details for user")
    public ResponseEntity<OrderListCustomer> getOrdersByCustomer(){

        return new ResponseEntity<OrderListCustomer>(HttpStatus.OK);
    }


    @PostMapping("/order")
    @ApiOperation(value = "Save order details for CustomerEntity")
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestBody SaveOrderRequest saveOrderRequest){

        return new ResponseEntity<SaveOrderResponse>(HttpStatus.CREATED);
    }

    @GetMapping("/order/coupon/{coupon_name}")
    @ApiOperation(value = "Retrieve coupon details by Coupon Name")
    public ResponseEntity<CouponDetailsResponse> getCouponDetails(@PathVariable("coupon-name") String couponName){

        return new ResponseEntity<CouponDetailsResponse>(HttpStatus.OK);
    }

}
