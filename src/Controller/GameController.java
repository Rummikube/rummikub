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

    private int index = -1;

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

    public String playersStr() {
        String tmp = "";
        for(int i = 0 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] == null){
                tmp += "null\n";
            }
            else {
                tmp += players[i].toString() + "\n";
                tmp += view.getRoomNameLabel()[i].getText() + "\n";
            }
        }
        return tmp;
    }

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


    public int getIndex(){
        return index;
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

    // 방 만드는 함수 - 서버
    public void makeRoom() {
        players[0] = new Player(view.getNameTF().getText());
        players[0].setReadyState(Player.ReadyState.READY);
        index = 0;
        view.updatePlayers(players);
        server = new Server(PORT, this);
        isServer = true;
        // 뷰에서 화면 전환함수
        view.changePanel("RoomPanel");
    }


    public void connectRoom(String address) {
        view.getLoginErrorLabel().setText("연결 중 입니다...");
        isServer = false;
        String name = view.getNameTF().getText();
        try {
            client = new Client(this, address, PORT, name);
            view.changePanel("RoomPanel");
        } catch (UnknownHostException e) {
            view.getLoginErrorLabel().setText("IP주소로 접속할 수 없습니다. IP주소를 다시 확인해주세요.");
        } catch (NotEmptySpaceException e){
            view.getLoginErrorLabel().setText("현재 방 인원이 가득 차 참가할 수 없습니다.");
        } catch (OnProgressException e){
            view.getLoginErrorLabel().setText("현재 게임이 진행 중인 방입니다.");
        }

    }


    // 플레이어 ready 상태 변경 함수
    public void updateReadyState(int index, Player.ReadyState readyState){
        // ready
        if(readyState == Player.ReadyState.READY && players[index].getReadyState() == Player.ReadyState.NOT_READY){
            players[index].setReadyState(Player.ReadyState.READY);
        }
        // ready 해제
        else if(readyState == Player.ReadyState.NOT_READY && players[index].getReadyState() == Player.ReadyState.READY){
            players[index].setReadyState(Player.ReadyState.NOT_READY);
        }
        view.updateRoomReadyPanel(players[index].getReadyState(), index);
    }

    public void changeReadyState(){
        players[index].setReadyState(players[index].getReadyState() == Player.ReadyState.READY ? Player.ReadyState.NOT_READY : Player.ReadyState.READY);
        view.updateRoomReadyPanel(players[index].getReadyState(), index);
        if(isServer){
            server.notifiObservers(new SerializeObject(players[index].getReadyState(), "ReadyState", index), index);
        }
        else{
            client.sendObject(new SerializeObject(players[index].getReadyState(), "ReadyState", index));
        }
    }


    // 게임 패널로 전환
    public void changeGamePanel(){
        // TODO 데이터 전송 및 UI 연동
        view.changePanel("GamePanel");
    }

    // 서버로 플레이어 추가시 서버 뷰 추가
    public void updatePlayers(){
        view.updatePlayers(players);
    }

    // 플레이어 정보 변경 함수
    public void changePlayers(Player[] getPlayers){
        // Model 플레이어 변경
        players = getPlayers;

        // 플레이어 정보 UI 적용
        view.updatePlayers(players);
    }

    // 모든 플레이어가 Ready 상태인지 체크
    public boolean checkReady(){
        for(int i = 0 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null && players[i].getReadyState() == Player.ReadyState.NOT_READY){
                return false;
            }
        }
        return true;
    }


    // 클라이언트로 넘어온 쿼리 실행 함수
    public void excuteQuery(SerializeObject object){
        System.out.println("받은 객체 : " + object.getEventObject());
        switch (object.getObjectType()){
            case "String" :
                String content = (String)object.getEventObject();

                break;

            case "Player[]":
                Player[] getPlayers = (Player[]) object.getEventObject();
                changePlayers(getPlayers);
                break;

            case "Name":
                if(isServer) {
                    System.out.println(object.getEventObject() + " 플레이어 추가");
                    String name = (String) object.getEventObject();
                    players[index].setName(name);
                    view.updateNameLabel(name, index);
                    System.out.println(playersStr());
                    server.notifiObservers(new SerializeObject(players, "Player[]", object.getIndex()), -1);
                }
                break;

            case "Index" :
                this.index = (int) object.getEventObject();
                break;

            case "ReadyState" :
                    Player.ReadyState readyState = (Player.ReadyState) object.getEventObject();
                    updateReadyState(object.getIndex(), readyState);
                    if(isServer) server.notifiObservers(new SerializeObject(readyState, "ReadyState", object.getIndex()), index);
                    break;
            default:
                break;
        }

    }

 }



