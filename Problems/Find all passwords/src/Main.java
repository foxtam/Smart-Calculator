import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        Pattern pattern = Pattern.compile("password[ :]*([a-z\\d]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean wasPassword = false;
        while (matcher.find()) {
            wasPassword = true;
            System.out.println(matcher.group(1));
        }
        if (!wasPassword) {
            System.out.println("No passwords found.");
        }
    }
}