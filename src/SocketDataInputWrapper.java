import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketDataInputWrapper {

    private DataInputStream din;
    private DataOutputStream dout;

    public SocketDataInputWrapper(Socket socket) throws IOException {
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
    }


    public void writeUtf(String text) throws IOException {
        getDout().writeUTF(text);
//        getDout().size();
        getDout().flush();
    }

    public String readUtf() throws IOException {
        return getDin().readUTF();
    }

    public DataInputStream getDin() {
        return din;
    }

    public DataOutputStream getDout() {
        return dout;
    }



}