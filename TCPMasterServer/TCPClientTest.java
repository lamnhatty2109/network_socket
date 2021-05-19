
import java.io.*;
import java.net.*;
import java.util.*;
  
// Client class
class TCPClientTest {
    
    // driver code
    public static void main(String[] args)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        Socket socket = null;
        // establish a connection by providing host and port
        // number
        try 
        // (Socket socket = new Socket("localhost", 7777)) 
        {
            socket = new Socket("localhost", 7777);
            // writing to server
            out = new PrintWriter(
                socket.getOutputStream(), true);
  
            // reading from server
            
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
  
            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;
  

            // call register file services
            out.println("3");

            int numOfFile = Integer.parseInt(in.readLine());

            for (int i = 0; i < numOfFile; i++) {
                System.out.println("filename: " + in.readLine());
                System.out.println("server ip: " + in.readLine());
                System.out.println("server port: " + in.readLine());
                System.out.println("---------------"+i);
            }

            line = null;
            

            while (!"exit".equalsIgnoreCase(line)) {
                
                // reading from user
                line = sc.nextLine();

  
                // sending the user input to server
                out.println(line);
                out.flush();
  
                // displaying server reply
                System.out.println("Server replied "
                                   + in.readLine());
            }
            
            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}