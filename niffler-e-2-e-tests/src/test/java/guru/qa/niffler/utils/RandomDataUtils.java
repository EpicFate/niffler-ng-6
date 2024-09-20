package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomFunnyName() {
        return faker.funnyName().name();
    }

    public static String randomName() {
        return faker.name().name();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.funnyName().name();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount).trim();
    }

    public static String randomPassword() {
        return faker.number().digits(5);
    }
}