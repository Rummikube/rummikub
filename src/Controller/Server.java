package Controller;

import java.io.*;
import java.net.*;
public class Server {


    static int clientCnt = 0;
    static int port;
    static ClientHandler[] handlers = new ClientHandler[GameController.MAX_PLAYER_COUNT];
    public Server(int port) {
        this.port = port;
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

                            if (clientCnt < 4) {
                                // 클라이언트와 통신하는 스레드 생성
                                ClientHandler curHandler = new ClientHandler(clientSocket);
                                clientCnt++;
                                handlers[clientCnt - 1] = curHandler;
                                Thread clientThread = new Thread(curHandler);
                                clientThread.start();
                            } else {
                                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                out.println("인원 초과");
                                clientSocket.close();
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

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("클라이언트로부터 받은 메시지: " + message);
                out.println("서버에서 응답: " + message);
            }
        } catch (IOException e) {
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
