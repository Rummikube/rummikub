package Controller;

import Model.Game;
import Model.Player;

import java.io.*;
import java.net.*;
public class Server {


    static int clientCnt = 0;
    static int port;
    static ClientHandler[] handlers = new ClientHandler[GameController.MAX_PLAYER_COUNT];

    static private GameController gameController;

    public Server(int port, GameController gameController) {
        this.port = port;
        this.gameController = gameController;
        openServer();
    }

    static void openServer() {

            Thread serverWaitThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerSocket serverSocket;
                    try {
                        serverSocket = new ServerSocket(port);
                        System.out.println("server 소켓 생성");
                        while(true) {
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress().getHostAddress());

                            if (clientCnt < 4 || GameController.gameState == GameController.GameState.FINISHED) {
                                // 클라이언트와 통신하는 스레드 생성
                                ClientHandler curHandler = new ClientHandler(clientSocket);
                                clientCnt++;
                                handlers[clientCnt - 1] = curHandler;
                                Thread clientThread = new Thread(curHandler);
                                clientThread.start();
                                curHandler.returnObj(new SerializeObject("connected", "String"));
                                String name = (String)curHandler.getAnswer().getEventObject(); // 플레이어 객체 받기
                                gameController.getPlayers()[clientCnt - 1] = new Player(name);
                            } else {
                                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                if(clientCnt >= 4) {
                                    out.println("방이 가득찼습니다.");
                                }
                                else{
                                    out.println("현재 게임이 진행중입니다.");
                                }
                                clientSocket.close();
                                out.close();
                            }
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            serverWaitThread.start();
        }
    }

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private SerializeObject result = null;
    private boolean flag = true;

    private SerializeObject obj = null;


    // 클라이언트에 객체 전달
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


    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
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
            while (obj != null) {
                objectOutputStream.writeObject(obj);
                result = (SerializeObject) objectInputStream.readObject();
                obj = null;
                setFlag();
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
