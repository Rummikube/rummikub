public class Board {
    final int BOARD_WIDTH = 20; // 보드판 가로 길이
    final int BOARD_HEIGHT = 10; // 보드판 세로 길이

    private Tile[][] tiles;

    public Board(){
        tiles = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    }

    // 타일을 보드판에 추가하는 메서드 => 보드판에 추가 가능하면 true, 불가능하면 false 반환
    public boolean addTile(Tile tile, int tileRow, int tileCol) {
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
        return true; // 반환값
    }

    // 보드판 상태를 출력하는 메서드
    public void displayBoard() {
        // 보드판 상태 출력 로직
    }
}
