package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(value = "CustomerEntity Controller",description = "end-points for CustomerEntity Functions: Signup/Login/Logout/Password")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/customer/signup",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value="Registration for new customer")
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup( @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        final CustomerEntity customerEntity=new CustomerEntity();

        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setSalt("1234abc");
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        final CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);

        SignupCustomerResponse customerResponse = new SignupCustomerResponse()
                .id(createdCustomerEntity.getUuid())
                .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }


    @PostMapping(path="/customer/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value="Authentication for an existing customer")
    public ResponseEntity<LoginResponse> login(@RequestHeader("authentication") final String authentication) throws AuthenticationFailedException {

        byte[] decode = Base64.getDecoder().decode(authentication.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        final CustomerAuthEntity customerAuthToken = customerService.authenticate(decodedArray[0], decodedArray[1]);

        CustomerEntity customerEntity = customerAuthToken.getCustomer();

        LoginResponse loginResponse = new LoginResponse()
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .emailAddress(customerEntity.getEmail())
                .contactNumber(customerEntity.getContactNumber())
                .id(customerEntity.getUuid())
                .message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);
        headers.add("access-token", customerAuthToken.getAccessToken());

        return new ResponseEntity<LoginResponse>(  loginResponse, headers, HttpStatus.OK);
    }

    @PostMapping(path="/customer/logout",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value="Sign-out for an already logged-in customer")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String accessToken)throws AuthorizationFailedException {

        String [] bearerToken = accessToken.split("Bearer ");
        final CustomerAuthEntity logout = customerService.logout(bearerToken[1]);

        LogoutResponse logoutResponse=new LogoutResponse()
                .id(logout.getUuid())
                .message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse,HttpStatus.OK);
    }

    @PutMapping(path="/customer",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value="Update the registered customer information")
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest,
                                                                 @RequestHeader("authorization") final String accessToken) throws UpdateCustomerException,AuthorizationFailedException {

        String [] bearerToken = accessToken.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());

        final CustomerEntity updatedCustomerEntity=customerService.updateCustomer(customerEntity);

        UpdateCustomerResponse updateCustomerResponse=new UpdateCustomerResponse()
                .firstName(updatedCustomerEntity.getFirstName())
                .lastName(updatedCustomerEntity.getLastName())
                .id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse,HttpStatus.OK);
    }

    @PutMapping(path="/customer/password",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value="Update the password for an existing CustomerEntity")
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestBody(required = false)final UpdatePasswordRequest updatePasswordRequest,
                                                                 @RequestHeader("authorization") final String accessToken) throws UpdateCustomerException,AuthorizationFailedException {

        String [] bearerToken = accessToken.split("Bearer ");
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        final CustomerEntity updatedCustomerEntity=customerService.updateCustomerPassword(updatePasswordRequest.getOldPassword(),updatePasswordRequest.getNewPassword(),customerEntity);

        UpdatePasswordResponse updatePasswordResponse=new UpdatePasswordResponse()
                .id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse,HttpStatus.OK);
    }

}
