package calculator;

import calculator.exception.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Map<String, Integer> store = new HashMap<>();
    private static final Pattern assignPattern = Pattern.compile("(.+?)\\s*=\\s*(.+)");
    private static final Pattern toPlusPattern = Pattern.compile("\\+{2,}|(--)+");
    private static final Pattern toMinusPattern = Pattern.compile("-\\+|\\+-");
    private static boolean doContinue = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (doContinue) {
            String input = scanner.nextLine().trim();
            if (input.isBlank()) continue;
            process(input);
        }
    }

    private static void process(String input) {
        try {
            tryProcess(input);
        } catch (InvalidIdentifierException e) {
            System.out.println("Invalid identifier");
        } catch (InvalidAssignmentException e) {
            System.out.println("Invalid assignment");
        } catch (UnknownCommandException e) {
            System.out.println("Unknown command");
        } catch (InvalidExpressionException e) {
            System.out.println("Invalid expression");
        } catch (UnknownVariableException e) {
            System.out.println("Unknown variable");
        }
    }

    private static void tryProcess(String input) throws InvalidIdentifierException, InvalidAssignmentException, UnknownCommandException, InvalidExpressionException, UnknownVariableException {
        if (isCommand(input)) {
            runCommand(input);
        } else if (isAssignment(input)) {
            runAssign(input);
        } else {
            printExpressionResult(input);
        }
    }

    private static void runAssign(String line) throws InvalidIdentifierException, InvalidAssignmentException, UnknownVariableException {
        Matcher matcher = assignPattern.matcher(line);
        if (!matcher.find()) throw new InvalidAssignmentException();
        String variableName = matcher.group(1);
        if (!isCorrectIdentifier(variableName)) throw new InvalidIdentifierException();
        String expression = matcher.group(2);
        if (!isCorrectExpression(expression)) throw new InvalidAssignmentException();
        try {
            store.put(variableName, calculateExpression(expression));
        } catch (InvalidExpressionException e) {
            throw new InvalidAssignmentException();
        }
    }

    private static boolean isCorrectExpression(String expression) {
        return expression.matches("[-+]?(\\d+|[a-zA-Z]+)(\\s+[-+]+\\s+(\\d+|[a-zA-Z]+))*");
    }

    private static boolean isAssignment(String line) {
        return assignPattern.matcher(line).matches();
    }

    private static boolean isCorrectIdentifier(String variableName) {
        return variableName.matches("[a-zA-Z]+");
    }

    private static boolean isCommand(String line) {
        return line.startsWith("/");
    }

    private static void runCommand(String line) throws UnknownCommandException {
        if (line.equals("/exit")) {
            System.out.println("Bye!");
            doContinue = false;
        } else if (line.equals("/help")) {
            System.out.println("The program calculates the sum of numbers");
        } else {
            throw new UnknownCommandException();
        }
    }

    private static void printExpressionResult(String line) throws InvalidExpressionException, UnknownVariableException {
        System.out.println(calculateExpression(line));
    }

    private static int calculateExpression(String line) throws InvalidExpressionException, UnknownVariableException {
        line = shrinkOperators(line);
        line = insertValues(line);
        if (hasAnyLetter(line)) throw new UnknownVariableException();
        String[] tokens = line.split("\\s+");
        return parseAndCalculate(tokens);
    }

    private static int parseAndCalculate(String[] tokens) throws InvalidExpressionException {
        int result = Integer.parseInt(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            switch (tokens[i]) {
                case "+":
                    result += Integer.parseInt(tokens[i + 1]);
                    break;
                case "-":
                    result -= Integer.parseInt(tokens[i + 1]);
                    break;
                default:
                    throw new InvalidExpressionException();
            }
        }
        return result;
    }

    private static boolean hasAnyLetter(String line) {
        return line.matches(".*[a-zA-Z].*");
    }

    private static String insertValues(String line) {
        for (Map.Entry<String, Integer> entry : store.entrySet()) {
            line = line.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue().toString());
        }
        return line;
    }

    private static String shrinkOperators(String line) {
        while (true) {
            Matcher plusMatcher = toPlusPattern.matcher(line);
            if (plusMatcher.find()) {
                line = plusMatcher.replaceAll("+");
                continue;
            }
            Matcher minusMatcher = toMinusPattern.matcher(line);
            if (minusMatcher.find()) {
                line = minusMatcher.replaceAll("-");
                continue;
            }
            return line;
        }
    }
}
