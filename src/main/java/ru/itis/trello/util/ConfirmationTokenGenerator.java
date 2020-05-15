package ru.itis.trello.util;

import java.util.Random;

public class ConfirmationTokenGenerator {
    public static String generate() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 128;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
