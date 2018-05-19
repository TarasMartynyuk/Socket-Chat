package Calc;

import utils.SocketObjectIOWrapper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class CalcClient {

    static final int serverPort = 6666;
    static final String serverAddress = "127.0.0.1";

    Socket _serverSocket;
    SocketObjectIOWrapper _serverWrapper;
    BufferedReader _consoleIn;

    public static void main(String[] ar) {

        try {
            new CalcClient().beACalcClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CalcClient() throws IOException {
        _serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort);
        _serverWrapper = new SocketObjectIOWrapper(_serverSocket);

        _consoleIn = new BufferedReader(new InputStreamReader(System.in));
    }

    private void beACalcClient() throws IOException, InterruptedException {

        System.out.println("look, we will now do something on another program, using the internet!");
        System.out.println("Maybe even on another machine!");

        _serverWrapper.writeObj(new CalcImpl());

        System.out.println("doing extensive calculations...");

        int fourtyTwo = new DataInputStream(_serverSocket.getInputStream()).readInt();

        System.out.println("Tada!");
        System.out.println("result is " + fourtyTwo);
    }
}
