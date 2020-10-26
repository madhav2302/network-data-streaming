import java.io.IOException;
import java.util.*;

public class CountMin {

    private final int[][] C;
    private final int[] hashHelpers;

    public CountMin(int k, int w) {
        C = new int[k][w];
        this.hashHelpers = Project3Util.createHashHelpers(k);
    }

    public static void main(String[] args) throws IOException {
        int k = 3;
        int w = 3_000;

        Set<Project3Util.Flow> flows = Project3Util.readFlows();
        CountMin countMin = new CountMin(k, w);

        // Record all flows
        flows.forEach(countMin::record);

        // Total Error
        Integer totalError = flows.stream().map(f -> countMin.query(f) - f.numberOfPackets).reduce(0, Integer::sum);
        System.out.println(totalError / flows.size());

        // the flows of the largest estimated sizes
        PriorityQueue<Project3Util.Flow> queue = new PriorityQueue<>(Comparator.comparing(countMin::query));

        for (Project3Util.Flow f : flows) {
            queue.add(f);
            if (queue.size() > 100) {
                queue.poll();
            }
        }

        while (!queue.isEmpty()) {
            Project3Util.Flow f = queue.poll();
            String format = "%-15s\t%10d\t%10d\n";
            System.out.printf(format, f.flowId, countMin.query(f), f.numberOfPackets);
        }
    }

    /**
     * Record size of flow in the counters
     */
    public void record(Project3Util.Flow flow) {
        for (int index = 0; index < C.length; index++) {
            C[index][hashIndex(index, flow)] += flow.numberOfPackets;
        }
    }

    /**
     * Fetch minimum size of all the counters for the flow
     */
    public int query(Project3Util.Flow flow) {
        int min = Integer.MAX_VALUE;

        for (int index = 0; index < C.length; index++) {
            min = Math.min(C[index][hashIndex(index, flow)], min);
        }

        return min;
    }

    /**
     * Hash Index for the counter number index
     */
    private int hashIndex(int index, Project3Util.Flow flow) {
        return (Math.abs(flow.flowId.hashCode()) ^ hashHelpers[index]) % C[0].length;
    }
}
