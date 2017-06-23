package moe.kotohana.randomfood.models;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Junseok Oh on 2017-06-23.
 */

public class History extends RealmObject {
    public RealmList<Restaurant> historyList;

    public RealmList<Restaurant> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(RealmList<Restaurant> historyList) {
        this.historyList = historyList;
    }
}
