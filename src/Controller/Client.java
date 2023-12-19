package Controller;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Exceptions.NotEmptySpaceException;
import Exceptions.OnProgressException;

public class Client{


    private Socket curSocket;
    private ClientOutput output;
    private ClientInput input;

    private GameController gameController;


    public void sendObject(SerializeObject object){
        output.addObject(object);
    }

    public Client(GameController gameController, String address, int port, String name) throws UnknownHostException, NotEmptySpaceException, OnProgressException{
        this.gameController = gameController;
        try {
            curSocket = new Socket(address, port);

            output = new ClientOutput(curSocket);
            input = new ClientInput(curSocket);

            SerializeObject answer = null;

            while (answer == null) {
                try {
                    answer = (SerializeObject) input.getObjectInputStream().readObject();
                } catch (IOException e) {
                    // IOException 처리
                    e.printStackTrace();
                }
            }
            System.out.println("받은 객체 : " + answer.getEventObject());
            String answerStr = (String)answer.getEventObject();
            if(answerStr.equals("방이 가득찼습니다.")) {
                throw new NotEmptySpaceException();
            }
            else if(answerStr.equals("현재 게임이 진행중입니다.")){
                throw new OnProgressException();
            }
            sendObject(new SerializeObject("received", "String", gameController.getIndex()));
            Thread outputThread = new Thread(output);
            Thread inputThread = new Thread(input);

            outputThread.start();
            inputThread.start();

            System.out.println("서버에 연결되었습니다.");
            sendObject(new SerializeObject(name, "Name", gameController.getIndex()));

        } catch (ClassNotFoundException e) {
            System.out.println("클래스가 맞지 않습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientOutput implements Runnable{
        private ObjectOutputStream objectOutputStream;
        private Socket clientSocket;

        private Queue<SerializeObject> outputQueue;

        public ClientOutput(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputQueue = new ConcurrentLinkedQueue<>();
        }

        public ObjectOutputStream getObjectOutputStream() {
            return objectOutputStream;
        }

        public void addObject(SerializeObject object){
            this.outputQueue.offer(object);
            System.out.println(object.getEventObject() + " 큐에 추가됨");
            System.out.println(outputQueue.toString());
        }


        @Override
        public void run() {
            try {
                while (true) {
                    SerializeObject sendObj = outputQueue.poll();
                    if (sendObj != null) {
                        System.out.println(sendObj.getEventObject());
                        objectOutputStream.writeObject(sendObj);
                        System.out.println(sendObj.getObjectType() + " " + sendObj.getEventObject() + " 전달");
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

    class ClientInput implements Runnable{
        ObjectInputStream objectInputStream;
        Socket clientSocker;

        Object curInput = null;

        public ClientInput(Socket clientSocker) throws IOException {
            this.clientSocker = clientSocker;
            objectInputStream = new ObjectInputStream(clientSocker.getInputStream());
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
                        gameController.excuteQuery(sInput);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}