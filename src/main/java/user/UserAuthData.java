package user;

public class UserAuthData {

    private final String email;
    private final String password;

    public UserAuthData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserAuthData from(User user) {
        return new UserAuthData(user.getEmail(), user.getPassword());
    }


}
