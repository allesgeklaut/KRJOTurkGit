package service;

public class currentUser extends User{
    //    private static systemData instance = new systemData();
    private static User instance = new User();

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User instance) {
        currentUser.instance = instance;
    }
}
