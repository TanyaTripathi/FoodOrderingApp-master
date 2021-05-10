package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(
        name = "address"
)
@NamedQueries({
        @NamedQuery(name = "addressByAddressUuid", query = "select a from AddressEntity a where a.uuid =:uuid"),//returns address by addressUuid
        @NamedQuery(name = "allSavedAddresses", query = "select a from AddressEntity a "),//returns all the address records
        @NamedQuery(name = "addressById", query = "select a from AddressEntity a where a.id =:id")//returns address record for a addressId
})

public class AddressEntity implements Serializable {

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
            name = "FLAT_BUIL_NUMBER"
    )
    //flatBuilNumber can be NULL
    private String flatBuilNumber;

    @Column(
            name = "LOCALITY"
    )
    //locality can be NULL
    private String locality;

    @Column(
            name = "CITY"
    )
    //city can be NULL
    @Size(
            max = 30
    )
    private String city;

    @Column(
            name = "PINCODE"
    )
    //pinCode can be NULL
    @Size(
            max = 30
    )
    private String pinCode;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "STATE_ID"
    )
    private StateEntity state;

    @Column(
            name="ACTIVE"
    )
    private Integer  active;

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

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;

    }
}
