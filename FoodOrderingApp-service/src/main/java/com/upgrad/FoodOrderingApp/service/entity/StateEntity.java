package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(
        name = "state"
)
@NamedQueries({
        @NamedQuery(name = "allStates", query = "select s from StateEntity s"),
        @NamedQuery(name = "stateByStateUuid",query="select s from StateEntity s where s.uuid=:uuid"),
        @NamedQuery(name = "getStateById", query = "select s from StateEntity s where s.id=:id")
})

public class StateEntity implements Serializable {

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
            name = "STATE_NAME"
    )
    //stateName can be NULL
    @Size(
            max = 30
    )
    private String stateName;


    public StateEntity(){

    }

    public StateEntity(String uuid,String stateName){
        this.uuid = uuid;
        this.stateName = stateName;
    }

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
