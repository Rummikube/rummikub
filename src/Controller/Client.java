package Controller;
import java.io.*;
import java.net.*;

import Exceptions.NotEmptySpaceException;
import Exceptions.OnProgressException;

public class Client implements Runnable{


    private Socket curSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private SerializeObject obj = null;

    private Object input = null;
    private GameController gameController;

    public void setObject(SerializeObject object){
        obj = object;
    }

    public Client(GameController gameController, String address, int port, String name) throws UnknownHostException, NotEmptySpaceException, OnProgressException{
        this.gameController = gameController;
        try {
            curSocket = new Socket(address, port);

            this.objectOutputStream = new ObjectOutputStream(curSocket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(curSocket.getInputStream());

            SerializeObject answer = (SerializeObject) objectInputStream.readObject();
            System.out.println("받은 객체 : " + answer + " " + answer.getObjectType());
            String answerStr = (String)answer.getEventObject();
            if(answerStr.equals("방이 가득찼습니다.")) {
                throw new NotEmptySpaceException();
            }
            else if(answerStr.equals("현재 게임이 진행중입니다.")){
                throw new OnProgressException();
            }
            setObject(new SerializeObject("received", "String"));
            Thread clientThread = new Thread(this);
            clientThread.start();
            System.out.println("서버에 연결되었습니다.");
            setObject(new SerializeObject(name, "Name"));

        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스가 맞지 않습니다.");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("클라이언트 소켓 통신 중...");
            while (true) {
                if(obj != null || (input = objectInputStream.readObject()) != null) {
                    // 입력
                    if (obj != null) {
                        objectOutputStream.writeObject(obj);
                        System.out.println(obj.getObjectType() + " " + obj.getEventObject() + " 전달");
                        obj = null;
                    } else {
                        gameController.excuteQuery((SerializeObject) input, -1);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                curSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}