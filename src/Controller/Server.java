package Controller;

import Model.Game;
import Model.Player;

import java.io.*;
import java.net.*;
public class Server {


    static int clientCnt = 0;
    static int port;
    static Player[] players;
    static private GameController gameController;

    public Server(int port, GameController gameController) {
        this.port = port;
        this.gameController = gameController;
        players = gameController.getPlayers();
        openServer();
    }


    public void notifiObservers(ClientHandler clientHandler, SerializeObject object){
        for(int i = 0 ; i < GameController.MAX_PLAYER_COUNT ; i ++){
            if(players[i] == null || players[i].getClientHandler() == clientHandler) continue;
            ClientHandler curHandler = players[i].getClientHandler();
            curHandler.returnObj(object);
        }
    }

     private void addPlayer(Socket clientSocket){
        clientCnt++;
        int idx = clientCnt - 1;
        ClientHandler curHandler = new ClientHandler(clientSocket, this, idx);
        Thread clientThread = new Thread(curHandler);
        clientThread.start();
        curHandler.returnObj(new SerializeObject("connected", "String"));
        players[idx] = new Player("", curHandler);
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
                        while(true) {
                            System.out.println("기다리는 중");
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress().getHostAddress());

                            if (clientCnt < 4 || GameController.gameState == GameController.GameState.FINISHED) {
                                // 클라이언트와 통신하는 스레드 생성
                                addPlayer(clientSocket);
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
