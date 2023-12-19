package Controller;

import Model.Player;

import java.io.*;
import java.net.*;

public class Server {


    static int clientCnt = 1;
    static int port;
    static Player[] players;
    static ClientHandler[] handlers = new ClientHandler[GameController.MAX_PLAYER_COUNT];
    static private GameController gameController;


    public static GameController getGameController() {
        return gameController;
    }

    public Server(int port, GameController gameController) {
        this.port = port;
        this.gameController = gameController;
        players = gameController.getPlayers();
        openServer();
    }

    public void notifiObservers(SerializeObject object, int eIndex) {
        System.out.println("notify => " + object.getEventObject());
        for (int i = 1; i < GameController.MAX_PLAYER_COUNT; i++) {
            if (players[i] == null || i == eIndex) continue;
            handlers[i].sendObject(object);
        }
    }

    public void excute(SerializeObject object){
        gameController.excuteQuery(object);
    }

    private void addPlayer(Socket clientSocket) throws SocketException {
        clientCnt++;
        int idx = clientCnt - 1;
        ClientHandler curHandler = new ClientHandler(clientSocket, this, idx);
        handlers[idx] = curHandler;
        players[idx] = new Player("");
        curHandler.sendObject(new SerializeObject("connected", "String", gameController.getIndex()));
        curHandler.sendObject(new SerializeObject(idx, "Index", gameController.getIndex()));
        curHandler.startThread();
        System.out.println("플레이어 추가됨");
        gameController.updatePlayers();
    }

    private void openServer() {

        Thread serverWaitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("server 소켓 생성, IP : " + serverSocket.getLocalSocketAddress());
                    System.out.println(InetAddress.getLocalHost().getHostAddress());

                    InetAddress serverLocalIpAddress = serverSocket.getInetAddress();
                    System.out.println("Serversocket Local IP Address : " + serverLocalIpAddress.getHostAddress());
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress().getHostAddress());

                        if (clientCnt < 4 && GameController.gameState == GameController.GameState.FINISHED) {
                            // 클라이언트와 통신하는 스레드 생성
                            addPlayer(clientSocket);
                        } else {
                            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                            if (clientCnt >= 4) {
                                out.writeObject(new SerializeObject("방이 가득찼습니다.", "String", gameController.getIndex()));
                            } else {
                                out.writeObject(new SerializeObject("현재 게임이 진행중입니다.", "String", gameController.getIndex()));
                            }
                            clientSocket.close();
                            out.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverWaitThread.start();
    }


}
