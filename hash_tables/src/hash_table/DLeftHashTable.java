package hash_table;

public class DLeftHashTable extends HashTable {

    private final int segmentSize;

    public DLeftHashTable(int tableSize, int numberOfHashFunctions) {
        super(tableSize, numberOfHashFunctions);
        this.segmentSize = tableSize / numberOfHashFunctions;
    }

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;

        DLeftHashTable table = new DLeftHashTable(tableSize, numberOfHashFunctions);

        table.run(numberOfFlows);
    }

    @Override
    public boolean insert(Flow flow) {
        for (int count = 0; count < hashHelpers.length; count++) {
            int index = hashValue(count, flow);
            if (entries[index] == null) {
                entries[index] = flow;
                return true;
            }
        }

        return false;
    }

    @Override
    public String name() {
        return "DLeftHashTable";
    }

    public int hashValue(int index, Flow flow) {
        int value = Math.abs(flow.getId().hashCode() ^ hashHelpers[index]) % entries.length;
        int currentSegmentMinValue = index * segmentSize;
        return (value % segmentSize) + currentSegmentMinValue;
    }
}
