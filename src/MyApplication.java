import controllers.UserController;

public class MyApplication {
    private final UserController controller;

    public MyApplication(UserController controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.println(controller.createUser("Alice", "Smith", 28, false, 987654321, 1500, 75, 300));
    }
}
