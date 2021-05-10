package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(
        name = "category"
)
@NamedQueries({
        @NamedQuery(name = "getAllCategoryById" , query = "select c from CategoryEntity c  " +
                "INNER JOIN RestaurantCategoryEntity rc on c.id = rc.category " +
                "INNER JOIN RestaurantEntity r on rc.restaurant = r.id Where r.uuid =:uuid order by c.categoryName"),

        @NamedQuery( name = "allCategories", query = "select ca from CategoryEntity ca order by ca.categoryName asc "),
        @NamedQuery( name = "categoryByUUID", query = "select ca from CategoryEntity ca where ca.uuid = :uuid ")
})
public class CategoryEntity implements Serializable {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    //uuid must be UNIQUE & NOTNULL
    @Column(
            name = "UUID"
    )
    @Size(
            max = 200
    )
    private String uuid;

    @Column(
            name = "CATEGORY_NAME"
    )
    //categoryName can be NULL
    private String categoryName;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
