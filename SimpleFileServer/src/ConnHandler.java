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
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    int requestNumber = -100;
    
    SFTP thing;
    
    private ArrayList<String> USlist = SimpleFileServer.USlist;
    private ArrayList<String> FRlist = SimpleFileServer.FRlist;
    private ArrayList<String> UKlist = SimpleFileServer.UKlist;
    private ArrayList<String> GElist = SimpleFileServer.GElist;
    private ArrayList<String> BAlist = SimpleFileServer.BAlist;
    private ArrayList<String> BUlist = SimpleFileServer.BUlist;
    private ArrayList<String> CHlist = SimpleFileServer.CHlist;
    private ArrayList<String> INlist = SimpleFileServer.INlist;
    private ArrayList<String> SPlist = SimpleFileServer.SPlist;
    
    public ConnHandler(Socket so, int number) {
    	this.sock = so;
    	this.threadName = String.valueOf(number);
    	try {
    	    this.thing = new SFTP();
    		} catch (Exception e) {
    	       e.printStackTrace();
    	    }
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
					
					filterData(newfile,timestamp);
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
	
	private void filterData(String filename,String timestamp) {
		try {
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(inputFile);
        	doc.getDocumentElement().normalize();
        	
        	NodeList nList = doc.getElementsByTagName("MEASUREMENT");
        	
        	boolean write = false;
        	
        	for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                   Element eElement = (Element) nNode;
                   String station = eElement.getElementsByTagName("STN").item(0).getTextContent();
                   if(FRlist.contains(station)) {
                	   writeFile("FR",eElement,timestamp);
                   } else if (USlist.contains(station)) {
                	   writeFile("US",eElement,timestamp);
                   } else if (UKlist.contains(station)) {
                	   writeFile("UK",eElement,timestamp);
                   } else if (GElist.contains(station)) {
                	   writeFile("GE",eElement,timestamp);
                   } else if (BAlist.contains(station)) {
                	   writeFile("BA",eElement,timestamp);
                   } else if (BUlist.contains(station)) {
                	   writeFile("BU",eElement,timestamp);
                   } else if (CHlist.contains(station)) {
                	   writeFile("CH",eElement,timestamp);
                   } else if (INlist.contains(station)) {
                	   writeFile("IN",eElement,timestamp);
                   } else if (SPlist.contains(station)) {
                	   writeFile("SP",eElement,timestamp);
                   }
                   
                }
             }
		} catch (Exception e) {
	         e.printStackTrace();
	      }
		
		
		try {
			Files.delete(Paths.get(filename));
		} catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	private void writeFile(String country,Element eElement,String timestamp) {
		String station = eElement.getElementsByTagName("STN").item(0).getTextContent();
		try {
			String filename = "C:/weather/"+country+"/"+station+"_"+timestamp+".xml";
	   PrintWriter writer = new PrintWriter(filename);
	   writer.println("<DATA>");
	   writer.println("<STN>"+station+"</STN>");
  	   writer.println("<DATE>"+eElement.getElementsByTagName("DATE").item(0).getTextContent()+"</DATE>");
  	   writer.println("<TIME>"+eElement.getElementsByTagName("TIME").item(0).getTextContent()+"</TIME>");
  	   writer.println("<TEMP>"+eElement.getElementsByTagName("TEMP").item(0).getTextContent()+"</TEMP>");
//  	   writer.println("<DEWP>"+eElement.getElementsByTagName("DEWP").item(0).getTextContent()+"</DEWP>");
//  	   writer.println("<STP>"+eElement.getElementsByTagName("STP").item(0).getTextContent()+"</STP>");
//  	   writer.println("<SLP>"+eElement.getElementsByTagName("SLP").item(0).getTextContent()+"</SLP>");
//  	   writer.println("<VISIB>"+eElement.getElementsByTagName("VISIB").item(0).getTextContent()+"</VISIB>");
//  	   writer.println("<WDSP>"+eElement.getElementsByTagName("WDSP").item(0).getTextContent()+"</WDSP>");
  	   writer.println("<PRCP>"+eElement.getElementsByTagName("PRCP").item(0).getTextContent()+"</PRCP>");
//  	   writer.println("<SNDP>"+eElement.getElementsByTagName("SNDP").item(0).getTextContent()+"</SNDP>");
//  	   writer.println("<FRSHTT>"+eElement.getElementsByTagName("FRSHTT").item(0).getTextContent()+"</FRSHTT>");
//  	   writer.println("<CLDC>"+eElement.getElementsByTagName("CLDC").item(0).getTextContent()+"</CLDC>");
//  	   writer.println("<WNDDIR>"+eElement.getElementsByTagName("WNDDIR").item(0).getTextContent()+"</WNDDIR>");
  	   writer.println("</DATA>");
  	   writer.close();
  	   
  	   String static_file = "C:/weather/"+country+"/"+station+".xml";
  	   
  	  FileChannel src = new FileInputStream(filename).getChannel();
  	  FileChannel dest = new FileOutputStream(static_file).getChannel();
  	  dest.transferFrom(src, 0, src.size());
  	  
  	  src.close();
  	  dest.close();
  	   
  	   GiveFile(filename, country);
  	 GiveFile(static_file, country);
		} catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	private void GiveFile(String filename, String country) {
		thing.SendFile(filename, country);
	}

}
