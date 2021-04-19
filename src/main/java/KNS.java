import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class KNS {
    public static boolean PLAY_AUTO = true;
    public static int AUTO_MAX_WORD_LENGTH = 10000;
    public static int AUTO_ALPHABET_SIZE = 8;
    public static int AUTO_OPPONENT = 3;
    public static int AUTO_NUMBER_OF_GAMES = 1;

    public static void main(String[] args) {
        boolean continueGame = true;
        while (continueGame) {
            Scanner in = new Scanner(System.in);
            System.out.println("Czy chcesz grać automatycznie? (y - tak, n - nie)");
            String line = in.nextLine();
            if (line.equals("y")) {
                PLAY_AUTO = true;
            } else {
                PLAY_AUTO = false;
            }


            int maxLengthOfWord;
            int alphabetLength;


            ClassLoader classLoader = KNS.class.getClassLoader();
            in = new Scanner(System.in);
            System.out.println("Podaj maksymalną długość słowa");
            line = in.nextLine();
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
            System.out.println("3) Przeciwnik heurystyczny z heurystyką maksymalnej różnicy");
            System.out.println("4) Przeciwnik heurystyczny z heurystyką sumy");

            line = in.nextLine();
            int opponent = 0;
            if (line != null) {
                try {
                    opponent = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Złe dane wejściowe, żeby wybrać przeciwnika podaj liczbę z odpowiedniego zakresu");
                    return;
                }
                if (opponent < 1 || opponent > 4) {
                    System.out.println("Złe dane wejściowe, żeby wybrać przeciwnika podaj liczbę z odpowiedniego zakresu");
                    return;
                }
                if (opponent == 1)
                    System.out.println("Wybrano przeciwnika losowego");
                if (opponent == 2)
                    System.out.println("Wybrano przeciwnika brue force");
                if (opponent == 3) {
                    System.out.println("Wybrano przeciwnika heurystycznego z heurystyką minimalnej różnicy");
                }
                if (opponent == 4) {
                    System.out.println("Wybrano przeciwnika heurystycznego z heurystyką sumy");
                }
            }

            if (PLAY_AUTO) {
                AUTO_OPPONENT = opponent;
                AUTO_ALPHABET_SIZE = alphabetLength;
                AUTO_MAX_WORD_LENGTH = maxLengthOfWord;


                System.out.println("Podaj liczbę gier jakie ma zostać rozegrana");
                AUTO_NUMBER_OF_GAMES = Integer.parseInt(in.nextLine());
                int wins = 0;
                int losses = 0;
                int sumOfLosses = 0;
                int[] results = new int[AUTO_NUMBER_OF_GAMES];
                for (int gameNumber = 0; gameNumber < AUTO_NUMBER_OF_GAMES; gameNumber++) {
                    if (gameNumber % 10 == 0)
                        System.out.println("Gra numer: " + gameNumber);
                    char[] autoAlphabet = new char[AUTO_ALPHABET_SIZE];
                    for (int i = 0; i < AUTO_ALPHABET_SIZE; i++) {
                        if (i < 25)
                            autoAlphabet[i] = (char) (i + 65);
                        else autoAlphabet[i] = (char) (i + 72);
                    }

                    Game game = new Game();
                    int result = game.game(AUTO_MAX_WORD_LENGTH, autoAlphabet, AUTO_OPPONENT, true);
                    if (result == 1) {
                        wins++;
                        results[gameNumber] = AUTO_MAX_WORD_LENGTH;
                    } else {
                        losses++;
                        sumOfLosses += result;
                        results[gameNumber] = result;
                    }
                }
                System.out.println("Gier wygranych przez komputer heurystyczny: " + wins);
                System.out.println("Wynik średni gier przegranych: " + (sumOfLosses / losses));
                System.out.println("Najgorszy wynik: " + Arrays.stream(results).min().getAsInt());
                System.out.println("Najlepszy wynik: " + Arrays.stream(results).max().getAsInt());
                System.out.println("Wariancja: " + variance(results, AUTO_NUMBER_OF_GAMES));
            } else {
                Game game = new Game();
                game.game(maxLengthOfWord, alphabet, opponent, false);
            }
            System.out.println("Zakończyć grę? (y -tak)");
            if (in.nextLine().equals("y")) {
                continueGame = false;
            }
        }

    }

    static double variance(int a[],
                           int n) {
        // Compute mean (average
        // of elements)
        double sum = 0;

        for (int i = 0; i < n; i++)
            sum += a[i];
        double mean = (double) sum /
                (double) n;

        // Compute sum squared
        // differences with mean.
        double sqDiff = 0;
        for (int i = 0; i < n; i++)
            sqDiff += (a[i] - mean) *
                    (a[i] - mean);

        return (double) sqDiff / n;
    }
}

