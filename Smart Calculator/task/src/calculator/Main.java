package calculator;

import java.util.Scanner;

public class Main {

    private static boolean doContinue = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (doContinue) {
            String input = scanner.nextLine();
            if (isCommand(input)) {
                runCommand(input);
            } else if (!input.isBlank()) {
                runCalculating(input);
            }
        }
    }

    private static boolean isCommand(String line) {
        return line.startsWith("/");
    }

    private static void runCommand(String line) {
        if (line.equals("/exit")) {
            System.out.println("Bye!");
            doContinue = false;
        } else if (line.equals("/help")) {
            System.out.println("The program calculates the sum of numbers");
        } else {
            System.out.println("Unknown command");
        }
    }

    private static void runCalculating(String line) {
        if (line.matches("[-+]?\\d+(\\s+[-+]+\\s+\\d+)*")) {
            System.out.println(calculate(line));
        } else {
            System.out.println("Invalid expression");
        }
    }

    private static int calculate(String line) {
        line = removePluses(line);
        line = removeDoubleMinuses(line);
        String[] inputNumbers = splitIntoNumbers(line);
        int[] numbers = stringsToNumbers(inputNumbers);
        return sum(numbers);
    }

    private static String removePluses(String line) {
        return line.replaceAll("\\+", "");
    }

    private static String removeDoubleMinuses(String line) {
        return line.replaceAll("--", "");
    }

    private static String[] splitIntoNumbers(String line) {
        line = line.replaceAll("(?<=-)\\s*(?=\\d)", "");
        return line.split("\\s+");
    }

    private static int[] stringsToNumbers(String[] inputNumbers) {
        int[] array = new int[inputNumbers.length];
        for (int i = 0; i < inputNumbers.length; i++) {
            array[i] = Integer.parseInt(inputNumbers[i]);
        }
        return array;
    }

    private static int sum(int[] numbers) {
        int sum = 0;
        for (int n : numbers) {
            sum += n;
        }
        return sum;
    }
}
