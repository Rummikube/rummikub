package Controller;

import Model.Player;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ClientHandler{
    private Socket clientSocket;
    private Object input = null;

    private ServerOutput out;
    private ServerInput in;
    private Server server;
    private int index;


    // 클라이언트에 객체 전달
    public void sendObject(SerializeObject obj){
        out.addObject(obj);
    }

    public void startThread(){
        Thread outThread = new Thread(out);
        Thread inThread = new Thread(in);

        outThread.start();
        inThread.start();
    }

    public ClientHandler(Socket socket, Server server, int index) {
        this.clientSocket = socket;
        this.server = server;
        this.index = index;
        try {
            out = new ServerOutput(clientSocket);
            in = new ServerInput(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
1
    class ServerOutput implements Runnable{
        private ObjectOutputStream objectOutputStream;
        private Socket serverSocket;

        private Queue<SerializeObject> outputQueue;

        public ServerOutput(Socket serverSocket) throws IOException {
            this.serverSocket = serverSocket;
            objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            outputQueue = new ConcurrentLinkedQueue<>();
        }

        public ObjectOutputStream getObjectOutputStream() {
            return objectOutputStream;
        }

        public void addObject(SerializeObject object){
            this.outputQueue.offer(object);
        }


        @Override
        public void run() {
            try {
                while (true) {
                    SerializeObject sendObj = outputQueue.poll();
                    if (sendObj != null) {
                        objectOutputStream.writeObject(sendObj);
                        if(sendObj.getObjectType() == "Player[]") {
                            Player[] cur = (Player[]) sendObj.getEventObject();
                            for(int i = 0 ; i < 4 ; i ++){
                                if(cur[i] != null) {
                                    System.out.println(cur[i].getName());
                                }
                            }
                            System.out.println(sendObj.getEventObject());
                        }
                        System.out.println("출력 추가됨 " + sendObj.getEventObject() + " index : " + index);
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class ServerInput implements Runnable{
        ObjectInputStream objectInputStream;
        Socket serverSocket;

        Object curInput = null;

        public ServerInput(Socket serverSocket) throws IOException {
            this.serverSocket = serverSocket;
            objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
        }

        public ObjectInputStream getObjectInputStream() {
            return objectInputStream;
        }

        @Override
        public void run() {
            while(true){
                try {
                    if((curInput = objectInputStream.readObject()) != null){
                        SerializeObject sInput = (SerializeObject) curInput;
                        server.getGameController().excuteQuery(sInput);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
