import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.AnsiFormat;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Random;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class Game {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public void game(int maxLengthOfWord, char[] alphabet, int opponent) {
        String word = "";
        System.out.println("Przekazany alfabet to: ");
        for (int i = 0; i < alphabet.length; i++) {
            System.out.print(alphabet[i] + " ");
        }
        System.out.println("");
        System.out.println("Zaczynajmy");
        while (word.length() < maxLengthOfWord) {
            System.out.println("Nasze słowo wygląda następująco:");
            System.out.println(word);
            System.out.println("Wybierz miejsce w słowie w którym komputer ma postawić literę");
            String showPositions = "1";
            for (int i = 0; i < word.length(); i++) {
                showPositions = showPositions + word.charAt(i) + (i + 2);
            }
            System.out.println(showPositions);
            Scanner in = new Scanner(System.in);
            String position = in.nextLine();
            int positionNumber;
            try {
                positionNumber = Integer.parseInt(position);
            } catch (NumberFormatException e) {
                System.out.println("Wybrana pozycja musi być liczbą");
                continue;
            }
            if (positionNumber < 0 || positionNumber > word.length() + 1) {
                System.out.println("Wybrana pozycja musi być liczbą z zakresu 0 - " + (word.length() + 1));
                continue;
            }

            if (opponent == 1)
                word = randomTactics(word, positionNumber, alphabet);

            if (opponent == 2)
                word = bruteForce(word, positionNumber, alphabet, 0);
            if (opponent == 3)
                word = heuristics(word, positionNumber, alphabet);

            if (checkWinner(word, true)) {
                System.out.println("Naciśnij enter, aby zakończyć");
                String tmp = in.nextLine();
                return;
            }


        }
        System.out.println("Ostateczne słowo to: " + word);
        System.out.println("Przegrałeś");
        System.out.println("Naciśnij enter, aby zakończyć");
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
    }

    private String randomTactics(String word, int positionNumber, char[] alphabet) {
        String prefix = word.substring(0, positionNumber - 1);
        String suffix = word.substring(positionNumber - 1);
        Random random = new Random();
        random.nextInt(alphabet.length);
        word = prefix + alphabet[random.nextInt(alphabet.length)] + suffix;
        return word;
    }

    private String bruteForce(String word, int positionNumber, char[] alphabet, int depth) {
        String tmpWord = "";
        for (char c : alphabet) {
            String prefix = word.substring(0, positionNumber - 1);
            String suffix = word.substring(positionNumber - 1);
            tmpWord = prefix + c + suffix;
            if (bruteForceRecursion(tmpWord, alphabet, depth + 1)) {
                return tmpWord;
            }
        }

        if (depth < 5) {
//            System.out.println("Zmniejszenie głębokości");
            return bruteForce(word, positionNumber, alphabet, depth + 1);
        }
        else {
//            System.out.println("Random choice");
            return randomTactics(word, positionNumber, alphabet);
        }
    }

    private boolean bruteForceRecursion(String word, char[] alphabet, int depth) {
        boolean shouldReturn = true;
        if (depth < 6) {
            String tmpWord = "";
            for (char c : alphabet) {
                shouldReturn = true;
                for (int i = 1; i < word.length() + 1; i++) {
                    String prefix = word.substring(0, i - 1);
                    String suffix = word.substring(i - 1);
                    tmpWord = prefix + c + suffix;
                    if (shouldReturn) {
                        if (checkWinner(word, false)) {
                            shouldReturn = false;
                        } else {
                            if (!bruteForceRecursion(tmpWord, alphabet, depth + 1)) {
                                shouldReturn = false;
                            }
                        }
                    }
                }
                if (shouldReturn) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private String heuristics(String word,  int positionNumber, char[] alphabet) {
        String maxWord = word;
        String tmpWord;
        int maxHeuristics = 0;
        String prefix = word.substring(0, positionNumber - 1);
        String suffix = word.substring(positionNumber - 1);
        for (char c : alphabet) {
            tmpWord = prefix + c + suffix;
            int tmpHeuristics = countHeuristics(tmpWord);
            if (tmpHeuristics > maxHeuristics) {
                    maxHeuristics = tmpHeuristics;
                    maxWord = tmpWord;
            }
        }
        if (maxWord.equals(word)) {
            return prefix + alphabet[0] + suffix;
        }
        return maxWord;
    }

    private int countHeuristics(String word) {
        int minDifference = word.length();
        for (int i = 0; i < word.length(); i++) {
            for (int j = 1; j < word.length() / 2 + 1; j++) {
                if (j * 2 + i <= word.length()) {
                    int tmpDifference = 0;
                    Multiset<String> firstWord = HashMultiset.create();
                    Multiset<String> secondWord = HashMultiset.create();
                    for (int k = i; k < i + j; k++) {
                        firstWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (int k = i + j; k < i + 2 * j; k++) {
                        secondWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (String c: firstWord) {
                        if (secondWord.contains(c)){
                            if (j == 1) {
                                return 0;
                            }
                            secondWord.remove(c, 1);
                        } else {
                            tmpDifference++;
                        }
                    }

                    if (tmpDifference < minDifference) {
                        minDifference = tmpDifference;
                    }
                }
            }
        }
        return minDifference;
    }

    private boolean checkWinner(String word, boolean print) {
//        System.out.println("####################################################");
//        System.out.println("Sprawdzanie słowa: " + word);
        //i mówi o początku słowa
        for (int i = 0; i < word.length(); i++) {
            //j mówi o długości słowa
            for (int j = 1; j < word.length() / 2 + 1; j++) {
                if (j * 2 + i <= word.length()) {
                    Multiset<String> firstWord = HashMultiset.create();
                    Multiset<String> secondWord = HashMultiset.create();
                    for (int k = i; k < i + j; k++) {
                        firstWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (int k = i + j; k < i + 2 * j; k++) {
                        secondWord.add(String.valueOf(word.charAt(k)));
                    }
                    if (firstWord.equals(secondWord)) {
                        if (print) {
                            System.out.println("Wygrałeś");
                            System.out.println("Ostateczne słowo to: " + word.substring(0, i) + "|# " + word.substring(i, i + j)+ " #|" + "|# " + word.substring(i + j, i + 2 * j) + " #|" + word.substring(i + 2 * j));
                            System.out.println("Znalezioną abelową repetycją jest: " + word.substring(i, i + j) + " oraz " + word.substring(i + j, i + 2 * j));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
