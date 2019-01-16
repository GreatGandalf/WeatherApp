import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleFileServer {

  public final static int SOCKET_PORT = 13267;  // you may change this
  
  public static final String
  FILE_TO_RECEIVED = "D:/weather/weather.xml";  // you may change this, I give a
                                                       // different name because i don't want to
                                                       // overwrite the one used by server...

  public final static int FILE_SIZE = 6022386; // file size temporary hard coded
                                          // should bigger than the file to be downloaded
  
  public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
	}

  public static void main (String [] args ) throws IOException {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    
    
    try {
      servsock = new ServerSocket(SOCKET_PORT);
      while (true) {
        System.out.println("Waiting...");
        try {
          sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          // send file
          String newfile = "D:/weather/"+getCurrentTimeStamp()+".xml";
          byte [] mybytearray  = new byte [FILE_SIZE];
          InputStream is = sock.getInputStream();
          fos = new FileOutputStream(newfile);
          bos = new BufferedOutputStream(fos);
          bytesRead = is.read(mybytearray,0,mybytearray.length);
          current = bytesRead;
          
          do {
              bytesRead =
                 is.read(mybytearray, 0, mybytearray.length);
              bos.write(mybytearray, 0 , bytesRead);
              System.out.println("File " + newfile
                  + " downloaded (" + bytesRead + " bytes read)");
              
              bos.flush();
              fos.flush();
              fos.close();
              bos.close();
              
              newfile = "D:/weather/"+getCurrentTimeStamp()+".xml";
              
              fos = new FileOutputStream(newfile);
              bos = new BufferedOutputStream(fos);
              //bos.write(mybytearray, 0 , current); //Extra line added to continuously write to the xml file instead of only at the end of the file
              //if(bytesRead >= 0) current += bytesRead;
           } while(bytesRead > -1);
        }
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (fos != null) fos.close();
          if (bos != null) bos.close();
          if (sock != null) sock.close();
        }
      }
    }
    finally {
      if (servsock != null) servsock.close();
    }
  }
}