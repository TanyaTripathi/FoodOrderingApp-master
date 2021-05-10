package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.requestmodal.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "Payment Controller: ",description = "end-points for payment details")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

        @GetMapping("/payment")
        @ApiOperation(value="Retrieve payment methods for the application")
        public ResponseEntity<PaymentListResponse> getPaymentResponse(){
            List<PaymentEntity> listPaymentEntity = paymentService.getAllPaymentMethods();

            PaymentListResponse paymentListResponse = new PaymentListResponse();

            for(PaymentEntity payment : listPaymentEntity){
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setId(UUID.fromString(payment.getUuid()));
                paymentResponse.setPaymentName(payment.getPaymentName());
                paymentListResponse.addPaymentMethodsItem(paymentResponse);
            }

            return new ResponseEntity<>(paymentListResponse,HttpStatus.OK);
        }
}
