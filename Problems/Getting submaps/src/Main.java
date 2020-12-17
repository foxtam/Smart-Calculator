import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int from = scanner.nextInt();
        int to = Integer.parseInt(scanner.nextLine().strip());

        int pairNumber = Integer.parseInt(scanner.nextLine());
        TreeMap<Integer, String> pairs = new TreeMap<>();
        for (int i = 0; i < pairNumber; i++) {
            int key = scanner.nextInt();
            String value = scanner.nextLine().strip();
            pairs.put(key, value);
        }

        SortedMap<Integer, String> part = pairs.subMap(from, to + 1);

        for (Map.Entry<Integer, String> entry : part.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}