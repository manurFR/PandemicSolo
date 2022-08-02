package pandemic.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtil implements Serializable {
    private static final long serialVersionUID = -2711763798006275010L;

    private static final int TIMES_RESHUFFLE = 20;

    private final Random random;

    public RandomUtil() {
        random = new Random();
    }

    public void shuffleInPlace(List<?> list) {
        for (int i = 0; i < TIMES_RESHUFFLE; i++) {
            Collections.shuffle(list, random);
        }
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
