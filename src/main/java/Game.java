import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public int game(int maxLengthOfWord, char[] alphabet, int opponent, boolean auto) {
        String word = "";
        if (!auto) {
            System.out.println("Przekazany alfabet to: ");
            for (int i = 0; i < alphabet.length; i++) {
                System.out.print(alphabet[i] + " ");
            }
            System.out.println("");
            System.out.println("Zaczynajmy");
        }
        int round = 0;
        while (word.length() < maxLengthOfWord) {
            round++;
            if (round % 100 == 0) {
                System.out.println(word);
            }
            if (!auto) {
                System.out.println("Nasze słowo wygląda następująco:");
                System.out.println(word);
                System.out.println("Wybierz miejsce w słowie w którym komputer ma postawić literę");
            }
            String showPositions = "1";
            for (int i = 0; i < word.length(); i++) {
                showPositions = showPositions + word.charAt(i) + (i + 2);
            }
            int positionNumber;
            if (auto) {
                Random random = new Random();
                positionNumber = 1 + random.nextInt(word.length() + 1);
            } else {
                System.out.println(showPositions);
                Scanner in = new Scanner(System.in);
                String position = in.nextLine();

                try {
                    positionNumber = Integer.parseInt(position);
                } catch (NumberFormatException e) {
                    System.out.println("Wybrana pozycja musi być liczbą");
                    continue;
                }
                if (positionNumber < 1 || positionNumber > word.length() + 1) {
                    System.out.println("Wybrana pozycja musi być liczbą z zakresu 1 - " + (word.length() + 1));
                    continue;
                }
            }
            if (opponent == 1)
                word = randomTactics(word, positionNumber, alphabet);

            if (opponent == 2)
                word = bruteForce(word, positionNumber, alphabet, 0);
            if (opponent == 3)
                word = heuristics(word, positionNumber, alphabet);
            if (opponent == 4)
                word = heuristicsSum(word, positionNumber, alphabet);

            if (checkWinner(word, !auto)) {
                if (!auto) {
                    System.out.println("Naciśnij enter, aby zakończyć");
                    Scanner in = new Scanner(System.in);
                    String tmp = in.nextLine();
                }
                return word.length();
            }


        }
        if (!auto) {
            System.out.println("Ostateczne słowo to: " + word);
            System.out.println("Przegrałeś");
        }
        return 1;
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
            return bruteForce(word, positionNumber, alphabet, depth + 1);
        } else {
            return heuristicsSum(word, positionNumber, alphabet);
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

    private String heuristics(String word, int positionNumber, char[] alphabet) {
        String maxWord = word;
        String tmpWord;
        int[] maxHeuristics = new int[word.length() + 1];
        for (int i = 0; i < word.length() + 1; i++)
            maxHeuristics[i] = 0;
        String prefix = word.substring(0, positionNumber - 1);
        String suffix = word.substring(positionNumber - 1);
        for (char c : alphabet) {
            tmpWord = prefix + c + suffix;
            int[] tmpHeuristics = countHeuristics(tmpWord, positionNumber);

            if (compareResults(tmpHeuristics, maxHeuristics)) {
                maxHeuristics = tmpHeuristics;
                maxWord = tmpWord;
            }
        }
        if (maxWord.equals(word)) {
            return prefix + alphabet[0] + suffix;
        }
        return maxWord;
    }

    private String heuristicsSum(String word, int positionNumber, char[] alphabet) {
        String maxWord = word;
        String tmpWord;
        int maxHeuristics = 0;
        String prefix = word.substring(0, positionNumber - 1);
        String suffix = word.substring(positionNumber - 1);
        for (char c : alphabet) {
            tmpWord = prefix + c + suffix;
            int[] tmpHeuristics = countHeuristics(tmpWord, positionNumber);

            if (compareResults(tmpHeuristics, maxHeuristics)) {
                maxHeuristics = Arrays.stream(tmpHeuristics).sum();
                maxWord = tmpWord;
            }
        }
        if (maxWord.equals(word)) {
            return prefix + alphabet[0] + suffix;
        }
        return maxWord;
    }

    private int[] countHeuristics(String word, int positionNumber) {
        int[] minDifferenceArray = new int[word.length()];
        for (int i = 0; i < word.length(); i++) {
            minDifferenceArray[i] = word.length();
        }
        for (int i = 0; i < positionNumber + 1; i++) {
            if (word.length() > 1) {
                for (int j = 1; j * 2 + i <= word.length(); j++) {
                    if (i + 2 * j + 1 < positionNumber)
                       continue;
                    int tmpDifference = 0;
                    Multiset<String> firstWord = HashMultiset.create();
                    Multiset<String> secondWord = HashMultiset.create();
                    for (int k = i; k < i + j; k++) {
                        firstWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (int k = i + j; k < i + 2 * j; k++) {
                        secondWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (String c : firstWord) {
                        if (secondWord.contains(c)) {
                            secondWord.remove(c, 1);
                        } else {
                            tmpDifference++;
                        }
                    }

                    if (tmpDifference < minDifferenceArray[j]) {
                        minDifferenceArray[j] = tmpDifference;
                    }
                }
            }
        }
        return minDifferenceArray;
    }

    private boolean compareResults(int[] candidate, int[] maxResult) {
        for (int i = 1; i < candidate.length; i++) {
            if (candidate[i] == 0) {
                return false;
            }
        }
        for (int i = 1; i < candidate.length; i++) {
            if (candidate[i] > maxResult[i])
                return true;
            if (candidate[i] < maxResult[i])
                return false;
        }
        return false;
    }

    private boolean compareResults(int[] candidate, int maxResult) {
        for (int i = 1; i < candidate.length; i++) {
            if (candidate[i] == 0) {
                return false;
            }
        }
        return Arrays.stream(candidate).sum() > maxResult;
    }

    private boolean checkWinner(String word, boolean print) {
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
                            System.out.println("Ostateczna długość słowa to: " + word.length());
                            System.out.println("Ostateczne słowo to: " + word.substring(0, i) + "|# " + word.substring(i, i + j) + " #|" + "|# " + word.substring(i + j, i + 2 * j) + " #|" + word.substring(i + 2 * j));
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
