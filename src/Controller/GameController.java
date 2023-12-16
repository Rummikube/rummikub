package Controller;

import Exceptions.NotEmptySpaceException;
import Exceptions.OnProgressException;
import Model.Player;
import View.GameView;

import java.net.UnknownHostException;

// 서버 플레이어가 게임 진행을 관리
public class GameController {

    public enum GameState{
        IN_PROGRESS, FINISHED
    }

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

    public static GameState gameState = GameState.FINISHED;

    public Player curPlayer;


    private Player[] players = new Player[MAX_PLAYER_COUNT];

    public GameController(GameView view) {
        this.view = view;
        view.setGameController(this);
    }

    public void start(){
        view.startUI();
    }

    public Player getCurPlayer() {
        return curPlayer;
    }


    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        GameController.gameState = gameState;
    }

    protected Player[] getPlayers() {
        return players;
    }

    protected void setPlayers(Player[] players) {
        this.players = players;
    }

    // 방 만드는 함수 - 서버
    public void makeRoom() {
        server = new Server(PORT, this);
        isServer = true;
        // 뷰에서 화면 전환함수
        view.changePanel("RoomPanel");
    }


    public void connectRoom(String address) {
        client = new Client();
        isServer = false;
        String name = view.getNameTF().getText();
        try {
            client.connect(address, PORT, name);
            view.changePanel("RoomPanel");
            curPlayer = new Player(name);
        } catch (UnknownHostException e) {
            view.getLoginErrorLabel().setText("IP주소로 접속할 수 없습니다. IP주소를 다시 확인해주세요.");
        } catch (NotEmptySpaceException e){
            view.getLoginErrorLabel().setText("현재 방이 가득 차 참가할 수 없습니다.");
        } catch (OnProgressException e){
            view.getLoginErrorLabel().setText("현재 게임이 진행 중인 방입니다.");
        }
    }
 }



