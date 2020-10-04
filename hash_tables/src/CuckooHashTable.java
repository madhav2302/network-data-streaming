import java.util.*;

public class CuckooHashTable {

    private static final Random RANDOM = new Random();

    /**
     * Hash Table as array
     */
    private final Flow[] entries;

    private final int[] hashHelpers;
    private final int cuckooSteps;

    public CuckooHashTable(int tableSize, int numberOfHashFunctions, int cuckooSteps) {
        this.entries = new Flow[tableSize];
        this.hashHelpers = createHashHelpers(numberOfHashFunctions);
        this.cuckooSteps = cuckooSteps;
    }

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;
        int numberOfCuckooSteps = 2;

        CuckooHashTable table = new CuckooHashTable(tableSize, numberOfHashFunctions, numberOfCuckooSteps);

        table.run(numberOfFlows);
        table.print();
    }

    void run(int numberOfFlows) {
        randomFlows(numberOfFlows).forEach(this::insert);
    }

    /**
     * Generates numberOfFlows distinct random flows b/w 0 and {@link #entries} length * 10
     */
    protected Set<Flow> randomFlows(int numberOfFlows) {
        Set<Flow> flows = new HashSet<>();
        Set<Integer> usedFlowIds = new HashSet<>();
        for (int count = 0; count < numberOfFlows; count++) {
            int randomFlowId = nextRandomInt(usedFlowIds);
            flows.add(new Flow(randomFlowId));
            usedFlowIds.add(randomFlowId);
        }
        return flows;
    }

    /**
     * Prints count of number of flow present in hash table in first line.
     * Prints flow id present at index number, otherwise zero (0).
     */
    public void print() {
        long numberOfFlowsInHashTable = Arrays.stream(entries).filter(Objects::nonNull).count();
        System.out.println(numberOfFlowsInHashTable);

        for (Flow entry : entries) {
            System.out.println(entry != null ? entry.getId() : 0);
        }
    }

    /**
     * Creates some random values b/w 0 and {@link #entries} length * 10 to be used for XOR with hash for hash functions
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
     * Returns a random value b/w 0 and {@link #entries} length * 10 other than already present in used.
     */
    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt(entries.length * 10));

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    /**
     * Inserts flow id into hash table if possible, otherwise ignore the flow.
     */
    public boolean insert(Flow flow) {
        for (int hashIndex = 0; hashIndex < hashHelpers.length; hashIndex++) {
            int entryIndex = hashValue(hashIndex, flow);
            if (entries[entryIndex] == null) {
                entries[entryIndex] = flow;
                return true;
            }
        }

        for (int count = 0; count < hashHelpers.length; count++) {
            int index = hashValue(count, flow);
            if (move(index, cuckooSteps)) {
                entries[index] = flow;
                return true;
            }
        }

        return false;
    }

    /**
     * Move flow to other index if possible.
     */
    private boolean move(int index, int cuckooSteps) {
        if (cuckooSteps == 0) return false;

        Flow existingFlow = entries[index];

        for (int count = 0; count < hashHelpers.length; count++) {
            int newIndex = hashValue(count, existingFlow);
            if (newIndex != index && entries[newIndex] == null) {
                entries[newIndex] = existingFlow;
                return true;
            }
        }

        for (int count = 0; count < hashHelpers.length; count++) {
            int newIndex = hashValue(count, existingFlow);
            if (newIndex != index && move(newIndex, cuckooSteps - 1)) {
                entries[newIndex] = existingFlow;
                return true;
            }
        }

        return false;
    }

    /**
     * Index for flow for hash table.
     */
    public int hashValue(int index, Flow flow) {
        return Math.abs(flow.getId().hashCode() ^ hashHelpers[index]) % entries.length;
    }
}
