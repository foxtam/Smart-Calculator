import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String first = scanner.nextLine().toLowerCase(Locale.ROOT);
        String second = scanner.nextLine().toLowerCase(Locale.ROOT);

        HashMap<Character, Integer> charCounter = new HashMap<>();

        for (Character c : first.toCharArray()) {
            charCounter.put(c, charCounter.getOrDefault(c, 0) + 1);
        }

        for (Character c : second.toCharArray()) {
            charCounter.put(c, charCounter.getOrDefault(c, 0) - 1);
        }

        int total = 0;
        for (Integer value : charCounter.values()) {
            total += Math.abs(value);
        }

        System.out.println(total);
    }
}