package main.java;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class KNS {

    public static void main(String[] args) {
        int maxLengthOfWord;
        int alphabetLength;
        try {
            ClassLoader classLoader = KNS.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("inputfile.txt");
            InputStreamReader streamReader =
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
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
            line = reader.readLine();
            if (line != null) {
                try {
                    alphabetLength = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Złe dane wejściowe, długość alfabetu powinna być dodatnią liczbą naturalną, mniejszą od 50");
                    return;
                }
                if (alphabetLength < 1 || alphabetLength > 49) {
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

            reader.close();
            Game game = new Game();


            game.game(maxLengthOfWord, alphabet);


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}