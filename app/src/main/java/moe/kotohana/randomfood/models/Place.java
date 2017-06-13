package moe.kotohana.randomfood.models;

import java.io.Serializable;

/**
 * Created by Junseok Oh on 2017-06-13.
 */

public class Place implements Serializable {
    private String name;

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
