import Controller.GameController;
import Model.GameModel;
import View.GameView;

public class Application {
    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
        controller.start();
    }
}
