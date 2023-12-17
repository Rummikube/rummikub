package Controller;
import java.io.*;
import java.net.*;

import Exceptions.NotEmptySpaceException;
import Exceptions.OnProgressException;

public class Client implements Runnable{


    private Socket curSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private SerializeObject obj = null;

    private SerializeObject result = null;
    private boolean flag = true;

    // 서버에 객체 전달
    public synchronized void returnObj (SerializeObject obj){
        this.obj = obj;
        flag = false;
        while(!flag){
        }
    }

    public SerializeObject getAnswer(){
        return result;
    }



    public synchronized void setFlag() {
        flag = true;
        notify(); // 메인 스레드에 flag 변경 알림
    }

    public void connect(String address, int port, String name) throws UnknownHostException, NotEmptySpaceException, OnProgressException{
        try {
            System.out.println("연결을 시도합니다.");
            curSocket = new Socket(address, port);
            System.out.println(curSocket);
            this.out = new ObjectOutputStream(curSocket.getOutputStream());
            this.in = new ObjectInputStream(curSocket.getInputStream());

            SerializeObject answer = (SerializeObject)in.readObject();
            String answerStr = (String)answer.getEventObject();
            returnObj(new SerializeObject("received", "String"));
            if(answerStr.equals("방이 가득찼습니다.")) {
                throw new NotEmptySpaceException();
            }
            else if(answerStr.equals("현재 게임이 진행중입니다.")){
                throw new OnProgressException();
            }

            System.out.println("서버에 연결되었습니다.");

            out.writeObject(new SerializeObject(name, "String"));
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("클래스가 맞지 않습니다.");
        }
    }

    @Override
    public void run() {
        try {
            while (obj != null) {
                out.writeObject(obj);
                result = (SerializeObject) in.readObject();
                obj = null;
                setFlag();
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