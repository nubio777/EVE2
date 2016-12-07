package coe.wuti.var;

/**
 * Created by jesus on 2/1/16.
 */
public class RandomStrings {
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static String get(int length) {
        return get (length, length) ;
    }

    public static String get(int minLength, int maxLength) {
        int num = randomInt(minLength, maxLength);
        byte b[] = new byte[num];
        for (int i = 0; i < num; i++)
            b[i] = (byte) randomInt('a', 'z');
        return new String(b);
    }
}
