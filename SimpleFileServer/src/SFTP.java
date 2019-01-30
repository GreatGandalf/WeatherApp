import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTP {

	ChannelSftp channel;

	public SFTP() throws JSchException, SftpException, FileNotFoundException, InterruptedException{

		//create JSch
		JSch jsch = new JSch();

		//Create empty session variable
		Session session = null;

		//Set session variables with uname,host and port
		session = jsch.getSession("ITV2D02","13.95.111.217",22);

		//set password (dit kan leeg zijn maar dan wordt er om gevraagt
		session.setPassword("S39KF76tyGF6");

		//no key check on login
		session.setConfig("StrictHostKeyChecking", "no");

		//start verbinding
		session.connect();
		System.out.println("Connected..");

		//set SFTP channel als sessie SFTP
		channel = (ChannelSftp)session.openChannel("sftp");

		//connect
		channel.connect();

		//		System.out.println("Closing connections...");
		//		//channel en session disconnect.
		//		channel.disconnect();
		//		session.disconnect();
		//		System.out.println("Closed. Bye!");
	}

	public void SendFile(String filename, String country) {
		try {

			//push sftp
			channel.put(filename,"/var/www/html/weather/"+country+"/");
			System.out.println("File sent");
			
			Files.delete(Paths.get(filename));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
