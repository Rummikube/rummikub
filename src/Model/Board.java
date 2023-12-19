package Model;

import Controller.GameController;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Board implements Serializable {
    final int BOARD_WIDTH = GameController.BOARD_WIDTH; // 보드판 가로 길이
    final int BOARD_HEIGHT = GameController.BOARD_HEIGHT; // 보드판 세로 길이

    private Tile[][] tiles;

    public Board(){
        tiles = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    // 변경이전의 보드 상태를 반환하는 함수
    public Tile[][] getLastBoardState(){
        Tile[][] newTiles = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
        for(int i = 0 ; i < BOARD_HEIGHT ; i ++){
            for(int j = 0 ; j < BOARD_WIDTH ; j ++){
                newTiles[i][j] = tiles[i][j];
            }
        }
        return newTiles;
    }


    // 타일을 보드판에 추가하는 메서드 => 보드판에 추가 가능하면 true, 불가능하면 false 반환
    public boolean canAddTile(Tile tile, int tileRow, int tileCol) {
        if(tiles[tileRow][tileCol] == null){
            tiles[tileRow][tileCol] = tile;
            return true;
        }
        else return false;
    }

    // 타일을 보드판에서 제거하는 메서드
    // 제거되는 타일 반환
    public Tile removeTile(int tileRow, int tileCol) {
        Tile tile = tiles[tileRow][tileCol];
        tiles[tileRow][tileCol] = null;
        return tile;
    }

    // 보드판의 타일 조합이 유효한지 검증하는 메서드
    public boolean validateCombination() {
        // 조합 검증 로직 구현
        int start = -1;
        for(int i = 0 ; i < BOARD_HEIGHT ; i ++){
            for(int j = 0 ; j < BOARD_WIDTH ; j ++) {
                if (start == -1 && tiles[i][j] != null) { // 타일 시작 부분
                    start = j;
                }
                else if(start != -1 && tiles[i][j] == null){ // 타일의 끝 부분
                    if(!isRun(start, j - 1, i) && !isGroup(start, j - 1, i)) return false;
                    start = -1;
                }
            }
            if(start != -1){
                if(!isRun(start, BOARD_WIDTH - 1, i) && !isGroup(start, BOARD_WIDTH - 1, i)) return false;
            }
        }
        return true; // 반환값
    }


    // 타일 세트가 Run 상태인지
    private boolean isRun(int start, int end, int row){
        if(end - start + 1 < 3) return false;
        String color = null;
        int num = -1;
        for(int i = start ; i <= end ; i ++){
            Tile cur = tiles[row][i];
            if(color == null && num == -1){
                color = cur.getColor();
                num = cur.getNumber();
            }
            else{
                if(!color.equals(cur.getColor()) || num + 1 != cur.getNumber()){ // 색이 다르거나 번호가 Run이 아닌 경우
                    return false;
                }
            }
        }
        return true;
    }

    // 타일 세트가 Group 상태인지
    private boolean isGroup(int start, int end, int row){
        if(end - start + 1 < 3) return false;
        Set<String> set = new HashSet<>();
        int num = -1;
        for(int i = start ; i <= end ; i ++){
            Tile cur = tiles[row][i];
            if(num == -1) num = cur.getNumber();
            else {
                if(cur.getNumber() != num) return false; // 번호가 다른 경우
            }
            if(!set.contains(cur.getColor())){
                set.add(cur.getColor());
            }
            else{ // 색상이 같은것이 있는 경우
                return false;
            }
        }
        return true;
    }
}
