package moe.kotohana.randomfood.models;

import java.io.Serializable;

/**
 * Created by Junseok Oh on 2017-06-23.
 */

public class RestaurantContent implements Serializable{
    private String title, link, category, description, telephone, address, roadAddress, mapx, mapy;
    private int type = 0;

    public RestaurantContent() {
    }

    public RestaurantContent(String title, String link, String category, String description, String telephone, String address, String roadAddress, String mapx, String mapy) {
        this.title = title;
        this.link = link;
        this.category = category;
        this.description = description;
        this.telephone = telephone;
        this.address = address;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
    }

    public RestaurantContent convertToThis(Restaurant r) {
        this.title = r.getTitle();
        this.link = r.getLink();
        this.category = r.getCategory();
        this.description = r.getRealDescription();
        this.telephone = r.getTelephone();
        this.address = r.getAddress();
        this.roadAddress = r.getRoadAddress();
        this.mapx = r.getMapx();
        this.mapy = r.getMapy();
        this.type = r.getType();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAddress() {
        return address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getMapx() {
        return mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public int getType() {
        return type;
    }
}
