package utils;
import java.io.*;
import java.net.Socket;

public class SocketDataIOWrapper {

    private DataInputStream _din;
    private DataOutputStream _dout;

    public SocketDataIOWrapper(Socket socket) throws IOException {
        _din = new DataInputStream(socket.getInputStream());
        _dout = new DataOutputStream(socket.getOutputStream());
    }


    public void writeUtf(String text) throws IOException {
        getDout().writeUTF(text);
        getDout().flush();
    }

    public String readUtf() throws IOException {
        return getDin().readUTF();
    }

    public DataInputStream getDin() {
        return _din;
    }

    public DataOutputStream getDout() {
        return _dout;
    }
}