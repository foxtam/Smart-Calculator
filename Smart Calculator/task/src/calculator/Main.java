package calculator;

import calculator.exception.*;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Map<String, BigInteger> store = new HashMap<>();
    private static final Pattern assignPattern = Pattern.compile("(.+?)\\s*=\\s*(.+)");
    private static final Pattern toPlusPattern = Pattern.compile("\\+{2,}|(--)+");
    private static final Pattern toMinusPattern = Pattern.compile("-\\+|\\+-");
    private static final String unaryMinus = "(-)";
    private static final Map<String, Integer> operatorPriority =
            Map.of("*", 7,
                    "/", 7,
                    "+", 5,
                    "-", 5,
                    unaryMinus, 8,
                    "^", 7);

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

        try {
            store.put(variableName, calculateExpression(expression));
        } catch (InvalidExpressionException e) {
            throw new InvalidAssignmentException();
        }
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

    private static BigInteger calculateExpression(String line) throws InvalidExpressionException, UnknownVariableException {
        line = shrinkOperators(line);
        line = replaceVariables(line);
        line = line.replaceAll("\\s+", "");
        if (line.matches(".*(\\d[a-zA-Z]|([*/^]){2,}).*")) {
            throw new InvalidExpressionException();
        }
        if (line.matches(".*[a-zA-Z].*")) throw new UnknownVariableException();
        String[] tokens = line.split("(?<=\\D)|(?=\\D)");
        String[] rpn = getReversedPolishNotation(tokens);
        return calculateReversedPolishNotation(rpn);
    }

    private static BigInteger calculateReversedPolishNotation(String[] expression) throws InvalidExpressionException {
        Deque<BigInteger> stack = new ArrayDeque<>();
        for (String token : expression) {
            if (isNumber(token)) {
                stack.addLast(new BigInteger(token));
            } else if (token.equals(unaryMinus)) {
                stack.addLast(stack.removeLast().negate());
            } else {
                BigInteger b = stack.removeLast();
                BigInteger a = stack.removeLast();
                stack.addLast(performOperation(token, a, b));
            }
        }
        return stack.removeLast();
    }

    private static BigInteger performOperation(String operator, BigInteger a, BigInteger b) throws InvalidExpressionException {
        switch (operator) {
            case "*":
                return a.multiply(b);
            case "/":
                return a.divide(b);
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "^":
                return a.pow(b.intValueExact());
            default:
                throw new InvalidExpressionException();
        }
    }

    private static String[] getReversedPolishNotation(String[] tokens) throws InvalidExpressionException {
        if (tokens[0].equals("-")) tokens[0] = unaryMinus;

        List<String> expression = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                expression.add(token);
            } else if (token.equals("(")) {
                stack.addLast(token);
            } else if (operatorPriority.containsKey(token)) {
                removeOperatorsToExpressionByPriority(expression, stack, token);
                stack.addLast(token);
            } else if (token.equals(")")) {
                removeOperatorsToExpressionUntilLeftParenthesis(expression, stack);
            } else {
                throw new InvalidExpressionException();
            }
        }

        while (!stack.isEmpty()) {
            String element = stack.removeLast();
            if (element.equals("(")) throw new InvalidExpressionException();
            expression.add(element);
        }

        return expression.toArray(new String[0]);
    }

    private static void removeOperatorsToExpressionUntilLeftParenthesis(List<String> expression, Deque<String> stack) throws InvalidExpressionException {
        try {
            while (!stack.getLast().equals("(")) {
                expression.add(stack.removeLast());
            }
            stack.removeLast();
        } catch (NoSuchElementException e) {
            throw new InvalidExpressionException();
        }
    }

    private static void removeOperatorsToExpressionByPriority(List<String> expression, Deque<String> stack, String token) {
        while (!stack.isEmpty() && !stack.getLast().equals("(")
                && operatorPriority.get(token) <= operatorPriority.get(stack.getLast())) {
            expression.add(stack.removeLast());
        }
    }

    private static boolean isNumber(String token) {
        return token.matches("\\d+");
    }

    private static String replaceVariables(String line) {
        for (Map.Entry<String, BigInteger> entry : store.entrySet()) {
            line = line.replaceAll("\\b" + entry.getKey() + "\\b", "(0" + entry.getValue().toString() + ")");
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
