package main.java;

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
                showPositions = showPositions + word.charAt(i) + (i+2);
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
            checkCorrectnessOfTheWord(word, alphabet);
        }
        System.out.println("Ostateczne słowo to: " + word);
    }

    private int checkCorrectnessOfTheWord(String word, char[] alphabet ) {
        return 0;
    }
}
