package moe.kotohana.randomfood.models;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Junseok Oh on 2017-06-23.
 */

public class Restaurant extends RealmObject implements Serializable {
    private String title, link, category, description, telephone, address, roadAddress, mapx, mapy;
    private int type = 0;

    public Restaurant() {
    }

    public Restaurant(String title, String link, String category, String description, String telephone, String address, String roadAddress, String mapx, String mapy) {
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

    public String getTitle() {
        return title.replace("<b>", "").replace("</b>", "").replace("&amp;", "");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        if (!roadAddress.isEmpty()) return roadAddress;
        else return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public Restaurant setRealType(int type) {
        this.type = type;
        return this;
    }
}
