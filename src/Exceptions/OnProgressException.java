package Exceptions;

public class OnProgressException extends Exception{
    public OnProgressException() {
        super("현재 게임이 진행중인 방입니다.");
    }
}
