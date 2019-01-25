
// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
  
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 


// Server class 
public class server  
{ 
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 5056 
        ServerSocket ss = new ServerSocket(5056); 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                // obtaining input and out streams 
                InputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                Thread t = new ClientHandler(s, dis, dos); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{ 
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    final InputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
    
    int bytesRead;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
  
    // Constructor 
    public ClientHandler(Socket s, InputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    }
    
    public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
	}
  
    @Override
    public void run()  
    { 
        String received; 
        String toreturn; 
        int x = 0;
        		
        while (true)
        { 
            try { 

            	String newfile = "C:/weather/"+x+"_"+getCurrentTimeStamp()+".xml";
				byte [] mybytearray  = new byte [60000];
				fos = new FileOutputStream(newfile);
				bos = new BufferedOutputStream(fos);
				bytesRead = dis.read(mybytearray,0,mybytearray.length);
				bos.write(mybytearray, 0 , bytesRead);
				// JOOST DEEL
//                // Ask user what he wants 
//                dos.writeUTF("What do you want?[Date | Time]..\n"+ 
//                            "Type Exit to terminate connection."); 
//                  
//                // receive the answer from client 
//              received = dis.read();
//                System.out.println(received);
//                
//                //System.out.println(received);
//                BufferedWriter writer = new BufferedWriter(
//                		new FileWriter("C:\\Users\\joost\\Downloads\\UnwdmiGenerator22\\output\\output" + x + ".xml"));
//                writer.write(received);
//                writer.close();
//                x++;
       
                  received = "nee";
                if(received.equals("exit")) 
                {  
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // creating Date object 
                Date date = new Date(); 
                  
                // write on output stream based on the 
                // answer from the client 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
}