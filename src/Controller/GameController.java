package Controller;

import Model.Player;
import View.GameView;

import java.awt.*;
import java.net.UnknownHostException;


// 서버 플레이어가 게임 진행을 관리
public class GameController {
    private GameView view;

    public static final int PORT = 12345; // 포트 번호

    private boolean isServer = false; // 현재 서버로 운영되는 사용자인지
    private Server server = null;
    private Client client = null;

    public static final int MAX_PLAYER_COUNT = 4; // 최대 플레이어 수 (본인 포함)
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 7;

    public static final int HAND_HEIGHT = 2;
    public static final int HAND_WIDTH = 10;


    private Player[] players = new Player[MAX_PLAYER_COUNT];

    public GameController(GameView view) {
        this.view = view;
        view.setGameController(this);
    }

    public void start(){
        view.startUI();
    }


    // 방 만드는 함수 - 서버
    public void makeRoom() {
        server = new Server(PORT);
        isServer = true;
        // 뷰에서 화면 전환함수
        view.changePanel("RoomPanel");
    }


    public boolean connectRoom(String address) {
        client = new Client();
        isServer = false;
        try {
            client.connect(address, PORT);
            CardLayout cardLayout = (CardLayout) view.getFrame().getContentPane().getLayout();
            cardLayout.next(view.getFrame().getContentPane());
        } catch (UnknownHostException e) {
            return false;
        }
        return true;
    }
 }



