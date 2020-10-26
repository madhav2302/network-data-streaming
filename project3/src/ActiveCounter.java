import java.util.Random;

public class ActiveCounter {
    // We use it to check 2^16 limit for counter
    public static final int OVERFLOW = (int) Math.pow(2, 16);
    public Random random = new Random();

    int n = 0, e = 0;

    public static void main(String[] args) {
        ActiveCounter activeCounter = new ActiveCounter();
        activeCounter.run(1_000_000);

        System.out.println(activeCounter.n * Math.pow(2, activeCounter.e));
    }

    /**
     * Run active counter n times
     */
    public void run(int times) {
        for (int i = 0; i < times; i++) {
            // Check probability
            if (random.nextInt((int) Math.pow(2, e)) == 0) {
                activeIncrease();
            }
        }
    }

    /**
     * Increase n, if overflow, then increase e and right shift n by 1
     */
    public void activeIncrease() {
        n++;

        if (n == OVERFLOW) {
            n = n >> 1;
            e++;
        }
    }

    @Override
    public String toString() {
        return "ActiveCounter{" +
                "n=" + n +
                ", e=" + e +
                ", value=" + n * ((int) Math.pow(2, e)) +
                '}';
    }
}
