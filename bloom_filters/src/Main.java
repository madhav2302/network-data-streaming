public class Main {
    public static void main(String[] args) {
        System.out.println("BloomFilter");
        BloomFilter.main(new String[]{});

        System.out.println("CountingBloomFilter");
        CountingBloomFilter.main(new String[]{});

        System.out.println("CodingBloomFilter");
        CodedBloomFilter.main(new String[]{});
    }
}
