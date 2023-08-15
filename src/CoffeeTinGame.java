import java.util.Arrays;
import java.util.Random;

/**
 * @overview A program that simulates the coffee tin game on a tin of beans and displays the results.
 *
 */
public class CoffeeTinGame {
    private static final char GREEN = 'G';
    private static final char BLUE = 'B';
    private static final char REMOVED = '-';
    private static final char NULL = '\u0000';

    private static final char[] BeansBag = new char[60];
    static {
        for (int i = 0; i < BeansBag.length; i++) {
            if (i < BeansBag.length / 3) {
                BeansBag[i] = BLUE;
            } else if (i < 2 * BeansBag.length / 3) {
                BeansBag[i] = GREEN;
            } else {
                BeansBag[i] = REMOVED;
            }
        }
    }

    /**
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects Simulates the coffee tin game to determine the color of the last bean.
     *          The method modifies the tin array according to the game rules and returns the color of the last bean.
     */
    private static char tinGame(char[] tin) {
        while (hasAtLeastTwoBeans(tin)) {
            char[] twoBeans = takeTwo(tin);
            updateTin(tin, twoBeans[0], twoBeans[1]);
        }
        return anyBean(tin);
    }

    /**
     * @requires tin is not null
     * @effects Checks if there are at least two beans in the tin.
     * @return true if there are at least two beans in the tin, false otherwise.
     */
    private static boolean hasAtLeastTwoBeans(char[] tin) {
        int count = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                count++;
                if (count >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @requires tin is not null /\ tin.length >= 2
     * @modifies tin
     * @effects Takes two beans from the tin and removes them.
     * @return An array containing the two beans removed from the tin.
     */
    private static char[] takeTwo(char[] tin) {
        char first = takeOne(tin);
        char second = takeOne(tin);
        return new char[]{first, second};
    }

    /**
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects Takes one bean from the tin and removes it.
     * @return The bean that was removed.
     */
    private static char takeOne(char[] tin) {
        int randomIndex = randInt(tin.length);
        while (tin[randomIndex] == REMOVED) {
            randomIndex = randInt(tin.length);
        }
        char bean = tin[randomIndex];
        tin[randomIndex] = REMOVED;
        return bean;
    }

    /**
     * @requires tin is not null /\ bean is either 'G' or 'B'
     * @modifies tin
     * @effects Places a bean in a vacant position in the tin.
     */
    private static void putIn(char[] tin, char bean) {
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == REMOVED) {
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @requires tin is not null
     * @effects Returns any bean that is not removed from the tin.
     * @return A bean that is not removed, or NULL if no beans are left.
     */
    private static char anyBean(char[] tin) {
        for (char bean : tin) {
            if (bean != REMOVED) {
                return bean;
            }
        }
        return NULL;
    }

    /**
     * @requires n > 0
     * @effects Generates a random integer between 0 (inclusive) and n (exclusive).
     * @return A random integer between 0 (inclusive) and n (exclusive).
     */
    public static int randInt(int n) {
        return new Random().nextInt(n);
    }

    /**
     * @requires beansBag is not null /\ beansBag has at least one bean of the specified type
     * @modifies beansBag
     * @effects Removes a random bean of the specified type from beansBag and returns it.
     */
    public static char getBean(char[] beansBag, char beanType) {
        char selectedBean = takeOne(beansBag);
        while (selectedBean != beanType) {
            putIn(beansBag, selectedBean);
            selectedBean = takeOne(beansBag);
        }
        return selectedBean;
    }

    /**
     * @requires tin is not null /\ tin has vacant positions for new beans
     * @modifies tin
     * @effects Takes two beans and updates the tin based on the coffee tin game rules.
     */
    public static void updateTin(char[] tin, char b1, char b2) {
        char newBean;
        if (b1 == b2) {
            newBean = getBean(BeansBag, BLUE);
        } else {
            newBean = getBean(BeansBag, GREEN);
        }
        putIn(tin, newBean);
    }

    public static void main(String[] args) {
        char[][] tins = {
                {BLUE, BLUE, BLUE, GREEN, GREEN},
                {BLUE, BLUE, BLUE, GREEN, GREEN, GREEN},
                {GREEN},
                {BLUE},
                {BLUE, GREEN}
        };

        for (char[] tin : tins) {
            int greens = countBeans(tin, GREEN);
            char last = (greens % 2 == 1) ? GREEN : BLUE;

            System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));

            char lastBean = tinGame(tin);

            System.out.printf("tin after: %s %n", Arrays.toString(tin));

            if (lastBean == last) {
                System.out.printf("last bean: %c%n", lastBean);
            } else {
                System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
            }
        }
    }

    /**
     * @requires tin is not null /\ beanType is 'G' or 'B'
     * @effects Counts the number of beans of the specified type in the tin.
     * @return The count of beans of the specified type in the tin.
     */
    private static int countBeans(char[] tin, char beanType) {
        int count = 0;
        for (char bean : tin) {
            if (bean == beanType) {
                count++;
            }
        }
        return count;
    }
}
