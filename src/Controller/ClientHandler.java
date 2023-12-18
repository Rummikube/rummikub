package Controller;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Object input = null;

    private Server server;
    private int index;
    private SerializeObject obj = null;


    // 클라이언트에 객체 전달
    public void setObject(SerializeObject obj){
        this.obj = obj;
    }

    public void update(SerializeObject object){
        // 현재 오브젝트 파싱 및 처리
        server.excute(object, index);
    }


    public ClientHandler(Socket socket, Server server, int index) {
        this.clientSocket = socket;
        this.server = server;
        this.index = index;
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(obj != null || (input = objectInputStream.readObject()) != null) {
                    // 입력
                    if (obj != null) {
                        System.out.println("출력 추가됨 " + obj.getEventObject() + " ");
                        objectOutputStream.writeObject(obj);
                        obj = null;
                    } else {
                        SerializeObject sinput = (SerializeObject) input;
                        System.out.println("입력 추가됨 " + sinput.getEventObject());
                        update((SerializeObject) input);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
