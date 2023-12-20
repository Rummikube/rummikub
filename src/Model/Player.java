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

    private ReadyState readyState = ReadyState.NOT_READY;

    private int tileCount = -1;

    // 유저 클래스의 생성자
    public Player(String name) {
        this.name = name;
    }

    public void setTileCount(int tileCount) {
        this.tileCount = tileCount;
    }

    public int getTileCount() {
        return tileCount;
    }

    // 이름을 반환하는 메서드
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
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
