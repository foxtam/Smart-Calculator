import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stringWithHtmlTags = scanner.nextLine();

        String s = stringWithHtmlTags.replaceAll("<.+?>", "");
        System.out.println(s);
    }
}