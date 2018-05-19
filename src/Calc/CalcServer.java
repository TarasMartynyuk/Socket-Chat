package Calc;

import utils.SocketDataIOWrapper;
import utils.SocketObjectIOWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcServer {

    private static final int PORT = 6666;

    private ServerSocket _serverSocket;

    public static void main(String[] ar) throws IOException, ClassNotFoundException, InterruptedException {

        new CalcServer().serve();
    }

    public CalcServer() {

        try {
            this._serverSocket = new ServerSocket(PORT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serve() throws IOException, ClassNotFoundException, InterruptedException {

        System.out.println("Waiting for a client...");

        var clientSocket = _serverSocket.accept();

        System.out.println("client with ip: " + clientSocket.getInetAddress() +
                " and port : " + clientSocket.getPort() + " has connected");


        var clientWrapper = new SocketObjectIOWrapper(clientSocket);

        var calculable = clientWrapper.<Calculable<Integer>>readObj();
        int fourtyTwo = calculable.calc();

        Thread.sleep(2000);

        new SocketDataIOWrapper(clientSocket).getDout().writeInt(fourtyTwo);
    }
}
