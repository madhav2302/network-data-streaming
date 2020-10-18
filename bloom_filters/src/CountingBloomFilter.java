import java.util.*;

public class CountingBloomFilter {

    public static final Random RANDOM = new Random();

    private final int[] counter;
    private final int[] hashHelpers;

    public CountingBloomFilter(int numberOfBits, int numberOfHashes) {
        this.counter = new int[numberOfBits];
        Arrays.fill(this.counter, 0);

        this.hashHelpers = createHashHelpers(numberOfHashes);
    }

    public static void main(String[] args) {
        int numberOfElementsEncoded = 1_000;
        int numberOfElementsRemoved = 500;
        int numberOfElementsAdded = 500;
        int numberOfCounters = 10_000;
        int numberOfHashes = 7;

        CountingBloomFilter bloomFilter = new CountingBloomFilter(numberOfCounters, numberOfHashes);
        List<Integer> original = bloomFilter.randomElements(numberOfElementsEncoded);

        original.forEach(bloomFilter::encode);

        List<Integer> temp = new ArrayList<>(original);
        for (int count = 0; count < numberOfElementsRemoved; count++) {
            bloomFilter.remove(temp.remove(RANDOM.nextInt(temp.size())));
        }

        List<Integer> elementsAdded = bloomFilter.randomElements(numberOfElementsAdded, new HashSet<>(original));

        elementsAdded.forEach(f -> {
            if (!bloomFilter.lookUp(f)) bloomFilter.encode(f);
        });

        System.out.println(original.stream().filter(bloomFilter::lookUp).count());
    }

    public void remove(int element) {
        for (int i = 0; i < hashHelpers.length; i++) {
            counter[hashValue(i, element)]--;
        }
    }

    private void encode(int element) {
        for (int i = 0; i < hashHelpers.length; i++) {
            counter[hashValue(i, element)]++;
        }
    }

    private boolean lookUp(int element) {
        for (int i = 0; i < hashHelpers.length; i++) {
            if (counter[hashValue(i, element)] < 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates some random values between 0 and {@link #counter} length * 10 to be used for XOR with hash for hash functions
     */
    private int[] createHashHelpers(int numberOfHashFunctions) {
        int[] hashValues = new int[numberOfHashFunctions];

        Set<Integer> used = new HashSet<>();
        for (int index = 0; index < numberOfHashFunctions; index++) {
            hashValues[index] = nextRandomInt(used);
            used.add(hashValues[index]);
        }

        return hashValues;
    }

    /**
     * Returns a random value other than already present in used.
     */
    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt());

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    /**
     * Index for element for hash table.
     */
    public int hashValue(int index, int element) {
        return Math.abs(element ^ hashHelpers[index]) % counter.length;
    }

    protected List<Integer> randomElements(int numberOfElements) {
        return randomElements(numberOfElements, new HashSet<>());
    }

    /**
     * Generates numberOfElements distinct random elements b/w 0 and {@link #counter} length * 10
     */
    protected List<Integer> randomElements(int numberOfElements, Set<Integer> usedElements) {
        List<Integer> elements = new ArrayList<>();
        for (int count = 0; count < numberOfElements; count++) {
            int randomElement = nextRandomInt(usedElements);
            elements.add(randomElement);
            usedElements.add(randomElement);
        }
        return elements;
    }
}
