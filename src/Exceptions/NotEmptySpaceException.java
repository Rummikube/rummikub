package Exceptions;

public class NotEmptySpaceException extends Exception{
    public NotEmptySpaceException(){
        super("현재 방이 가득찬 상태입니다.");
    }
}
