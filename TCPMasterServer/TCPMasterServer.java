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

public class TCPMasterServer {
    private int masterServerPort = 6688;
    private String serverHost = "localhost";

    public static void main(String[] args) {
        String sourcePath = "D:\\client\\test.zip";
        String destinationDir = "D:\\server\\";
        UDPClient udpClient = new UDPClient();
        udpClient.connectServer();
        udpClient.sendFile(sourcePath, destinationDir);
    }

    /**
     * connect server
     * 
     * @author viettuts.vn
     */
    private void connectServer() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
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
    public void fileRegister() {

    }

    // Services #2
    // MasterServer(this) -> Client
    // Request by Client to Send to Client a list of every file and its server-ip:port
    public fileFlush(){

    }
}
