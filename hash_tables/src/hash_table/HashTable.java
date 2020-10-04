package hash_table;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class HashTable {
    private static final Random RANDOM = new Random();

    protected final Flow[] entries;
    protected final int[] hashHelpers;

    public HashTable(int tableSize, int numberOfHashFunctions) {
        this.entries = new Flow[tableSize];
        this.hashHelpers = createHashHelpers(numberOfHashFunctions);
    }

    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt(entries.length * 10));

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    void run(int numberOfFlows) {
        run(randomFlows(numberOfFlows));
    }

    void run(Set<Flow> flows) {
        int missed = 0;
        for (Flow flow : flows) {
            if (!insert(flow)) {
                missed++;
            }
        }

        print();
        System.out.println("Missed Flows : " + missed + " in " + name());
    }

    public Set<Flow> randomFlows(int numberOfFlows) {
        Set<Flow> flows = new HashSet<>();
        Set<Integer> usedFlowIds = new HashSet<>();
        for (int count = 0; count < numberOfFlows; count++) {
            int randomFlowId = nextRandomInt(usedFlowIds);
            flows.add(new Flow(randomFlowId));
            usedFlowIds.add(randomFlowId);
        }
        return flows;
    }

    private int[] createHashHelpers(int numberOfHashFunctions) {
        int[] hashValues = new int[numberOfHashFunctions];

        Set<Integer> used = new HashSet<>();
        for (int index = 0; index < numberOfHashFunctions; index++) {
            hashValues[index] = nextRandomInt(used);
            used.add(hashValues[index]);
        }

        return hashValues;
    }

    public abstract boolean insert(Flow flow);

    public abstract String name();

    public void print() {
        int numberOfFlowsInHashTable = 0;

        for (int index = 0; index < entries.length; index++) {
//            System.out.println("Index : " + index + ", Flow Id : " + (entries[index] != null ? entries[index].getId() : 0));
            if (entries[index] != null) numberOfFlowsInHashTable++;
        }

        System.out.println("Number of flow in hash table : " + numberOfFlowsInHashTable + " in " + name());
    }
}


class Flow {
    private final int id;

    public Flow(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}

