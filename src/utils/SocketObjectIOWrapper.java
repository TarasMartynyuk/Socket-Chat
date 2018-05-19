package utils;

import java.io.*;
import java.net.Socket;

public class SocketObjectIOWrapper {

    private ObjectInputStream _oin;
    private ObjectOutputStream _oOut;

    public SocketObjectIOWrapper(Socket socket) throws IOException {
        _oOut = new ObjectOutputStream(socket.getOutputStream());
        _oin = new ObjectInputStream(socket.getInputStream());
    }


    public void writeObj(Object obj) throws IOException {
        getOout().writeObject(obj);
        getOout().flush();
    }

    public <T> T readObj() throws IOException, ClassNotFoundException {
        return (T) getOin().readObject();
    }

    public ObjectInputStream getOin() {
        return _oin;
    }

    public ObjectOutputStream getOout() {
        return _oOut;
    }
}
