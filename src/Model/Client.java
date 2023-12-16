package Model;

import Controller.GameController;

import java.io.*;
import java.net.*;

public class Client {
    public void connect(String address, int port) throws UnknownHostException{
        try (Socket socket = new Socket(address, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("서버에 연결되었습니다.");

            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("서버로 전송한 메시지: " + userInput);

                String response = in.readLine();
                System.out.println("서버로부터 받은 응답: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}