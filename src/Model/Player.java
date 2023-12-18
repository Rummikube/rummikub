package Model;

import Controller.ClientHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 루미큐브 플레이어의 내용을 담근 객체

public class Player implements Serializable{

    public enum ReadyState{
        READY, NOT_READY;
    }
    private String name; // 유저의 이름
    private List<Tile> tiles; // 유저가 가지고 있는 타일들

    private ReadyState readyState = ReadyState.NOT_READY;

    // 유저 클래스의 생성자
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>(); // 타일 리스트 초기화
    }

    // 이름을 반환하는 메서드
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    public void setReadyState(ReadyState changeReadyState){
        this.readyState = changeReadyState;
    }

    public ReadyState getReadyState(){
        return readyState;
    }


    @Override
    public String toString() {
        return "name : " + name + " ReadyState : " + this.readyState;
    }
    
//    public PlayerDTO toDTO(){
//        return new PlayerDTO(this.name, this.readyState);
//    }
}
