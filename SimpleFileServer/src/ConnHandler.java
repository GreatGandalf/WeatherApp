import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConnHandler implements Runnable{
	
	public final int FILE_SIZE = 5000;
	
	FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    
    String threadName;
    int requestNumber = 60;
    
    public ConnHandler(Socket so, int number) {
    	this.sock = so;
    	this.threadName = String.valueOf(number);
    }
	
	
	public String getCurrentTimeStamp() {
	    return new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
	}

	@Override
	public void run() {
		while(true) {
			try {
				InputStream is = sock.getInputStream();
				//String newfile = "D:/weather/"+threadName+"_"+getCurrentTimeStamp()+".xml";
				byte [] mybytearray  = new byte [FILE_SIZE];
				bytesRead = is.read(mybytearray,0,mybytearray.length);
				if(requestNumber >= 60) {
					requestNumber = 1;
					
					String newfile = "D:/weather/"+threadName+"_"+getCurrentTimeStamp()+".xml";
					fos = new FileOutputStream(newfile);
					bos = new BufferedOutputStream(fos);
					//bytesRead = is.read(mybytearray,0,mybytearray.length);
					bos.write(mybytearray, 0 , bytesRead);
					
					mybytearray = null;
					bos.flush();
					fos.flush();
					fos.close();
					bos.close();
				} else {
					requestNumber++;
					//System.out.println("Request Number: " + requestNumber);
				}
				//System.out.println("File " + newfile + " downloaded (" + bytesRead + " bytes read)");
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
