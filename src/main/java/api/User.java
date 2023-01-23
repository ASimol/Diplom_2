package api;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class User {
    private String email;
    private String password;
    private String name;

    static Faker faker = new Faker(new Locale("en_EN"));

    public User() {
    }

    public User(String email, String password, String name) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public static User getRandomUser() {
        return new User().setRandomName().setRandomEmail().setRandomPassword();
    }

    public User setRandomName() {
        this.name = faker.name().username();
        return this;
    }

    public User setRandomEmail() {
        this.email = RandomStringUtils.randomAlphabetic(5) + "@ya.ru";
        return this;
    }

    public User setRandomPassword() {
        this.password = RandomStringUtils.randomAlphabetic(5);
        return this;
    }
}
