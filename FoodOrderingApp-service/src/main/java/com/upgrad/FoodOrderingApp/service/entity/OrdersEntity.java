package com.upgrad.FoodOrderingApp.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "orders"
)
/*@NamedQueries({@NamedQuery(
        name = "customerByContactNumber",
        query = "select c from CustomerEntity c where c.contactNumber = :contactNumber"
)
})*/

public class OrdersEntity implements Serializable {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(
            name = "UUID"
    )
    @Size(
            max = 200
    )
    private String uuid;

    @Column(
            name="BILL"
    )
    @NotNull

    private BigDecimal bill;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "COUPON_ID"
    )
    private CouponEntity coupon;

    @Column(
            name="DISCOUNT"
    )

    //discount can be NULL
    private BigDecimal discount;

    @Column(name="DATE")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "PAYMENT_ID"
    )
    private PaymentEntity payment;


    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "CUSTOMER_ID"
    )
    private CustomerEntity customer;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "ADDRESS_ID"
    )
    private AddressEntity address;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "RESTAURANT_ID"
    )
    private RestaurantEntity restaurant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
