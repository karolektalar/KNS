import java.io.*;
import java.util.Scanner;

public class KNS {

    public static void main(String[] args) {
        int maxLengthOfWord;
        int alphabetLength;

        ClassLoader classLoader = KNS.class.getClassLoader();
        Scanner in = new Scanner(System.in);
        System.out.println("Podaj maksymalną długość słowa");
        String line = in.nextLine();
        if (line != null) {
            try {
                maxLengthOfWord = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Złe dane wejściowe, maksymalna długość słowa musi być dodatnią liczbą naturalną");
                return;
            }
            if (maxLengthOfWord < 1) {
                System.out.println("Złe dane wejściowe, maksymalna długość słowa musi być dodatnią liczbą naturalną");
                return;
            }
        } else {
            System.out.println("Złe dane wejściowe, w pierwszej linijce pliku wejściowego powinna być przekazana długość słowa");
            return;
        }
        System.out.println("Podaj liczbę znaków w alfabecie (1-50)");
        line = in.nextLine();
        if (line != null) {
            try {
                alphabetLength = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Złe dane wejściowe, długość alfabetu powinna być dodatnią liczbą naturalną, mniejszą od 50");
                return;
            }
            if (alphabetLength < 1 || alphabetLength > 50) {
                System.out.println("Złe dane wejściowe, długość alfabetu powinna być dodatnią liczbą naturalną, mniejszą od 50");
                return;
            }
        } else {
            System.out.println("Złe dane wejściowe, długość alfabetu powinna być dodatnią liczbą naturalną, mniejszą od 50");
            return;
        }
        char[] alphabet = new char[alphabetLength];
        for (int i = 0; i < alphabetLength; i++) {
            if (i < 25)
                alphabet[i] = (char) (i + 65);
            else alphabet[i] = (char) (i + 72);
        }

        System.out.println("Wybierz przeciwnika");
        System.out.println("1) Przeciwnik losowy");
        System.out.println("2) Przeciwnik brute force");
        System.out.println("3) Przeciwnik heurystyczny");
        line = in.nextLine();
        int opponent = 0;
        if (line != null) {
            try {
                opponent = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Złe dane wejściowe, żeby wybrać przeciwnika podaj liczbę z odpowiedniego zakresu");
                return;
            }
            if (opponent < 1 || opponent > 3) {
                System.out.println("Złe dane wejściowe, żeby wybrać przeciwnika podaj liczbę z odpowiedniego zakresu");
                return;
            }
            if (opponent == 1)
                System.out.println("Wybrano przeciwnika losowego");
            if (opponent == 2)
                System.out.println("Wybrano przeciwnika brue force");
            if (opponent == 3) {
                System.out.println("Wybrano przeciwnika heurystycznego");
            }
        }

        Game game = new Game();


        game.game(maxLengthOfWord, alphabet, opponent);
    }
}

