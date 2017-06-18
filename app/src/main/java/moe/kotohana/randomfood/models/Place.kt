package moe.kotohana.randomfood.models

import android.util.Log
import java.io.Serializable

/**
 * Created by Junseok Oh on 2017-06-13.
 */

open class Place(val items: ArrayList<Restaurant>) : Serializable

class Restaurant {
    var title: String = ""
    var link: String = ""
    var category: String = ""
    var description: String = ""
    var telephone: String = ""
    var address: String = ""
    var roadAddress: String = ""
    var mapx: String = ""
    var mapy: String = ""

    constructor(title: String, link: String, category: String, description: String, telephone: String, address: String, roadAddress: String, mapx: String, mapy: String) {
        this.title = title
        this.link = link
        this.category = category
        this.description = description
        this.telephone = telephone
        this.address = address
        this.roadAddress = roadAddress
        this.mapx = mapx
        this.mapy = mapy
    }

    fun getRealTitle() : String {
        return title.replace("<b>", "").replace("</b>", "").replace("&amp;", "")
    }

    fun getRealAddress() : String{
        return if (roadAddress.isNotEmpty()) roadAddress else address
    }

}