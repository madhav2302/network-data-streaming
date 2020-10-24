import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Project3Util {

    private static final Random RANDOM = new Random();

    public static int[] createHashHelpers(int numberOfHashFunctions) {
        int[] hashValues = new int[numberOfHashFunctions];

        Set<Integer> used = new HashSet<>();
        for (int index = 0; index < numberOfHashFunctions; index++) {
            hashValues[index] = nextRandomInt(used);
            used.add(hashValues[index]);
        }

        return hashValues;
    }

    private static int nextRandomInt(Set<Integer> used) {
        int random = Math.abs(RANDOM.nextInt());

        if (used.contains(random)) return nextRandomInt(used);

        return random;
    }

    public static Set<Flow> readFlows() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("project3/resources/project3input.txt"));
        int n = Integer.parseInt(scanner.nextLine());

        Set<Flow> flows = new HashSet<>();
        for (int i = 0; i < n; i++) {
            flows.add(new Flow(scanner.nextLine()));
        }

        return flows;
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
