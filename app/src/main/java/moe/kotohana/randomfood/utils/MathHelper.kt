package moe.kotohana.randomfood.utils

import android.content.Intent
import java.util.Random

/**
 * Created by Junseok Oh on 2017-06-13.
 */

class MathHelper {
    companion object {
        fun getRandomNumber(range: Int): Int {
            return Random().nextInt(range + 1)
        }

        fun getRandomNumber(start: Int, range: Int): Int {
            return Random().nextInt(range + start)
        }

        fun calculatePercent(vararg items: Int): ArrayList<Float> {
            val total: Int = items.sum()
            val array: ArrayList<Float> = ArrayList()
            items.mapTo(array) { ((it.toFloat() / total) * 100)}
            return array
        }
    }

}
