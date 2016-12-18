package br.com.hisao.restaurantchallenge.Model;

/**
 * Created by viniciushisao
 */


public class Restaurant {

    public Restaurant(String id, String name, String address, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "id:" + this.id + " name:" + this.name + " address:" + this.address + " imageUrl:" + this.imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String id;
    private String name;
    private String address;
    private String imageUrl;

}

