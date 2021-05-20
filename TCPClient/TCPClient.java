import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
 
import javax.swing.JTextArea;
public class TCPClient {
    //private Socket client;
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private int serverPort = 6677;
    private String serverHost = "localhost";

    public static void main(String[] args) {
        String sourcePath = "";
        String destinationDir = "C:\\Users\\DELL\\Desktop\\New folder\\network_socket\\UDPServer\\UDPServer\\server\\";
        TCPClient tcpClient = new TCPClient();
        tcpClient.connectServer();
        tcpClient.sendFile(sourcePath, destinationDir);
    }
    /*public TCPClient(String host, int port, JTextArea textAreaLog) {
        this.host = host;
        this.port = port;
        this.textAreaLog = textAreaLog;
    }*/
    // Master server info:
    private int MSPort = 7777;
    private String MSHost = "localhost";

    public void connectServer() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        /*try {
            System.out.println("Conencting to Master_Server...");
            client = new Socket(MSHost, MSPort);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }*/
        
        System.out.println("Connected");
    }
    
    
 
    private void sendFile(String sourcePath, String destinationDir) {
        InetAddress inetAddress;
        DatagramPacket sendPacket;
        Scanner sc = new Scanner(System.in);
        boolean check = false;
        String temp = "";

        try {
            
            File fileshow = new File("C:\\Users\\DELL\\Desktop\\New folder\\network_socket\\UDPClient\\client\\");
            String[] paths;
            paths = fileshow.list();
            for(String path:paths) {
         
                // prints filename and directory name
                System.out.println(path);
            }
            while(!(check)){
                System.out.println("Nhập file muốn truyền đi:");
                temp = sc.nextLine();
                for(String path:paths) {
                    if(temp.equals(path) ){
                        check = true;
                    }
                }

                if (!(check)) {
                    System.out.println("File " + temp + " không có trong thư mục!!");
                    for(String path:paths) {
         
                        // prints filename and directory name
                        System.out.println(path);
                    }
                }
            }

            sourcePath = "C:\\Users\\DELL\\Desktop\\New folder\\network_socket\\UDPClient\\client\\" + temp;

            
            File fileSend = new File(sourcePath);
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            inetAddress = InetAddress.getByName(serverHost);
            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
            
            // get file size
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);

            // check last bytes of file
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // split file into pieces and assign to fileBytess
            byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
            int count = 0;
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytess[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }

            // read file info
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(fileSend.getName());
            fileInfo.setFileSize(fileSend.length());
            fileInfo.setPiecesOfFile(piecesOfFile);
            fileInfo.setLastByteLength(lastByteLength);
            fileInfo.setDestinationDirectory(destinationDir);

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
                    inetAddress, serverPort);
            clientSocket.send(sendPacket);

            // send file content
            System.out.println("Sending file...");
            // send pieces of file
            for (int i = 0; i < (count - 1); i++) {
                sendPacket = new DatagramPacket(fileBytess[i], PIECES_OF_FILE_SIZE,
                        inetAddress, serverPort);
                clientSocket.send(sendPacket);
                waitMillisecond(40);
            }
            // send last bytes of file
            sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,
                    inetAddress, serverPort);
            clientSocket.send(sendPacket);
            waitMillisecond(40);

            // close stream
            bis.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent.");
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
}
