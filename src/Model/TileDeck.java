package Model;

import java.util.*;

// 타일 전체를 관리하는 클래스
public class TileDeck {
    private List<Tile> tiles;

    public static final int TILES_PER_PLAYER = 14; // 플레이어당 타일 수

    public TileDeck() {
        this.tiles = new ArrayList<>();
        initializeTiles(); // 모든 타일을 추가한 후,
        shuffleTiles(); // 타일을 섞는다
    }

    // 풀에 남은 타일이 있는 지 확인하는 함수
    public boolean isEmpty() {
        if (tiles.isEmpty()) return true;
        else return false;
    }

    private void initializeTiles() {
        // 색상별로 1 ~ 13까지의 타일을 추가
        for (int i = 0; i < 4; i++) {
            String color = null;
            switch (i) {
                case 0:
                    color = "Red";
                    break;
                case 1:
                    color = "Orange";
                    break;

                case 2:
                    color = "Blue";
                    break;

                case 3:
                    color = "Black";
                    break;
            }
            for(int j = 1 ; j <= 13 ; j ++){
                tiles.add(new Tile(j, color));
            }
        }

        // 두 개의 부기(조커) 타일 추가
        tiles.add(new Tile(14, "Bugi"));
        tiles.add(new Tile(14, "Bugi"));
    }

    // 풀에 있는 타일들을 섞는 함수
    public void shuffleTiles() {
        Collections.shuffle(tiles);
    }

    // Deck에서 하나의 타일을 뽑는 함수
    public Tile draw(){
        if(this.isEmpty()) return null;
        Tile tmp = tiles.get(0);
        tiles.remove(0);
        return tmp;
    }

    // 플레이어의 첫 핸드 타일
    public Tile[][] getFirstHandTiles(int row, int col){
        Tile[][] curTileList = new Tile[row][col];
        for(int i = 0 ; i < TILES_PER_PLAYER ; i ++){
            curTileList[i / col][i % col] = draw();
        }
        return curTileList;
    }

}
