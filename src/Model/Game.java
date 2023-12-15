package Model;

import Controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private enum GameState{
        IN_PROGRESS, FINISHED
    }
    // 게임 진행 플레이어들
    private Player[] players;
    // 현재 보드판
    private Board board;
    // 현재 턴인 플레이어의 인덱스 번호
    private int currentPlayerIndex;
    private GameState gameState;

    private int playerCount = 0;

    public Game(){
        players = new Player[GameController.MAX_PLAYER_COUNT];
        this.board = new Board();
        this.currentPlayerIndex = 0;
        gameState = GameState.IN_PROGRESS;
    }


    // 처음 시작할 사람 랜덤으로 결정
    private int getStartPlayerIndex(){
        int startIdx = new Random().nextInt(playerCount);
        for(int i = 0 ; i < GameController.MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                if(startIdx == 0){
                    return i;
                }
                startIdx --;
            }
        }
        return -1;
    }

    // 게임 시작 메서드
    public void start(){

        // 플레이어 수 정하기
        playerCount = 0;
        for(int i = 0 ; i < GameController.MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                playerCount ++;
            }
        }

        currentPlayerIndex = getStartPlayerIndex();

        manageGameProgress();


    }

    // 게임 진행 상태를 관리하는 메서드
    public void manageGameProgress() {
        while (gameState == GameState.IN_PROGRESS) {
            Player currentPlayer = players[currentPlayerIndex];
            performPlayerTurn(currentPlayer);


            // 게임이 끝난 경우
            if (checkWinCondition(currentPlayer)) {
                //TODO
                gameState = GameState.FINISHED;
            }


            // 다음 턴으로 변경
            int curIdx = currentPlayerIndex + 1;

            while(true){
                if(curIdx > GameController.MAX_PLAYER_COUNT) curIdx %= GameController.MAX_PLAYER_COUNT;
                if(players[curIdx] != null){
                    currentPlayerIndex = curIdx;
                    break;
                }
                curIdx ++;
            }

        }
    }

    // 플레이어의 턴을 진행하는 메서드
    private void performPlayerTurn(Player player) {
        // 플레이어의 턴에 대한 로직 구현
        // 예: 타일 놓기, 조합 변경 등
    }

    // 승리 조건을 확인하는 메서드
    private boolean checkWinCondition(Player player) {
        // 승리 조건 확인 로직
        // 플레이어가 모든 타일을 사용하면 우승
        if(player.getTiles().size() == 0) return true;
        return false;
    }
}
