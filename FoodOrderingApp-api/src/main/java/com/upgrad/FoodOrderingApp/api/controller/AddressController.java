package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.requestmodal.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(value = "Address Controller",description = "end-points for address details related information")
public class AddressController {


    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/states",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Retrieve list of states")
    public ResponseEntity<StatesListResponse> getAllStates(){

        List<StateEntity> stateEntityList=new ArrayList<>();
        stateEntityList.addAll(addressService.getAllStates());
        StatesListResponse statesListResponse=new StatesListResponse();

        for (StateEntity stateEntity : stateEntityList) {
            StatesList statesList =new StatesList();
            statesList.setId(UUID.fromString(stateEntity.getUuid()));
            statesList.setStateName(stateEntity.getStateName());
            statesListResponse.addStatesItem(statesList);
        }

        return new ResponseEntity<StatesListResponse>(statesListResponse,HttpStatus.OK);
    }

    @DeleteMapping("/address/{address_id}")
    @ApiOperation(value = "Delete the address corresponding to specified Address ID")
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable("address_id") final String addressUuid,
                                                               @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, AddressNotFoundException {

        String [] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity signedinCustomerEntity = customerService.getCustomer(bearerToken[1]);
        final AddressEntity addressEntityToDelete=addressService.getAddressByAddressUuid(addressUuid);
        final CustomerAddressEntity customerAddressEntity=addressService.getCustomerAddressByAddress(addressEntityToDelete);
        final CustomerEntity ownerofAddressEntity=customerAddressEntity.getCustomer();
        final String Uuid = addressService.deleteAddress(addressEntityToDelete,signedinCustomerEntity,ownerofAddressEntity);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(Uuid))
                .status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }


    @GetMapping(value = "/address/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Retrieve Address Details for all the Customers")
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException  {

        String [] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        final List<CustomerAddressEntity> customerAddressesListByCustomerId = addressService.getAllCustomerAddressByCustomerId(customerEntity);


        AddressListResponse addressListResponse=new AddressListResponse();

        for( CustomerAddressEntity customerAddressEntity : customerAddressesListByCustomerId){
            AddressList addressList =new AddressList();
            AddressEntity addressEntity = customerAddressEntity.getAddress();
            addressList.setId(UUID.fromString(addressEntity.getUuid()));
            addressList.setFlatBuildingName(addressEntity.getFlatBuilNumber());
            addressList.setLocality(addressEntity.getLocality());
            addressList.setPincode(addressEntity.getPinCode());
            addressList.setCity(addressEntity.getCity());

            StateEntity stateEntity =addressEntity.getState();
            AddressListState addressListState=new AddressListState();
            addressListState.setId(UUID.fromString(stateEntity.getUuid()));
            addressListState.setStateName(stateEntity.getStateName());

            addressList.state(addressListState);

            addressListResponse.addAddressesItem(addressList);

        }

        return new ResponseEntity<>(addressListResponse,HttpStatus.OK);

    }

    @PostMapping(value = "/address",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Save Address Details for specified CustomerEntity")
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest,
                                                           @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        String [] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        final StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        final AddressEntity addressEntity= new AddressEntity();

        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPinCode(saveAddressRequest.getPincode());
        addressEntity.setState(stateEntity);
        addressEntity.setActive(1);

        final AddressEntity savedAddressEntity = addressService.saveAddress(addressEntity);

        final CustomerAddressEntity customerAddressEntity=new CustomerAddressEntity();
        customerAddressEntity.setAddress(savedAddressEntity);
        customerAddressEntity.setCustomer(customerEntity);
        addressService.createCustomerAddress(customerAddressEntity);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(savedAddressEntity.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);

    }


}


