import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class TCPMasterServer {
    public static int masterServerPort = 7777;
    public static String serverHost = "localhost";
    public static ArrayList<FileInfo> listFileInfo = new ArrayList<FileInfo>();

    public static void main(String[] args) {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(masterServerPort);
            server.setReuseAddress(true);

            System.out.println("Master Server is starting");

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * sleep program in millisecond
     * 
     * @param millisecond
     */
    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Service #1
    // Server -> MasterServer(this)
    // Receive request to register a file and its server-ip:port send from a Server
    public static void fileRegister(FileInfo fileInfo) {
        listFileInfo.add(fileInfo);
    }

    // Services #2
    // MasterServer(this) -> Client
    // Request by Client to Send to Client a list of every file and its
    // server-ip:port
    public static void fileFlush() {
        for (FileInfo fileInfo : listFileInfo) {

        }
    }

    public static void removeFileByServer(String host, int port) {
        FileInfo file = null;
        for (FileInfo fileInfo : listFileInfo) {
            if (fileInfo.getHost().equals(host) && fileInfo.getPort() == port)
                file = fileInfo;
        }

        listFileInfo.remove(file);
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                // get the outputstream of client
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // get the inputstream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // String line;
                // while ((line = in.readLine()) != null) {

                // // writing the received message from
                // // client
                // System.out.printf(
                // " Sent from the client: %s\n",
                // line);
                // out.println(line);
                // }

                int numOfFile = 0;

                String status;
                while ((status = in.readLine()) != null) {
                    // -1: null | 1: Server send File | 2: Server is down | 3: Client request file
                    // info
                    switch (status) {
                        case "1":
                            out.println("master server received services 1");
                            System.out.println("master server received services 1");
                            // receive number of size to initial loop
                            numOfFile = Integer.parseInt(in.readLine());
                            System.out.println("master server received files: " + numOfFile);
                            // cache every file to List
                            for (int i = 0; i < numOfFile; i++) {
                                FileInfo file = new FileInfo();
                                file.setFilename(in.readLine());
                                file.setInfo(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());

                                listFileInfo.add(file);
                            }
                            break;

                        case "2":
                            out.println("master server received server down");
                            System.out.println("master server received server down");
                            System.out.println("master server remove files: " + numOfFile);
                            // find file by name and current client ip
                            for (int i = 0; i < numOfFile; i++) {
                                removeFileByServer(clientSocket.getInetAddress().getHostAddress(),clientSocket.getPort());
                            }
                            break;

                        case "3":
                            out.println("master server received services 2");
                            System.out.println("master server received services 2");
                            // send the size of ArrayList for Client to Read
                            out.println(listFileInfo.size());
                            // then send file by file
                            for (int i = 0; i < listFileInfo.size(); i++) {
                                out.println(listFileInfo.get(i).getFilename());
                                out.println(listFileInfo.get(i).getHost());
                                out.println(listFileInfo.get(i).getPort());
                            }
                            break;

                        default:
                            out.println("master server received meanless command");
                            System.out.println("master server received meanless command");
                            break;
                    }

                    // for (FileInfo fileInfo : listFileInfo) {
                    //     System.out.println(fileInfo.getFilename());
                    //     System.out.println(fileInfo.getHost());
                    //     System.out.println(fileInfo.getPort());
                    // }
                    System.out.println(listFileInfo.size());

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
