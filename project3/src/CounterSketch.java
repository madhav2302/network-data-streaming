import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

public class CounterSketch {

    private final int[][] C;
    private final int[] hashHelpers;

    public CounterSketch(int k, int w) {
        C = new int[k][w];
        this.hashHelpers = Project3Util.createHashHelpers(k);
    }


    public static void main(String[] args) throws FileNotFoundException {
        int k = 3;
        int w = 3_000;

        CounterSketch countMin = new CounterSketch(k, w);

        Set<Project3Util.Flow> flows = Project3Util.readFlows();
        // Record all flows
        flows.forEach(countMin::record);

        // Total Error
        Integer totalError = flows.stream().map(f -> Math.abs(countMin.query(f) - f.numberOfPackets)).reduce(0, Integer::sum);
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


    public void record(Project3Util.Flow flow) {
        for (int index = 0; index < C.length; index++) {
            int hashValue = Math.abs(hashValue(index, flow));

            String s = Integer.toBinaryString(hashValue);

            //  TODO :
            if (s.length() >= 16) {
                C[index][hashIndex(index, flow)] += flow.numberOfPackets;
            } else {
                C[index][hashIndex(index, flow)] -= flow.numberOfPackets;
            }
        }
    }

    public int median(int[] array) {
        Arrays.sort(array);
        if (array.length % 2 == 0)
            return array[array.length / 2] / 2 + array[array.length / 2 - 1] / 2;
        else
            return array[array.length / 2];

    }

    public int query(Project3Util.Flow flow) {
        int[] array = new int[C.length];

        for (int index = 0; index < C.length; index++) {
            array[index] = C[index][hashIndex(index, flow)];
        }

        return median(array);
    }

    private int hashValue(int index, Project3Util.Flow flow) {
        return flow.flowId.hashCode() ^ hashHelpers[index];
    }

    private int hashIndex(int index, Project3Util.Flow flow) {
        return Math.abs(hashValue(index, flow)) % C[0].length;
    }
}
