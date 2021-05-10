package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        //validates email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        //matches 10-digit numbers only
        String contactNumberRegex = "^[0-9]{10}$";

        //Throws relevant exceptions if the contact number provided already exists in the current database,
        if (customerDao.getCustomerByContactNumber(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");

            //If any field other than last name is empty,
        } else if (customerEntity.getFirstName().isEmpty() || customerEntity.getContactNumber().isEmpty() || customerEntity.getEmail().isEmpty() || customerEntity.getPassword().isEmpty()) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");

            //If the email ID provided by the customer is not in the correct format, i.e., not in the format of xxx@xx.xx
        } else if (!pattern.matcher(customerEntity.getEmail()).matches()) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");

            //If the contact number provided by the customer is not in correct format, i.e., it does not contain only numbers and has more or less than 10 digits,
        } else if (!customerEntity.getContactNumber().matches(contactNumberRegex)) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");

            //If the password provided by the customer is weak, i.e., it doesn’t have at least eight characters and does not contain at least one digit, one uppercase letter,
            // and one of the following characters [#@$%&*!^]
        } else if (customerEntity.getPassword().length() < 8 || !customerEntity.getPassword().matches("(?=.*[0-9]).*") || !customerEntity.getPassword().matches("(?=.*[A-Z]).*") || !customerEntity.getPassword().matches("(?=.*[~!@#$%^&*()_-]).*")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password");

            //Else, save the customer information in the database.
        } else {
            String password = customerEntity.getPassword();
            if (password == null) {
                customerEntity.setPassword("Default@123");
            }
            String[] encryptedText = this.passwordCryptographyProvider.encrypt(customerEntity.getPassword());
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return this.customerDao.createCustomer(customerEntity);
        }
    }

    //Authenticates a customer based on contactNumber(as username) and password when the customer signs in for the first time and throw exception when certain conditions not met
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(final String contactNumber, final String password) throws AuthenticationFailedException {
        //matches 10-digit numbers only
        String contactNumberRegex = "^[0-9]{10}$";

        if(contactNumber.isEmpty() || password.isEmpty() || !contactNumber.matches(contactNumberRegex) || password.length() <8 || !password.matches("(?=.*[0-9]).*") || !password.matches("(?=.*[A-Z]).*") || !password.matches("(?=.*[~!@#$%^&*()_-]).*")) {
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }
        //If the contact number provided by the customer does not exist,
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        //If the password matches, save the customer login information in the database
        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
            customerAuthToken.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
            customerAuthToken.setLoginAt(now);
            customerAuthToken.setExpiresAt(expiresAt);
            customerAuthToken.setUuid(customerEntity.getUuid());
            customerDao.createAuthToken(customerAuthToken);
            customerDao.updateCustomer(customerEntity);
            return customerAuthToken;
        } else {
            //If the password provided by the customer does not match the password in the existing database,
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    //This endpoint is used to logout from the  Application
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(accessToken);
        //If the access token provided by the customer does not exist in the database
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            //If the access token provided by the customer exists in the database, but the customer has already logged out
        } else if (customerAuthEntity != null && customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            //If the access token provided by the customer exists in the database, but the session has expired
        } else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            final ZonedDateTime now = ZonedDateTime.now();
            customerAuthEntity.setLogoutAt(now);
            return customerAuthEntity;
        }
    }

    //getCustomer method is used to perform Bearer authorization
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomer(final String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(accessToken);
        //If the access token provided by the customer does not exist in the database
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            //If the access token provided by the customer exists in the database, but the customer has already logged out
        } else if (customerAuthEntity != null && customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            //If the access token provided by the customer exists in the database, but the session has expired
        } else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        else {
            return customerAuthEntity.getCustomer();
        }
    }

    //updateCustomer method is used to update a customer's firstname and/or lastname
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) throws  UpdateCustomerException {
        if (customerEntity.getFirstName().isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        } else {
            final CustomerEntity updatedCustomerEntity = new CustomerEntity();
            updatedCustomerEntity.setFirstName(customerEntity.getFirstName());
            if(!customerEntity.getLastName().isEmpty()) {
                updatedCustomerEntity.setLastName(customerEntity.getLastName());
            }
            updatedCustomerEntity.setUuid(customerEntity.getUuid());
            return updatedCustomerEntity;
        }
    }

    //updateCustomerPassword updates password as given by the Customer in newPassword field
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(final String oldPassword,final String newPassword, final CustomerEntity customerEntity) throws  UpdateCustomerException {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        } else if(newPassword.length() < 8 || !newPassword.matches("(?=.*[0-9]).*") || !newPassword.matches("(?=.*[A-Z]).*")|| !newPassword.matches("(?=.*[~!@#$%^&*()_-]).*")) {
            throw new UpdateCustomerException("UCR-001","Weak password!");
        } else if(!passwordCryptographyProvider.encrypt(oldPassword,customerEntity.getSalt()).equals(customerEntity.getPassword()) ){
            throw new UpdateCustomerException("UCR-004","Incorrect old password!");
        } else {
            String[] encryptedText = this.passwordCryptographyProvider.encrypt(newPassword);
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return customerEntity;
        }
    }
}
