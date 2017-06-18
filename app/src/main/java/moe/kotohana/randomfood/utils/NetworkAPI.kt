package moe.kotohana.randomfood.utils

import moe.kotohana.randomfood.models.Location
import moe.kotohana.randomfood.models.Place
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Sunrin on 2017-05-29.
 */

interface NetworkAPI {
    @GET("v1/map/reversegeocode")
    fun getAddressByGeocode(@Query("query") query: String): Call<Location>

    @GET("v1/search/local.json")
    fun getRestaurant(@Query("query") query : String): Call<Place>
}
