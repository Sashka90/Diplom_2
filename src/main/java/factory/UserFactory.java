package factory;

import net.datafaker.Faker;
import user.User;

public class UserFactory {

    public static Faker faker = new Faker();

    public static User getRandomDefaultUser() {
        String name = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        return new User(name, email, password);
    }

    public static User getUserWithoutEmail() {
        String name = faker.name().username();
        String email = "";
        String password = faker.internet().password();

        return new User(name, email, password);
    }

    public static User getUserWithIncorrectLogin() {
        String name = faker.name().username();
        String email = "ololololo";
        String password = faker.internet().password();

        return new User(name, email, password);
    }
}
