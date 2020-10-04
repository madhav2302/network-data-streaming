import java.util.Set;

public class Main {

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;

        MultiHashTable multiHashTable = new MultiHashTable(tableSize, 3);
        CuckooHashTable cuckooHashTable = new CuckooHashTable(tableSize, 3, 2);
        DLeftHashTable dLeftHashTable = new DLeftHashTable(tableSize, 4);

        Set<Flow> flows = multiHashTable.randomFlows(numberOfFlows);

        flows.forEach(multiHashTable::insert);
        flows.forEach(cuckooHashTable::insert);
        flows.forEach(dLeftHashTable::insert);

        multiHashTable.print();
        cuckooHashTable.print();
        dLeftHashTable.print();
    }
}
