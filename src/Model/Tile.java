package Model;

import java.io.Serializable;

public class Tile implements Serializable {
    private int number; // 타일의 숫자 만약, 부기(조커)타일이라면 -1, -2
    private String color; // 타일의 색상

    // 생성자
    public Tile(int number, String color) {
        this.number = number;
        this.color = color;
    }

    public Tile(int number) {
        this.number = number;
        color = "Bugi";
    }


    // 숫자를 반환하는 메서드
    public int getNumber() {
        return number;
    }

    // 색상을 반환하는 메서드
    public String getColor() {
        return color;
    }

    // 부기 타일 여부를 반환하는 메서드
    public boolean isBugi() {
        return (number == 1 && color.equals("Bugi"));
    }

    // 타일 정보를 문자열로 반환하는 메서드
//    @Override
//    public String toString() {
//        if(isBugi()) return "Model.Tile{ BUGI }";
//        else {
//            return "Model.Tile{" +
//                    "number=" + number +
//                    ", color='" + color + '\'' +
//                    "}";
//        }
//    }
}