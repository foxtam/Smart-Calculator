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
                int sum = getSum(input);
                System.out.println(sum);
            }
        }
    }

    private static int getSum(String input) {
        String[] numbers = input.split("\\s+");
        int sum = 0;
        for (String n : numbers) {
            sum += Integer.parseInt(n);
        }
        return sum;
    }
}
