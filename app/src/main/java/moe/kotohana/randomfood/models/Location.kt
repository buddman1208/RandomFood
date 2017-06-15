package moe.kotohana.randomfood.models

/**
 * Created by Junseok Oh on 2017-06-15.
 */
class Location(var location: LocationDetail) {
    fun getRequiredAddress(): String {
        return location.sigugun + " " + location.dongmyun
    }
}
open class LocationDetail(var country: String, var sido: String, var sigugun: String, var dongmyun: String)
