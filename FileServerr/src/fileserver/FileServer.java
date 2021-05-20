package fileserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.util.*;

public class FileServer  {
    private int port = 6677;
    private String host = "localhost";
    private Socket socket;
    private String listFilesName = "";
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket UDPServerSocket;
    // Master server info:
    private int MSPort = 7777;
    private String MSHost = "localhost";
   
    public class Responder implements Runnable {
        Socket socket = null;
        DatagramPacket packet = null;

        public Responder(Socket socket, DatagramPacket packet) {
            this.socket = socket;
            this.packet = packet;
        }

        public void run() {
//            byte[] data = makeResponse(); // code not shown
//            DatagramPacket response = new DatagramPacket(data, data.length,
//                packet.getAddress(), packet.getPort());
//            socket.send(response);
        }
    }
    
    public static void main(String[] args) throws IOException {
        FileServer fileServer = new FileServer();
//        fileServer.connectServer();
//        fileServer.sendInfomation();
        fileServer.openServer();
    }
    
    private void openServer() {
        try {
            UDPServerSocket = new DatagramSocket(port);
            System.out.println("Server is opened on port " + port);
            listening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    private void listening() {
        while (true) {
            receiveFile();
        }
    }
    
    public void receiveFile() {
        byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
        DatagramPacket receivePacket;
        
        try {
            // get file info
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            UDPServerSocket.receive(receivePacket);
            InetAddress inetAddress = receivePacket.getAddress();
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);System.out.print("Read file");
            FileInfo fileInfo = (FileInfo) ois.readObject();
            // show file info
            if (fileInfo != null) {
                System.out.println("File name: " + fileInfo.getFilename());
                System.out.println("File size: " + fileInfo.getFileSize());
                System.out.println("Pieces of file: " + fileInfo.getPiecesOfFile());
                System.out.println("Last bytes length: " + fileInfo.getLastByteLength());
            }
            // get file content
            System.out.println("Receiving file...");
            File fileReceive = new File(fileInfo.getDestinationDirectory() 
                    + fileInfo.getFilename());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(fileReceive));
            // write pieces of file
            for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length, 
                        inetAddress, port);
                UDPServerSocket.receive(receivePacket);
                bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
            }
            // write last bytes of file
            receivePacket = new DatagramPacket(receiveData, receiveData.length, 
                    inetAddress, port);
            UDPServerSocket.receive(receivePacket);
            bos.write(receiveData, 0, fileInfo.getLastByteLength());
            bos.flush();
            System.out.println("Done!");

            // close stream
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    public void connectServer() {
        try {
            System.out.println("Conencting to Master_Server...");
            socket = new Socket(MSHost, MSPort);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        System.out.println("Connected");
    }
    
    public void sendInfomation() throws IOException {
        listFilesForFolder();
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        System.out.println("Sending list of filename, IP, Port to the MasterServer");
        dataOutputStream.writeUTF("0");
        dataOutputStream.writeUTF(listFilesName);
        dataOutputStream.writeUTF(host+":"+String.valueOf(port));
        dataOutputStream.flush();
        dataOutputStream.close();
        System.out.println("Terminating!");
        socket.close();
    }
    
    public void listFilesForFolder() {
        File folder = new File("src\\fileserver\\data");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                listFilesName = listFilesName + file.getName() + "\n";
            }
        }
    }
}
    