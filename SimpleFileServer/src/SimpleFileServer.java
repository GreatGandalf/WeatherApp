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
    ServerSocket servsock = null;
    Socket sock = null;
    
    int threadNumber = 0;
    
    
    try {
      servsock = new ServerSocket(SOCKET_PORT);
      while (true) {
        System.out.println("Waiting...");
        try {
          sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          // send file
          Runnable runnable = new ConnHandler(sock,threadNumber); // or an anonymous class, or lambda...

          Thread thread = new Thread(runnable);
          thread.start();
          
          threadNumber++;
        }
        finally {
        	
        }
      }
    }
    finally {
      if (servsock != null) servsock.close();
    }
  }
}