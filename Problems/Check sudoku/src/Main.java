import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int smallSquareSize = readSmallSquareSize();
        Integer[][] sudokuSquare = readSudokuSquare(smallSquareSize);

        System.out.println(checkSudoku(sudokuSquare, smallSquareSize) ? "YES" : "NO");
    }

    private static int readSmallSquareSize() {
        return Integer.parseInt(scanner.nextLine());
    }

    private static Integer[][] readSudokuSquare(int smallSize) {
        int squareSize = smallSize * smallSize;
        Integer[][] square = new Integer[squareSize][squareSize];

        for (int i = 0; i < squareSize; i++) {
            for (int j = 0; j < squareSize; j++) {
                square[i][j] = scanner.nextInt();
            }
        }
        return square;
    }

    private static boolean checkSudoku(Integer[][] sudokuSquare, int smallSize) {
        return checkSudokuRows(sudokuSquare, smallSize)
                && checkSudokuColumns(sudokuSquare, smallSize)
                && checkSudokuSmallSquares(sudokuSquare, smallSize);
    }

    private static boolean checkSudokuRows(Integer[][] sudokuSquare, int smallSize) {
        for (Integer[] row : sudokuSquare) {
            Set<Integer> numbers = new HashSet<>(Arrays.asList(row));
            if (!checkSudokuNumbers(numbers, smallSize)) return false;
        }
        return true;
    }

    private static boolean checkSudokuColumns(Integer[][] sudokuSquare, int smallSize) {
        int size = smallSize * smallSize;
        for (int j = 0; j < size; j++) {
            Set<Integer> numbers = new HashSet<>();
            for (int i = 0; i < size; i++) {
                numbers.add(sudokuSquare[i][j]);
            }
            if (!checkSudokuNumbers(numbers, smallSize)) return false;
        }
        return true;
    }

    private static boolean checkSudokuSmallSquares(Integer[][] sudokuSquare, int smallSize) {
        int size = smallSize * smallSize;
        for (int initI = 0; initI < size; initI += smallSize) {
            for (int initJ = 0; initJ < size; initJ += smallSize) {
                Set<Integer> numbers = new HashSet<>();
                for (int i = 0; i < smallSize; i++) {
                    numbers.addAll(Arrays.asList(sudokuSquare[initI + i]).subList(initJ, smallSize + initJ));
                }
                if (!checkSudokuNumbers(numbers, smallSize)) return false;
            }
        }
        return true;
    }

    private static boolean checkSudokuNumbers(Set<Integer> numbers, int smallSize) {
        int size = smallSize * smallSize;
        if (numbers.size() != size) return false;

        for (int i = 1; i <= size; i++) {
            if (!numbers.contains(i)) return false;
        }
        return true;
    }
}