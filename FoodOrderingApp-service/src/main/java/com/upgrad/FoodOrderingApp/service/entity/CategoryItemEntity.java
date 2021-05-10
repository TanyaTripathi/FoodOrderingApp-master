package com.upgrad.FoodOrderingApp.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "category_item")
@NamedQueries({
        @NamedQuery( name = "categoryItemListByCategory", query = "select ci from CategoryItemEntity ci where ci.category = :category")
})

public class CategoryItemEntity implements Serializable {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "ITEM_ID"
    )
    private ItemEntity item;

    @ManyToOne
    @OnDelete(
            action = OnDeleteAction.CASCADE
    )
    @JoinColumn(
            name = "CATEGORY_ID"
    )
    private CategoryEntity category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
