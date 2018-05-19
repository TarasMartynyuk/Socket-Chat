import java.net.*;
import java.io.*;

public class Client {

    static final int serverPort = 6666;
    static final String serverAddress = "127.0.0.1";

    public static void main(String[] ar) {
        new Client().beAClient();
    }

    private void beAClient() {


        try {
            Socket serverSocket = new Socket(InetAddress.getByName(serverAddress), serverPort);

            SocketDataInputWrapper serverWrapper = new SocketDataInputWrapper(serverSocket);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));


            registerOnServer(keyboard, serverWrapper);

            connectWithAnotherClient(keyboard, serverWrapper);

            System.out.println( "WORKING");

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


    //#endregion

    //#region connection setup
    /**
     * prompts the user for a nickname, and sends a request to server to connect with that nickname
     * if the responce is negative, repropmts user
     */
    private static void connectWithAnotherClient(BufferedReader in,
                                                 SocketDataInputWrapper serverWrapper) throws IOException {

        while(true) {
            requestToConnectWithClient(in, serverWrapper);

            if(serverWrapper.readUtf().equals(Server.OK)) {
                return;
            }

            System.out.println("cannot find a user with that nickname, try again");
        }
    }

    private static void requestToConnectWithClient(BufferedReader in,
                                            SocketDataInputWrapper serverWrapper) throws IOException {
        String nickname =  propmtForString(in, "What's the nickname of a person you want to talk to?");

        serverWrapper.writeUtf(nickname);
        System.out.println("sent nickname");
    }

    private static void registerOnServer(BufferedReader in,
                                         SocketDataInputWrapper serverWrapper) throws IOException {
        String nickname =  propmtForString(in, "What will be your nickname?");
        serverWrapper.writeUtf(nickname);
    }

    //#endregion

    private static String propmtForString(BufferedReader in, String prompt) throws  IOException {
        System.out.println(prompt);

        String reply = in.readLine();
        assert ! reply.isEmpty();

        return reply;
    }
}