package Model;

import Controller.GameController;

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

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 " + port + " 포트에서 대기 중...");

            while (clientCnt < GameController.MAX_PLAYER_COUNT) {
                Socket clientSocket = serverSocket.accept();
                clientCnt ++;
                System.out.println("클라이언트 연결됨: " + clientSocket.getInetAddress().getHostAddress());

                // 클라이언트와 통신하는 스레드 생성
                ClientHandler curHandler = new ClientHandler(clientSocket);
                handlers[clientCnt - 1] = curHandler;
                Thread clientThread = new Thread(curHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
