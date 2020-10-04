package hash_table;

public class CuckooHashTable extends HashTable {

    private final int cuckooSteps;

    public CuckooHashTable(int tableSize, int numberOfHashFunctions, int cuckooSteps) {
        super(tableSize, numberOfHashFunctions);
        this.cuckooSteps = cuckooSteps;
    }

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;
        int cuckooSteps = 2;

        CuckooHashTable table = new CuckooHashTable(tableSize, numberOfHashFunctions, cuckooSteps);
        table.run(numberOfFlows);
    }

    @Override
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

    @Override
    public String name() {
        return "CuckooHashTable";
    }

    public int hashValue(int index, Flow flow) {
        return Math.abs(flow.getId().hashCode() ^ hashHelpers[index]) % entries.length;
    }

    private boolean move(int index, int cuckooSteps) {
        if (cuckooSteps == 0) {
            return false;
        }

        Flow existingFlow = entries[index];

        for (int count = 0; count < hashHelpers.length; count++) {
            if (hashValue(count, existingFlow) != index && entries[hashValue(count, existingFlow)] == null) {
                entries[hashValue(count, existingFlow)] = existingFlow;
                return true;
            }
        }

        for (int count = 0; count < hashHelpers.length; count++) {
            if (hashValue(count, existingFlow) != index && move(hashValue(count, existingFlow), cuckooSteps - 1)) {
                entries[hashValue(count, existingFlow)] = existingFlow;
                return true;
            }
        }

        return false;
    }
}
