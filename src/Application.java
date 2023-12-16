import Controller.GameController;
import View.GameView;

public class Application {
    public static void main(String[] args) {
        GameView view = new GameView();
        GameController controller = new GameController(view);
        controller.start();
    }
}
