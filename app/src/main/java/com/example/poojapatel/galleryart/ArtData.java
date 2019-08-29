package com.example.poojapatel.galleryart;

public class ArtData {
    String name;
    int price;
    int year;
    String image;
    String description, status;
    int artistId, categoryId, artId, shoppingCart_id, customercustomer_id;



    public ArtData(String name, int price, int year, String image, String description, int artistId, int categoryId, int artId) {
        this.name = name;
        this.price = price;
        this.year = year;
        this.image = image;
        this.description = description;
        this.artistId = artistId;
        this.categoryId = categoryId;
        this.artId = artId;
    }

    public ArtData(String name, int price, int year, String image, String description, int artistId, int categoryId, int artId,
                   int shoppingCart_id, int customercustomer_id, String status) {
        this.name = name;
        this.price = price;
        this.year = year;
        this.image = image;
        this.description = description;
        this.artistId = artistId;
        this.categoryId = categoryId;
        this.artId = artId;
        this.shoppingCart_id = shoppingCart_id;
        this.customercustomer_id = customercustomer_id;
        this.status = status;



    }
}
