import javax.imageio.IIOException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
//This class containsa server socket object
//Responsible for listening for incoming connections or clients and creating a socket object to communicate with them
        private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }


//    Resposible for keeping our server running
    public void startServer() {

        try{
//          We want our server to run indefinetly
            while (!serverSocket.isClosed()) {


                Socket socket = serverSocket.accept();
                System.out.println("A client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }


        } catch (IIOException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IIOException e) {
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
