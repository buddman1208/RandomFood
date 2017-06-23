package moe.kotohana.randomfood.models

import android.util.Log
import io.realm.RealmObject
import java.io.Serializable

/**
 * Created by Junseok Oh on 2017-06-13.
 */

open class Place(val items: ArrayList<Restaurant>) : Serializable
