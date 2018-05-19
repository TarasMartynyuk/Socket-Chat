package Chat;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import utils.SocketDataIOWrapper;

public class ChatServer {

    public static final String  OK = "OK";
    public static final String  NOT_OK = "NOT_OK";

    private static final int PORT = 6666;

    private ServerSocket _serverSocket;
    private HashMap<String, Socket> connectedClients = new HashMap<>();

    public static void main(String[] ar) throws IOException {

        new ChatServer().serve();
    }

    public ChatServer() throws IOException {
        this._serverSocket = new ServerSocket(PORT);
    }

    private void serve() {

        try {
            System.out.println("Waiting for a client...");

            while(true) {
                Socket socket = _serverSocket.accept();


                //TODO: release resources properly
                new Thread(() -> {

                    System.out.println("client with ip: " + socket.getInetAddress() +
                        " and port : " + socket.getPort() + " has connected, serving him in a sep thread");

                    try {
                        serveClient(socket);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        catch(Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * registers user,
     * then allows him to connect to other client,
     * and transfers the messages directed to server
     * from 2 clients to each other
     *
     * @param clientSocket
     */
    private void serveClient(Socket clientSocket) throws Exception {

        var thisClientWrapper = new SocketDataIOWrapper(clientSocket);

        var thisNickname =  registerClient(thisClientWrapper, clientSocket);

        boolean wantsToConnect = parseClientInteractionType(thisClientWrapper);

        if(wantsToConnect) {
            Socket otherClient;
            do {
                otherClient = parseClientsConnectionRequest(thisClientWrapper);

            } while (otherClient == null);

            //TODO: direct output from server to clients user
            var otherClientWrapper = new SocketDataIOWrapper(otherClient);
            otherClientWrapper.writeUtf("connected you to the " + thisNickname + ", per his request");

            transferMessagesTwoWay(thisClientWrapper, otherClientWrapper);
        }

        // the client that wants to wait does just that, until a more initiative client makes the first move
        // the 2-way channel will be open when the initiative client will connect to the awaiting one
    }

    //#region connection
    /**
     * waits until client sends a line,
     * then stores it as a nickname in the global hash table
     * @return created nickname
     */
    private String registerClient(SocketDataIOWrapper sdWrapper, Socket client) throws Exception {

        var nickname = sdWrapper.readUtf();

        if(connectedClients.containsKey(nickname)) {
            throw new Exception("nickname already taken");
        }

        connectedClients.put(nickname, client);
        System.out.println("registered client with a nickname: " + nickname);
        return nickname;
    }

    /**
     * waits for client to input string,
     * then interprets it as a type of interaction (wait or connect)
     *
     * returns true if client wants to connect,
     * and false if he wants to wait for a connection
     */
    private boolean parseClientInteractionType(SocketDataIOWrapper clientWrapper) throws IOException {
        var interactionType = clientWrapper.readUtf();

        if(interactionType.equals(ChatClient.CONNECT)) {
            return true;
        }

        if(interactionType.equals(ChatClient.WAIT)) {
            return false;
        }

        throw new IllegalArgumentException("the interaction type sent by client must be either a Chat.ChatClient.Connect or Chat.ChatClient.Wait constant");
    }

    //TODO: handle when user sends his own nickname
    /**
     * waits until client sends a line,
     * then interprets that line as a nickname,
     *
     * if there is a connected client with such nickname,
     * returns socket for him, and sends the OK to client
     *
     * else, returns null and sends the NOT_OK to client
     *
     */
    private Socket parseClientsConnectionRequest(SocketDataIOWrapper clientWrapper) throws IOException {

        var nickname = clientWrapper.getDin().readUTF();

//        assert ! nickname.isEmpty();
        System.out.println("recieved connection request : " + nickname);

        Socket otherClient = connectedClients.getOrDefault(nickname, null);

        if(otherClient != null) {
            clientWrapper.writeUtf(OK);

            System.out.println("success connecting with a nickname: " + nickname);
            return otherClient;
        }

        clientWrapper.writeUtf(NOT_OK);
        System.out.println("failure connecting with a nickname: " + nickname);

        return null;
    }
    //#endregion

    //#region chat
    /**
     * one thread for each way binding
     */
    private void transferMessagesTwoWay(
            SocketDataIOWrapper thisSocketWrapper, SocketDataIOWrapper otherSocketWrapper) throws IOException {

        transferMessagesOneWayInSepThread(thisSocketWrapper, otherSocketWrapper);
        transferMessagesOneWayInSepThread(otherSocketWrapper, thisSocketWrapper);
    }
    
    /**
     * in a infinite loop,
     * successively reads from the @param thisSocketWrapper
     * and writes to the @param otherSocketWrapper
     */
    private void transferMessagesOneWay(
            SocketDataIOWrapper thisSocketWrapper, SocketDataIOWrapper otherSocketWrapper) throws IOException {

        while (true) {
            var message = thisSocketWrapper.readUtf();
            otherSocketWrapper.writeUtf(message);
        }
    }

    private void transferMessagesOneWayInSepThread(
            SocketDataIOWrapper thisSocketWrapper, SocketDataIOWrapper otherSocketWrapper) throws IOException {

        new Thread(() -> {
            try {
                transferMessagesOneWay(thisSocketWrapper, otherSocketWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    //#endregion
}
