package moe.kotohana.randomfood.utils;

import java.util.Random;

/**
 * Created by Junseok Oh on 2017-06-13.
 */

public class RandomHelper {
    public static int getRandomNumber(int range) {
        return new Random().nextInt(range + 1);
    }
    public static int getRandomNumber(int start, int range) {
        return new Random().nextInt(range + start);
    }
}
