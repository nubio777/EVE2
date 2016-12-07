package coe.ahr;


/**
 * Created by jesus on 11/13/15.
 */
public enum User {
    user1(1, "user1", "paso123"), admin(2, "admin", "E$muyDificil0!");

    private int id;
    private String name;
    private String pwd;

    private User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public static boolean isValid(String userName, String pwd) {
        for (User user : User.values()) {
            if (userName != null && userName.equals(user.getName()) && pwd != null && pwd.equals(user.getPwd()))
                return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(user1);
        System.out.println(admin);
    }
}
