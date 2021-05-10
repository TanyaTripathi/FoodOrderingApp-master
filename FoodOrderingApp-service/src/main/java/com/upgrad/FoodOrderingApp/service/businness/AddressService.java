package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class AddressService {

    //Respective Data access object has been autowired to access the method defined in respective Dao
    @Autowired
    private CustomerAddressDao customerAddressDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private StateDao stateDao;
    //Throws exception,
    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(final String StateUuid) throws AddressNotFoundException,SaveAddressException {
        StateEntity stateEntity = stateDao.getStateByStateUuid(StateUuid);
        //If StateUuid is empty,
        if(StateUuid.isEmpty()){
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
        //If the state uuid entered does not exist in the database,
        if(stateEntity == null){
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        } else {
            return stateEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateById(final long id)  {
        return  stateDao.getStateById(id);

    }

    //Throws exception,
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity) throws SaveAddressException {

        String pinCodeRegex = "^[0-9]{6}$";
        //If any field is empty,
        if (addressEntity.getFlatBuilNumber().isEmpty() || addressEntity.getLocality().isEmpty() || addressEntity.getCity().isEmpty() || addressEntity.getPinCode().isEmpty() || addressEntity.getUuid().isEmpty()) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
            //If the pincode entered is invalid (i.e it does not include only numbers or its size is not six),
        } else if (!addressEntity.getPinCode().matches(pinCodeRegex)) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        } else {
            return addressDao.createAddress(addressEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity createCustomerAddress(final CustomerAddressEntity customerAddressEntity) {
        return addressDao.createCustomerAddress(customerAddressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates() { return stateDao.getAllStates(); }
    //Throws exception,
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByAddressUuid(final String addressUuid) throws AddressNotFoundException {
        AddressEntity addressEntity=addressDao.getAddressByAddressUuid(addressUuid);
        //If address id field is empty,
        if(addressUuid.isEmpty()) {
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }
        //If address id entered is incorrect,
        if(addressEntity == null ) {
            throw new AddressNotFoundException("ANF-003","No address by this id");
        } else {
            return addressEntity;
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressById(final long addressId)  {
        return  addressDao.getAddressById(addressId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity getCustomerAddressByAddress(final AddressEntity addressEntity) {
        return customerAddressDao.getCustomerAddressByAddressId(addressEntity);
    }
    //If the access token provided by the customer exists in the database, but the user who has logged in is not the same user who has created the address throw exception
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAddress(AddressEntity addressEntity,CustomerEntity signedcustomerEntity, CustomerEntity ownerofAddressEntity) throws AuthorizationFailedException {
        if(signedcustomerEntity.getUuid() != ownerofAddressEntity.getUuid()) {
            throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
        } else {
            return addressDao.deleteAddress(addressEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CustomerAddressEntity> getAllCustomerAddressByCustomerId(final CustomerEntity customerEntity) {
        return customerAddressDao.getCustomerAddressesListByCustomerId(customerEntity);
    }
}
