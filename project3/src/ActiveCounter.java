public class ActiveCounter {
    int n = 0;
    int e = 0;

    // We use it to check 2^16 limit for counter
    public static final int MAX = Integer.valueOf("1111111111111111", 2);

    public static void main(String[] args) {
        ActiveCounter activeCounter = new ActiveCounter();
        activeCounter.run(1_000_000);

        System.out.println(activeCounter.n * Math.pow(2, activeCounter.e));
    }

    public void run(int times) {
        // We use it for probability, basically increase n, once in 2^e
        int probability = (int) Math.pow(2, e);

        for (int i = 0; i < times; i++) {
            probability--;

            // If we have to 0, it means we have to increase n
            if (probability == 0) {
                n++;
            }

            // Check overflow
            if (n > MAX) {
                n = n >> 1;
                e++;
            }

            // Reset probability value with 2^e
            if (probability == 0) {
                probability = (int) Math.pow(2, e);
            }

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
