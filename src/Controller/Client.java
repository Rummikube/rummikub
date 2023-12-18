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


    public Client(GameController gameController){
        this.gameController = gameController;
    }

    // 서버에 객체 전달
    public synchronized void returnObj (SerializeObject obj){
        this.obj = obj;
    }

    public void connect(String address, int port, String name) throws UnknownHostException, NotEmptySpaceException, OnProgressException{
        try {
            System.out.println("연결을 시도합니다.");
            curSocket = new Socket(address, port);
            System.out.println(curSocket);
            this.objectOutputStream = new ObjectOutputStream(curSocket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(curSocket.getInputStream());

            SerializeObject answer = (SerializeObject) objectInputStream.readObject();
            String answerStr = (String)answer.getEventObject();
            returnObj(new SerializeObject("received", "String"));
            if(answerStr.equals("방이 가득찼습니다.")) {
                throw new NotEmptySpaceException();
            }
            else if(answerStr.equals("현재 게임이 진행중입니다.")){
                throw new OnProgressException();
            }

            System.out.println("서버에 연결되었습니다.");

            objectOutputStream.writeObject(new SerializeObject(name, "String"));
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스가 맞지 않습니다.");
        }
    }

    @Override
    public void run() {
        try {
            while (obj != null || (input = objectInputStream.readObject()) != null) {
                // 입력
                if(obj != null) {
                    objectOutputStream.writeObject(obj);
                    obj = null;
                }
                else {
                    gameController.excuteQuery((SerializeObject) input, -1);
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