package moe.kotohana.randomfood.models

/**
 * Created by Junseok Oh on 2017-06-15.
 */
open class Location(var result : LocationDetails)
open class LocationDetails(var items: ArrayList<Items>)
open class Items(var addrdetail : AddrDetail)
open class AddrDetail(var country: String, var sido: String, var sigugun: String, var dongmyun: String)
