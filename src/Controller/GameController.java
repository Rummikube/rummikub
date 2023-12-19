package Controller;

import Exceptions.NotEmptySpaceException;
import Exceptions.OnProgressException;
import Model.*;
import View.GameView;

import javax.swing.*;
import java.util.*;

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

    public Tile[][] handTiles;

    public Board boardTiles;
    private boolean isTurnProgress = false;
    private boolean isMyTurn = false;

    private Player[] players = new Player[MAX_PLAYER_COUNT];

    public String playersStr() {
        String tmp = "";
        for(int i = 0 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] == null){
                tmp += "null\n";
            }
            else {
                tmp += players[i].toString() + " ";
                tmp += view.getRoomNameLabel()[i].getText() + "\n";
            }
        }
        return tmp;
    }

    public GameController(GameView view) {
        this.view = view;
        view.setGameController(this);
    }

    public void setIndex(int index) {
        this.index = index;
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

    // 메인 게임 함수
    public void serverStartGame(){
        gameState = GameState.IN_PROGRESS;
        // 게임 관리 객체 생성
        Game game = new Game(players);
        // 게임 타일 객체 생성

        int playerCnt = game.getPlayerCount();
        // 플레이어들에게 첫 손패 타일 전달
        distributeTiles(game.getTileDeck());
        updateHandTiles();
        boardTiles = game.getBoard();
        updateBoardTiles();
        server.notifiObservers(new SerializeObject(boardTiles, "Board", 0), 0);


        // 클라이언트들에게 게임 시작을 알림
        server.notifiObservers(new SerializeObject("startGame", "String", 0), 0);

        System.out.println("게임 패널 변경");
        view.changePanel("GamePanel");



        manageGameProcess(game);

        for(int i = 1 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                players[i].setReadyState(Player.ReadyState.NOT_READY);
            }
        }

        server.notifiObservers(new SerializeObject(players, "Player[]", 0), 0);

        server.notifiObservers(new SerializeObject("finishGame", "String", 0), 0);
        view.updatePlayers(players);
        view.changePanel("RoomPanel");
    }

    public void startTurn(){
        isMyTurn = true;
        Tile[][] lastBoard = boardTiles.getLastBoardState();

    }



    public void manageGameProcess(Game game){
        while(true){
            isTurnProgress = true;
            server.notifiObservers(new SerializeObject(game.getCurrentPlayerIndex(), "Turn", 0), 0);
            if(game.getCurrentPlayerIndex() == 0){
                startTurn();
            }

            while(isTurnProgress){
            }

            if(canFinish(game.getTileDeck())) break;
        }
    }

    public boolean canFinish(TileDeck tileDeck){
        if(tileDeck.isEmpty()) return true;
        for(int i = 0 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null && players[i].getTileCount() == 0) return true;
        }
        return false;
    }

    public void distributeTiles(TileDeck tileDeck){
        handTiles = tileDeck.getFirstHandTiles(HAND_HEIGHT, HAND_WIDTH);
        for(int i = 1 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                server.notifiObservers(new SerializeObject(tileDeck.getFirstHandTiles(HAND_HEIGHT, HAND_WIDTH), "Hand", i), 0);
                server.notifiObservers(new SerializeObject(TileDeck.TILES_PER_PLAYER, "TileCount", i), 0);
                players[i].setTileCount(TileDeck.TILES_PER_PLAYER);
            }
        }
    }

    public void clientStartGame(){

    }


    // 핸드 UI 적용
    public void updateHandTiles(){
        System.out.println("타일 UI 업데이트");
        for(int i = 0 ; i < HAND_HEIGHT ; i ++){
            for(int j = 0 ; j < HAND_WIDTH ; j ++){
                Tile cur = handTiles[i][j];
                if(cur == null) {
                    view.setTransParentPanel(i, j, true);
                    continue;
                }
                view.updateTile(cur.getColor(), cur.getNumber(), i, j);
            }
        }
    }

    // 보드 UI 적용
    public void updateBoardTiles(){
        Tile[][] tiles = boardTiles.getTiles();
        System.out.println("보드 UI 업데이트");
        for(int i = 0 ; i < BOARD_HEIGHT ; i ++){
            for(int j = 0 ; j < BOARD_WIDTH ; j ++){
                Tile cur = tiles[i][j];
                if(cur == null){
                    view.setTransParentPanel(i, j, false);
                    continue;
                }
                view.updateTile(cur.getColor(), cur.getNumber(), i, j);
            }
        }
    }

    public void sortByRun(){
        ArrayList<Tile> tmp = new ArrayList<>();
        for(int i = 0 ; i < HAND_HEIGHT ; i ++){
            for(int j = 0 ; j < HAND_WIDTH ; j ++){
                if(handTiles[i][j] != null){
                    tmp.add(handTiles[i][j]);
                }
            }
        }

        Collections.sort(tmp, (tile1, tile2) -> Integer.compare(tile1.getNumber(), tile2.getNumber()));
        int idx = 0;
        for(int i = 0 ; i < HAND_HEIGHT * HAND_WIDTH ; i ++){
            if(i < tmp.size()){
                handTiles[i / HAND_WIDTH][i % HAND_WIDTH] = tmp.get(i);
            }
            else{
                handTiles[i / HAND_WIDTH][i % HAND_WIDTH] = null;
            }
        }

        updateHandTiles();
    }

    public void sortByGroup(){
        ArrayList<Tile> tmp = new ArrayList<>();
        for(int i = 0 ; i < HAND_HEIGHT ; i ++){
            for(int j = 0 ; j < HAND_WIDTH ; j ++){
                if(handTiles[i][j] != null){
                    tmp.add(handTiles[i][j]);
                }
            }
        }

        List<String> customOrder = Arrays.asList("Red", "Orange", "Blue", "Black", "Bugi");

        Comparator<Tile> customComparator = (tile1, tile2) -> {
            int order1 = customOrder.indexOf(tile1.getColor());
            int order2 = customOrder.indexOf(tile2.getColor());

            if (order1 != order2) {
                return Integer.compare(order1, order2);
            } else {
                return Integer.compare(tile1.getNumber(), tile2.getNumber());
            }
        };
        Collections.sort(tmp, customComparator);

        int idx = 0;
        for(int i = 0 ; i < HAND_HEIGHT * HAND_WIDTH ; i ++){
            if(i < tmp.size()){
                handTiles[i / HAND_WIDTH][i % HAND_WIDTH] = tmp.get(i);
            }
            else{
                handTiles[i / HAND_WIDTH][i % HAND_WIDTH] = null;
            }
        }

        updateHandTiles();
    }


    // 클라이언트로 넘어온 쿼리 실행 함수
    public void excuteQuery(SerializeObject object){
        System.out.println("받은 객체 : " + object.getEventObject());
        switch (object.getObjectType()){
            case "String" :
                String content = (String)object.getEventObject();
                // 게임 시작
                if(content.equals("startGame")){
                    view.changePanel("GamePanel");
                }
                else if(content.equals("finishTurn")){
                    isTurnProgress = false;
                    if(isServer) server.notifiObservers(new SerializeObject("finishTurn", "String", index), 0);
                }
                else if(content.equals("finishGame")){
                    // 게임 끝 TODO
                }
                break;

            case "Player[]":
                Player[] getPlayers = (Player[]) object.getEventObject();
                changePlayers(getPlayers);
                System.out.println(playersStr());
                break;

            // 타일 첫 손패
            case "Hand":
                if(object.getIndex() == index) {
                    handTiles = (Tile[][]) object.getEventObject();
                    updateHandTiles();
                }
                break;

            case "Board":
                boardTiles = (Board) object.getEventObject();
                updateBoardTiles();
                break;

            case "Turn":
                if((int)object.getEventObject() == index){
                    startTurn();
                }
                else{
                    // 스레드로 30초 타이머
                }
                break;

            case "Name":
                if(isServer) {
                    System.out.println(object.getEventObject() + " 플레이어 추가");
                    String name = (String) object.getEventObject();
                    int curIndex = object.getIndex();
                    players[curIndex].setName(name);
                    view.updatePlayers(players);
                    System.out.println(players + " " + object.getIndex());
                    server.notifiObservers(new SerializeObject(Arrays.copyOf(players, MAX_PLAYER_COUNT), "Player[]", object.getIndex()), 0);
                }
                break;

            case "Index" :
                this.index = (int) object.getEventObject();
                break;

            case "ReadyState" :
                    Player.ReadyState readyState = (Player.ReadyState) object.getEventObject();
                    updateReadyState(object.getIndex(), readyState);
                    if(isServer) server.notifiObservers(new SerializeObject(readyState, "ReadyState", object.getIndex()), object.getIndex());
                    break;

            case "TileCount" :
                    int cnt = (int)object.getEventObject();
                    players[object.getIndex()].setTileCount(cnt);
                    if(isServer) server.notifiObservers(new SerializeObject(cnt, "TileCount", object.getIndex()), object.getIndex());
            default:
                break;
        }

    }

 }



