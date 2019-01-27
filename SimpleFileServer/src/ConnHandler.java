import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
					
					String timestamp = getCurrentTimeStamp();
					
					System.out.println("Threadname:" + threadName);
					
					if(threadName == "0") {
						PrintWriter writer = new PrintWriter("C:/weather/US/"+timestamp+".txt", "UTF-8");
						writer.close();
						System.out.println("USFILE");
					}
					
					PrintWriter writer = new PrintWriter("C:/weather/US/"+timestamp+".xml");
					writer.close();
					
					String newfile = "C:/weather/"+threadName+"_"+timestamp+".xml";
					fos = new FileOutputStream(newfile);
					bos = new BufferedOutputStream(fos);
					//bytesRead = is.read(mybytearray,0,mybytearray.length);
					bos.write(mybytearray, 0 , bytesRead);
					
					mybytearray = null;
					bos.flush();
					fos.flush();
					fos.close();
					bos.close();
					
					filterData(newfile);
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
	
	private void filterData(String filename) {
		try {
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(inputFile);
        	doc.getDocumentElement().normalize();
        	
        	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        	NodeList nList = doc.getElementsByTagName("MEASUREMENT");
        	
        	for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                   Element eElement = (Element) nNode;
                   //System.out.println("Station ID: " + eElement.getElementsByTagName("STN").item(0).getTextContent());
                   //System.out.println("Date: " + eElement.getElementsByTagName("DATE").item(0).getTextContent());
                }
             }
		} catch (Exception e) {
	         e.printStackTrace();
	      }
	}

}
