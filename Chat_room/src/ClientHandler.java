import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: "+ clientUsername + "Has joined the chat!");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader,bufferedWriter);
                break;
            }
        }


    }

//    so this message in this case is used to broadcast the message that is added to clientHandler list
    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if(!clientHandler.clientUsername.equals(clientUsername)){

//                    this is to write to the screen
                    clientHandler.bufferedWriter.write(messageToSend);
//                    I'm donbe sending data you can close what I ahve written
                    clientHandler.bufferedWriter.newLine();
//                    manually flush because the buffer need to be full enough before it sends stuff through
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader,bufferedWriter);

            }

        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("Server" + clientUsername + "boy has left");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }

        } catch (IOException e){
            e.printStackTrace();
        }



    }

/*
    Quick note on socktes and streams

    **** With streams
        -YOu only need to close the outer wrapper as the underlying streams are closed when you close the wrapper
        -We only need to close out buffered reader and our buffered writer and not the output stream writer and input stream reader

    **** With sockets
        -Closing a socket will also close its input stream and output stream

*/
}
