import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;

public class FileClient {

	public final static int SOCKET_PORT = 13267;      // we can change this
	public final static String SERVER = "localhost";  // localhost
	public final static String FILE_TO_RECEIVED = "/home/kls102/clientStorage/tas.png";
	public final static String FILE_TO_SEND = "/home/kls102/Documents/Git.png";  // you may change this
																														

	public final static int FILE_SIZE = 6022386; // file size is temporary hard coded
																				

	public static void main (String [] args ) throws IOException {
		FileClient fc = new FileClient();
	   fc.sendFile();
	}

	public void recieveFile() throws IOException{
	int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		try {
			sock = new Socket(SERVER, SOCKET_PORT);
			System.out.println("Connecting...");

			// receive file
			byte [] mybytearray  = new byte [FILE_SIZE];
			InputStream is = sock.getInputStream();
			fos = new FileOutputStream(FILE_TO_RECEIVED);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray,0,mybytearray.length);
			current = bytesRead;

			do {
				 bytesRead =
						is.read(mybytearray, current, (mybytearray.length-current));
				 if(bytesRead >= 0) current += bytesRead;
			} while(bytesRead > -1);

			bos.write(mybytearray, 0 , current);
			bos.flush();
			System.out.println("File " + FILE_TO_RECEIVED
					+ " downloaded (" + current + " bytes read)");
		}
		finally {
			if (fos != null) fos.close();
			if (bos != null) bos.close();
			if (sock != null) sock.close();
		}
	}

	  public void sendFile() throws IOException{
  	FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;;
		Socket sock = null;
		try {
			sock = new Socket(SERVER, SOCKET_PORT);
			while (true) {
				System.out.println("Waiting...");
				try {
					System.out.println("Accepted connection : " + sock);
					// send file
					File myFile = new File (FILE_TO_SEND);
					byte [] mybytearray  = new byte [(int)myFile.length()];
					// to read byte oriented data from file
					fis = new FileInputStream(myFile);
					bis = new BufferedInputStream(fis);
					// It read the bytes from the specified byte-input stream into a specified byte array
					bis.read(mybytearray,0,mybytearray.length);
					os = sock.getOutputStream();
					System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
					os.write(mybytearray,0,mybytearray.length);
					os.flush();
					System.out.println("Done.");
				}
				finally {
					if (bis != null) bis.close();
					if (os != null) os.close();
					if (sock!=null) sock.close();
				}
			}
		}catch(Exception e){}

  }

}