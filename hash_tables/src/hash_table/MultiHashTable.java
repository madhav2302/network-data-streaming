package hash_table;

public class MultiHashTable extends HashTable {

    public MultiHashTable(int tableSize, int numberOfHashFunctions) {
        super(tableSize, numberOfHashFunctions);
    }

    public static void main(String[] args) {
        int tableSize = 1000;
        int numberOfFlows = 1000;
        int numberOfHashFunctions = 3;

        MultiHashTable table = new MultiHashTable(tableSize, numberOfHashFunctions);

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
        return "MultiHashtable";
    }

    public int hashValue(int index, Flow flow) {
        return Math.abs(flow.getId().hashCode() ^ hashHelpers[index]) % entries.length;
    }
}
