import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Random;
import java.util.Scanner;



public class Game {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public void game(int maxLengthOfWord, char[] alphabet) {
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
            String prefix = word.substring(0, positionNumber - 1);
            String suffix = word.substring(positionNumber - 1);
            Random random = new Random();
            random.nextInt(alphabet.length);
            word = prefix + alphabet[random.nextInt(alphabet.length)] + suffix;
            if(checkWinner(word, alphabet)) {
//                System.out.println("Ostateczne słowo to: " + word);
//                System.out.println("Wygrałeś");
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

    private boolean checkWinner(String word, char[] alphabet) {
//        System.out.println("####################################################");
//        System.out.println("Sprawdzanie słowa: " + word);
        //i mówi o początku słowa
        for (int i = 0; i < word.length(); i++) {
            //j mówi o długości słowa
            for (int j = 1; j < word.length()/2 + 1; j++) {
                if (j*2 + i <= word.length()) {
                    Multiset<String> firstWord = HashMultiset.create();
                    Multiset<String> secondWord = HashMultiset.create();
                    for (int k = i; k < i + j; k++) {
                        firstWord.add(String.valueOf(word.charAt(k)));
                    }
                    for (int k = i + j; k < i + 2*j; k++) {
                        secondWord.add(String.valueOf(word.charAt(k)));
                    }
                    if (firstWord.containsAll(secondWord)) {
                        System.out.println("Wygrałeś");
                        System.out.println("Ostateczne słowo to: " + word.substring(0, i) + ANSI_GREEN +  word.substring(i, i+j) + ANSI_YELLOW + word.substring(i+j, i + 2*j) + ANSI_RESET + word.substring(i + 2*j));
                        System.out.println("Znalezioną abelową repetycją jest: " + word.substring(i, i+j) + " oraz " + word.substring(i+j, i + 2*j));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
