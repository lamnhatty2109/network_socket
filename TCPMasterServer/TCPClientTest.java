
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
            out.println("1");

            int numOfFile = 3;
            // num of files
            out.println(""+numOfFile);

            // file names
            for (int i = 0; i < numOfFile; i++) {
                out.println("myFileName"+i);
            }

            // call remove file services dowwn
            // out.println("2");



            // System.out.println("Server replied "
            // + in.readLine());
            // System.out.println("Server replied "
            // + in.readLine());
            // System.out.println("Server replied "
            // + in.readLine());
            // System.out.println("Server replied "
            // + in.readLine());
            // System.out.println("Server replied "
            // + in.readLine());
            // System.out.println("Server replied "
            // + in.readLine());

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
            out.println("2");

            e.printStackTrace();
        }
        finally{
            try {
                out.println("2");

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