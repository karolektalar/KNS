import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Random;
import java.util.Scanner;

public class Game {
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
                System.out.println("Przegrałeś");
                return;
            }


        }
        System.out.println("Ostateczne słowo to: " + word);
        System.out.println("Wygrałeś");
    }

    private boolean checkWinner(String word, char[] alphabet) {
        System.out.println("####################################################");
        System.out.println("Sprawdzanie słowa: " + word);
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
                    System.out.println(firstWord);
                    System.out.println(secondWord);
                    System.out.println("####################################################");
                    if (firstWord.containsAll(secondWord)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
