import java.util.*;

public class DLeftHashTable {

    private static final Random RANDOM = new Random();

    /**
     * Hash Table as array
     */
    private final Flow[] table;

    private final int[] hashHelpers;
    private final int segmentSize;

    public DLeftHashTable(int tableSize, int numberOfHashFunctions) {
        this.table = new Flow[tableSize];
        this.hashHelpers = createHashHelpers(numberOfHashFunctions);
        this.segmentSize = tableSize / numberOfHashFunctions;
    }

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;

        DLeftHashTable table = new DLeftHashTable(tableSize, numberOfHashFunctions);

        table.run(numberOfFlows);
        table.print();
    }

    void run(int numberOfFlows) {
        randomFlows(numberOfFlows).forEach(this::insert);
    }

    /**
     * Generates numberOfFlows distinct random flows b/w 0 and {@link #table} length * 10
     */
    protected Set<Flow> randomFlows(int numberOfFlows) {
        Set<Flow> flows = new HashSet<>();
        Set<Integer> usedFlowIds = new HashSet<>();
        usedFlowIds.add(0); // We don't want to use 0 as flow id, as we print it for empty entry
        for (int count = 0; count < numberOfFlows; count++) {
            int randomFlowId = nextRandomInt(usedFlowIds);
            flows.add(new Flow(randomFlowId));
            usedFlowIds.add(randomFlowId);
        }
        return flows;
    }

    /**
     * Creates some random values b/w 0 and {@link #table} length * 10 to be used for XOR with hash for hash functions
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
     * Returns a random value b/w 0 and {@link #table} length * 10 other than already present in used.
     */
    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt(table.length * 10));

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    /**
     * Inserts flow id into hash table if possible, otherwise ignore the flow.
     */
    public boolean insert(Flow flow) {
        for (int count = 0; count < hashHelpers.length; count++) {
            int index = hashValue(count, flow);
            if (table[index] == null) {
                table[index] = flow;
                return true;
            }
        }

        return false;
    }

    /**
     * Prints count of number of flow present in hash table in first line.
     * Prints flow id present at index number, otherwise zero (0).
     */
    public void print() {
        long numberOfFlowsInHashTable = Arrays.stream(table).filter(Objects::nonNull).count();
        System.out.println(numberOfFlowsInHashTable);

        for (Flow entry : table) {
            System.out.println(entry != null ? entry.getId() : 0);
        }
    }

    /**
     * Index for flow for hash table.
     */
    public int hashValue(int index, Flow flow) {
        int value = Math.abs(flow.getId().hashCode() ^ hashHelpers[index]) % table.length;
        int currentSegmentMinValue = index * segmentSize;
        return (value % segmentSize) + currentSegmentMinValue;
    }
}
