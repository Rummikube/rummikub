package Controller;

import Model.GameModel;
import Model.Player;
import View.GameView;


// 서버 플레이어가 게임 진행을 관리
public class GameController {
    private GameModel model;
    private GameView view;

    public static final int MAX_PLAYER_COUNT = 4; // 최대 플레이어 수 (본인 포함)
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 7;

    public static final int HAND_HEIGHT = 2;
    public static final int HAND_WIDTH = 10;


    private Player[] players = new Player[MAX_PLAYER_COUNT];

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void start(){
        view.startUI();
    }


}
