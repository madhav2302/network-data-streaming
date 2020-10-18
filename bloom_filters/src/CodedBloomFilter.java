import java.util.*;

public class CodedBloomFilter {

    public int count = 0;
    public static final Random RANDOM = new Random();
    private final int[][] filters;
    private final int[] hashHelpers;
    private final int[][] setCodes = {
            {0, 0, 0}, // Set code for 0, NULL, won't be used
            {0, 0, 1}, // Set code for 1
            {0, 1, 0}, // for 2
            {0, 1, 1}, // for 3
            {1, 0, 0}, // for 4
            {1, 0, 1}, // for 5
            {1, 1, 0}, // for 6
            {1, 1, 1}  // for 7
    };

    public CodedBloomFilter(int numberOfFilters, int numberOfElementsPerFilter, int numberOfHashFunctions) {
        this.filters = new int[numberOfFilters][numberOfElementsPerFilter];
        this.hashHelpers = createHashHelpers(numberOfHashFunctions);
    }

    public static void main(String[] args) {
        int numberOfSets = 7;
        int numberOfElementPerSet = 1_000;
        int numberOfFilters = 3;
        int numberOfBitsPerFilter = 30_000;
        int numberOfHashes = 7;

        CodedBloomFilter bloomFilter = new CodedBloomFilter(numberOfFilters, numberOfBitsPerFilter, numberOfHashes);

        Map<Integer, Set<Integer>> setOfElements = new HashMap<>();

        Set<Integer> used = new HashSet<>();
        for (int setNumber = 1; setNumber <= numberOfSets; setNumber++) {
            setOfElements.put(setNumber, bloomFilter.randomElements(numberOfElementPerSet, used));
        }
        // Encode
        for (int setNumber = 1; setNumber <= numberOfSets; setNumber++) {
            for (int element : setOfElements.get(setNumber)) {
                bloomFilter.encode(setNumber, element);
            }
        }

        // LookUp
        int result = 0;
        for (int setNumber = 1; setNumber <= numberOfSets; setNumber++) {
            for (int element : setOfElements.get(setNumber)) {
                if (bloomFilter.lookUp(setNumber, element)) result++;
            }
        }

        System.out.println(result);
    }

    public void encode(int setNumber, int element) {
        int filterNumber = 0;
        for (int setCode : setCodes[setNumber]) {
            if (setCode == 1) {
                for (int i = 0; i < hashHelpers.length; i++) {
                    filters[filterNumber][hashValue(i, element)] = 1;
                }
            }
            filterNumber++;
        }
    }

    public boolean lookUp(int setNumber, int element) {
        int[] lookUpSet = new int[filters.length];

        for (int filterIndex = 0; filterIndex < filters.length; filterIndex++) {
            for (int hashIndex = 0; hashIndex < hashHelpers.length; hashIndex++) {
                if (filters[filterIndex][hashValue(hashIndex, element)] == 0) {
                    break;
                }
            }

            lookUpSet[filterIndex] = 1;
        }

        return Arrays.equals(lookUpSet, setCodes[setNumber]);
    }

    /**
     * Creates some random values between 0 and (numberOfFilters * numberOfElementsPerFilter * 10) to be used for XOR with hash for hash functions
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
     * Returns a random value b/w 0 and (numberOfFilters * numberOfElementsPerFilter * 10) other than already present in used.
     */
    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt(filters.length * filters[0].length * 10));

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    /**
     * Generates numberOfElements distinct random elements b/w 0 and (numberOfFilters * numberOfElementsPerFilter * 10)
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

    public int hashValue(int index, int element) {
        return Math.abs(element ^ hashHelpers[index]) % filters[0].length;
    }
}
