package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.requestmodal.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(value = "Category Controller: ",description = "end-points for getting categories related information")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Retrieve Category based upon the Category ID")
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable("category_id")final String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = categoryService.categoryByUUID(categoryUuid);

        List<CategoryItemEntity> categoryItemEntityList = categoryService.getCategoryItemListByCategory(categoryEntity);
        CategoryDetailsResponse categoryDetailsResponse=new CategoryDetailsResponse();

        for( CategoryItemEntity categoryItemEntity : categoryItemEntityList){

            CategoryList categoryList =new CategoryList();
            final CategoryEntity category = categoryItemEntity.getCategory();
            categoryList.id(UUID.fromString(category.getUuid()));
            categoryList.categoryName(category.getCategoryName());
            // categoryDetailsResponse.id(UUID.fromString(categoryEntity.getUuid()));
            // categoryDetailsResponse.categoryName(categoryEntity.getCategoryName());

            final ItemEntity itemEntity =categoryItemEntity.getItem();
            ItemList itemList=new ItemList();
            itemList.id(UUID.fromString(itemEntity.getUuid()));
            itemList.itemName(itemEntity.getItemName());
            itemList.price(itemEntity.getPrice());
            itemList.itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType()));

            categoryList.addItemListItem(itemList);
            //categoryDetailsResponse.addItemListItem(itemList);

        }

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }

    @GetMapping("/category")
    @ApiOperation(value = "Retrieve all Categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CategoryListResponse>> getAllCategories(){
        List<CategoryEntity> categoryEntityList=new ArrayList<CategoryEntity>();
        categoryEntityList.addAll(categoryService.getAllCategories());

        List<CategoryListResponse> categoryListResponseList=new ArrayList<>();

        for (CategoryEntity categoryEntity : categoryEntityList) {

            CategoryListResponse categoryListResponse=new CategoryListResponse();
            categoryListResponseList.add(categoryListResponse
                    .categoryName(categoryEntity.getCategoryName())
                    .id(UUID.fromString(categoryEntity.getUuid())));
        }

        return new ResponseEntity<List<CategoryListResponse>>(categoryListResponseList, HttpStatus.OK);
    }


}


