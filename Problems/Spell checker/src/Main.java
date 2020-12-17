import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int wordsNumber = Integer.parseInt(scanner.nextLine());
        HashSet<String> knownWords = new HashSet<>();
        for (int i = 0; i < wordsNumber; i++) {
            knownWords.add(scanner.nextLine().toLowerCase(Locale.ROOT));
        }

        int linesNumber = Integer.parseInt(scanner.nextLine());
        HashSet<String> erroneous = new HashSet<>();
        for (int i = 0; i < linesNumber; i++) {
            String[] words = scanner.nextLine().toLowerCase(Locale.ROOT).split("\\s+");
            for (String word : words) {
                if (!knownWords.contains(word)) {
                    erroneous.add(word);
                }
            }
        }

        for (String word : erroneous) {
            System.out.println(word);
        }
    }
}