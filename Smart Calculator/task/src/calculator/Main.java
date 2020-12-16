package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("/exit")) {
                System.out.println("Bye!");
                break;
            } else if (input.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            } else if (!input.isBlank()) {
                int sum = calculate(input);
                System.out.println(sum);
            }
        }
    }

    private static int calculate(String input) {
        input = removePluses(input);
        input = removeDoubleMinuses(input);
        String[] inputNumbers = splitIntoNumbers(input);
        int[] numbers = stringsToNumbers(inputNumbers);
        return sum(numbers);
    }

    private static String removePluses(String string) {
        return string.replaceAll("\\+", "");
    }

    private static String removeDoubleMinuses(String string) {
        return string.replaceAll("--", "");
    }

    private static String[] splitIntoNumbers(String input) {
        input = input.replaceAll("(?<=-)\\s*(?=\\d)", "");
        return input.split("\\s+");
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
