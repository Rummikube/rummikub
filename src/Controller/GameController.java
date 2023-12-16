package Controller;

import Model.Player;
import Model.Server;
import View.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;


// 서버 플레이어가 게임 진행을 관리
public class GameController {
    private GameView view;

    public static final int PORT = 12345; // 포트 번호

    private boolean isServer = false; // 현재 서버로 운영되는 사용자인지
    private Server server = null;

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
    }




}


