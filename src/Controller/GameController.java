package Controller;

import Model.GameModel;
import Model.Player;
import View.GameView;

public class GameController {
    private GameModel model;
    private GameView view;

    public static final int MAX_PLAYER_COUNT = 4; // 최대 플레이어 수 (본인 포함)

    private Player[] players = new Player[MAX_PLAYER_COUNT];

    private Tile

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }


}
