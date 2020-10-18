import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BloomFilter {
    public static final Random RANDOM = new Random();

    private final int[] filter;
    private final int[] hashHelpers;

    public BloomFilter(int numberOfBits, int numberOfHashes) {
        this.filter = new int[numberOfBits];
        Arrays.fill(this.filter, 0);
        this.hashHelpers = createHashHelpers(numberOfHashes);
    }

    public static void main(String[] args) {
        int numberOfElements = 1_000;
        int numberOfBits = 10_000;
        int numberOfHashes = 7;

        BloomFilter bloomFilter = new BloomFilter(numberOfBits, numberOfHashes);

        Set<Integer> elementsA = bloomFilter.randomElements(numberOfElements);
        elementsA.forEach(f -> {
            if (!bloomFilter.lookUp(f)) bloomFilter.encode(f);
        });
        long countA = elementsA.stream().filter(bloomFilter::lookUp).count();

        Set<Integer> elementsB = bloomFilter.randomElements(numberOfElements, elementsA);
        long countB = elementsB.stream().filter(bloomFilter::lookUp).count();

        System.out.println(countA);
        System.out.println(countB);
    }

    private void encode(Integer element) {
        for (int i = 0; i < hashHelpers.length; i++) {
            filter[hashValue(i, element)] = 1;
        }
    }

    private boolean lookUp(int element) {
        for (int i = 0; i < hashHelpers.length; i++) {
            if (filter[hashValue(i, element)] == 0) return false;
        }
        return true;
    }

    /**
     * Creates some random values between 0 and {@link #filter} length * 10 to be used for XOR with hash for hash functions
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


    protected Set<Integer> randomElements(int numberOfElements) {
        return randomElements(numberOfElements, new HashSet<>());
    }

    /**
     * Generates numberOfElements distinct random elements b/w 0 and {@link #filter} length * 10
     */
    protected Set<Integer> randomElements(int numberOfElements, Set<Integer> usedElements) {
        Set<Integer> elements = new HashSet<>();
        for (int count = 0; count < numberOfElements; count++) {
            int randomElement = nextRandomInt(usedElements);
            elements.add(randomElement);
            usedElements.add(randomElement);
        }
        return elements;
    }

    /**
     * Returns a random value b/w 0 and {@link #filter} length * 10 other than already present in used.
     */
    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt(filter.length * 10));

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    public int hashValue(int index, int element) {
        return Math.abs(element ^ hashHelpers[index]) % filter.length;
    }
}
