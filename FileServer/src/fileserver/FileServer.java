package fileserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.io.*;

public class FileServer {
    private ServerSocket serverSocket;
    private int port = 5560;
    private String host = "localhost";
    private Socket socket;
    private String listFilesName = "";
    
    // Master server info:
    private int MSPort = 7777;
    private String MSHost = "localhost";
    
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
        dataOutputStream.writeUTF(listFilesName);
        dataOutputStream.writeUTF(host+":"+String.valueOf(port));
        dataOutputStream.flush();
//        dataOutputStream.flush();
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
    
    public static void main(String[] args) throws IOException {
        FileServer fileServer = new FileServer();
        fileServer.connectServer();
        fileServer.sendInfomation();
    }
    
    public void open() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server is open on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        while (true) {
            Socket server = null;
            DataInputStream inFromClient = null;
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
             
            try {
                // accept connect from client and create Socket object
                server = serverSocket.accept();
                System.out.println("connected to "
                        + server.getRemoteSocketAddress());
  
                // get greeting from client
                inFromClient = new DataInputStream(server.getInputStream());
                System.out.println(inFromClient.readUTF());
  
                // receive file info
                ois = new ObjectInputStream(server.getInputStream());
                FileInfo fileInfo = (FileInfo) ois.readObject();
                if (fileInfo != null) {
                    createFile(fileInfo);
                }
  
                // confirm that file is received
                oos = new ObjectOutputStream(server.getOutputStream());
                fileInfo.setStatus("success");
                fileInfo.setDataBytes(null);
                oos.writeObject(fileInfo);
                  
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                // close all stream
                closeStream(ois);
                closeStream(oos);
                closeStream(inFromClient);
                // close session
                closeSocket(server);
            }
        }
    }
    
    private boolean createFile(FileInfo fileInfo) {
        BufferedOutputStream bos = null;
         
        try {
            if (fileInfo != null) {
                File fileReceive = new File(fileInfo.getDestinationDirectory() 
                        + fileInfo.getFilename());
                bos = new BufferedOutputStream(
                        new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(bos);
        }
        return true;
    }
    
    public void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void closeStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
    