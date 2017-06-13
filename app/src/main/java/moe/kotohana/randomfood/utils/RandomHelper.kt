package moe.kotohana.randomfood.utils

import java.util.Random

/**
 * Created by Junseok Oh on 2017-06-13.
 */

class RandomHelper {
    companion object {
        fun getRandomNumber(range: Int): Int {
            return Random().nextInt(range + 1)
        }

        fun getRandomNumber(start: Int, range: Int): Int {
            return Random().nextInt(range + start)
        }
    }

}
