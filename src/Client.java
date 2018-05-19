import java.net.*;
import java.io.*;

public class Client {

    static final int serverPort = 6666;
    static final String serverAddress = "127.0.0.1";

    Socket _serverSocket;
    SocketDataInputWrapper _serverWrapper;
    BufferedReader _consoleIn;

    public Client() throws IOException {
        _serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort);
        _serverWrapper = new SocketDataInputWrapper(_serverSocket);

        _consoleIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] ar) throws IOException {
        new Client().beAClient();
    }

    private void beAClient() {


        try {

            registerOnServer();

            connectWithAnotherClient();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    //#region chatting
    /**
     * infinetely prompts this client's user to enter messages,
     * then sends them via socket to server which redirects them to connected client
     *
     *
     */
    private static void PrintAndSendThisClientsMessages(DataOutputStream out)
    {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

//        try {
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private static void sendInputToServer(BufferedReader in, DataOutputStream out) {

    }


    //#endregion

    //#region connection setup
    /**
     * prompts the user for a nickname, and sends a request to server to connect with that nickname
     * if the responce is negative, repropmts user
     */
    private void connectWithAnotherClient() throws IOException {

        while(true) {
            requestToConnectWithOtherClient();

            if(_serverWrapper.readUtf().equals(Server.OK)) {
                return;
            }

            System.out.println("cannot find a user with that nickname, try again");
        }
    }

    private void requestToConnectWithOtherClient() throws IOException {
        String nickname =  propmtForString(_consoleIn, "What's the nickname of a person you want to talk to?");

        _serverWrapper.writeUtf(nickname);
    }

    private void registerOnServer() throws IOException {
        String nickname =  propmtForString(_consoleIn, "What will be your nickname?");
        _serverWrapper.writeUtf(nickname);
    }

    //#endregion

    private static String propmtForString(BufferedReader in, String prompt) throws  IOException {
        System.out.println(prompt);

        String reply = in.readLine();
        assert ! reply.isEmpty();

        return reply;
    }
}