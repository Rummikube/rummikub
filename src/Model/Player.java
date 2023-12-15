package Model;

import java.util.ArrayList;
import java.util.List;

// 루미큐브 플레이어 객체
public class Player {
    private String name; // 유저의 이름
    private List<Tile> tiles; // 유저가 가지고 있는 타일들

    // 유저 클래스의 생성자
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>(); // 타일 리스트 초기화
    }

    // 이름을 반환하는 메서드
    public String getName() {
        return name;
    }

    // 유저가 가진 타일 리스트를 반환하는 메서드
    public List<Tile> getTiles() {
        return tiles;
    }

    // 타일을 유저의 타일 리스트에 추가하는 메서드
    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    // 특정 타일을 유저의 타일 리스트에서 제거하는 메서드
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    // 추가 유저 행동 메서드
}
