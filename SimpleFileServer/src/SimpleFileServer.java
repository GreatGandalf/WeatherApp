import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class SimpleFileServer {

  public final static int SOCKET_PORT = 5056;  // you may change this

  public final static int FILE_SIZE = 5000; // file size temporary hard coded
                                          // should bigger than the file to be downloaded
  
  public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
	}
  
  public static ArrayList<String> CreateCountryLists(String country) {
	  ArrayList<String> list = new ArrayList<String>();
	  try {
		Scanner s = new Scanner(new File("C:\\Users\\Levi\\Documents\\GitHub\\WeatherApp\\SimpleFileServer\\src\\countrylists\\"+country+".txt"));
	  	while (s.hasNext()){
	      list.add(s.next());
	  	}
	  	s.close();
	  	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  	}
	  	return list;
  }
  
  public static ArrayList<String> USlist = CreateCountryLists("US");
  public static ArrayList<String> FRlist = CreateCountryLists("FR");
  public static ArrayList<String> UKlist = CreateCountryLists("UK");

  public static void main (String [] args ) throws IOException {
	  
	  System.out.println(USlist);
	  
	//CountryFilter filter1 = new CountryFilter();
	//filter1.testFile();
	
    ServerSocket servsock = null;
    Socket sock = null;
    
    int threadNumber = 0;
    
    
    try {
      servsock = new ServerSocket(SOCKET_PORT);
      while (true) {
        System.out.println("Waiting...");
        try {
          sock = servsock.accept();
          System.out.println("Accepted connection: " + sock);
          // send file
          Runnable runnable = new ConnHandler(sock,threadNumber); // or an anonymous class, or lambda...

          Thread thread = new Thread(runnable);
          thread.start();
          
          System.out.println("Started thread number: " + threadNumber);
          
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