import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VirtualBitmap {

    public static final Random RANDOM = new Random();
    private final int[] R;
    private final int[] B;

    double V_b = -1;

    public VirtualBitmap(int m, int l) {
        this.B = new int[m];
        this.R = createHashHelpers(l);
    }

    public static void main(String[] args) throws IOException {
        int m = 500_000, l = 500;
        VirtualBitmap bitmap = new VirtualBitmap(m, l);
        bitmap.run();
    }

    public static Set<Flow> readFlows() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileName()));
        int n = Integer.parseInt(scanner.nextLine());

        return IntStream.range(0, n).mapToObj(i -> new Flow(scanner.nextLine())).collect(Collectors.toSet());
    }

    public static String fileName() {
        String file = "flow_size_sketch/resources/project4input.txt";
        if (!new File(file).exists()) {
            file = "resources/project4input.txt";
            if (!new File(file).exists())
                throw new RuntimeException("File not present");
        }

        return file;
    }

    private static String directory() {
        return fileName().replace("project4input.txt", "").replace("resources", "output");
    }

    public void run() throws IOException {
        Set<Flow> flows = readFlows();

        // Record each flow
        flows.forEach(this::record);

        // Query each flow
        FileWriter writer = new FileWriter(directory() + "output.txt");
        for (Flow f : flows) writer.write(f.numberOfPackets + "," + query(f) + "\n");
        writer.close();
    }

    public void record(Flow flow) {
        for (int index = 0; index < flow.numberOfPackets; index++) {
            B[physicalBitmapIndex(virtualBitmapIndex(RANDOM.nextInt(), flow), flow)] = 1;
        }
    }

    public int query(Flow flow) {
        double countVf = 0;
        for (int index = 0; index < R.length; index++) {
            if (B[physicalBitmapIndex(index, flow)] == 0) {
                countVf++;
            }
        }

        double V_f = countVf / R.length;

        // We don't want keep calculating V_b as it will be always same
        if (V_b == -1) {
            double countVb = 0;
            for (int bit : B) if (bit == 0) countVb++;
            V_b = countVb / B.length;
        }

        return (int) (R.length * (Math.log(V_b) - Math.log(V_f)));
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

    protected int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt());

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    private int virtualBitmapIndex(int element, Flow flow) {
        return Math.abs((Math.abs(flow.flowId.hashCode()) ^ element) % R.length);
    }

    private int physicalBitmapIndex(int index, Flow flow) {
        return (Math.abs(flow.flowId.hashCode()) ^ R[index]) % B.length;
    }

    public static class Flow {
        public final String flowId;
        public final int numberOfPackets;

        public Flow(String input) {
            String[] split = input.split("\\s+");
            this.flowId = split[0].trim();
            this.numberOfPackets = Integer.parseInt(split[1]);
        }
    }
}
