package Model;

import Controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    // 게임 진행 플레이어들
    // 현재 보드판
    private Board board;
    // 현재 턴인 플레이어의 인덱스 번호
    private int currentPlayerIndex;

    private int playerCount = 0;

    private TileDeck tileDeck;

    private Player[] players;

    public TileDeck getTileDeck() {
        return tileDeck;
    }

    public Game(Player[] players){
        this.players = players;
        this.tileDeck = new TileDeck();
        this.board = new Board();
        this.currentPlayerIndex = 0;
        playerCount = getPlayerCount();
        setStartPlayerIndex();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    // 현재 플레이어 수
    public int getPlayerCount(){
        int curCnt = 0;
        for(int i = 0 ; i < GameController.MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                curCnt ++;
            }
        }
        return curCnt;
    }

    // 처음 시작할 사람 랜덤으로 결정
    public void setStartPlayerIndex(){
        int startIdx = new Random().nextInt(playerCount);
        for(int i = 0 ; i < GameController.MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                if(startIdx == 0){
                    currentPlayerIndex = i;
                    return;
                }
                startIdx --;
            }
        }
    }



//    // 게임 시작 메서드
//    public void start(){
//
//        playerCount = getPlayerCount();
//
//        currentPlayerIndex = getStartPlayerIndex();
//
//        manageGameProgress();
//
//
//    }

//    // 게임 진행 상태를 관리하는 메서드
//    public void manageGameProgress() {
//        while (true) {
//            Player currentPlayer = players[currentPlayerIndex];
//            performPlayerTurn(currentPlayer);
//
//
//            // 게임이 끝난 경우
//            if (checkWinCondition(currentPlayer)) {
//                //TODO
//                break;
//            }
//
//
//            // 다음 턴으로 변경
//            int curIdx = currentPlayerIndex + 1;
//
//            while(true){
//                if(curIdx > GameController.MAX_PLAYER_COUNT) curIdx %= GameController.MAX_PLAYER_COUNT;
//                if(players[curIdx] != null){
//                    currentPlayerIndex = curIdx;
//                    break;
//                }
//                curIdx ++;
//            }
//
//        }
//    }

    private void getNextTurnPlayerIndex(){
        int curIdx = currentPlayerIndex + 1;

            while(true){
                if(curIdx > GameController.MAX_PLAYER_COUNT) curIdx %= GameController.MAX_PLAYER_COUNT;
                if(players[curIdx] != null){
                    currentPlayerIndex = curIdx;
                    return;
                }
                curIdx ++;
            }
    }

}
