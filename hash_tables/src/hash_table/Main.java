package hash_table;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;
        int numberOfCuckooSteps = 2;

        MultiHashTable multiHashTable = new MultiHashTable(tableSize, numberOfHashFunctions);
        CuckooHashTable cuckooHashTable = new CuckooHashTable(tableSize, numberOfHashFunctions, numberOfCuckooSteps);
        DLeftHashTable dLeftHashTable = new DLeftHashTable(tableSize, numberOfHashFunctions);

        Set<Flow> flows = multiHashTable.randomFlows(numberOfFlows);

        multiHashTable.run(flows);
        cuckooHashTable.run(flows);
        dLeftHashTable.run(flows);
    }
}
