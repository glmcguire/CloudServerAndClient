package Assessment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * This class handles each thread (created for each client) separately
 * and performs the various functions.
 * 
 * @author Gary McGuire
 *
 */


public class WorkerRunnable implements Runnable, IConstants {
	
	 protected Socket socket = null;
	 protected String serverId   = null;
	 private volatile boolean keepRunning = true;

	 public WorkerRunnable(Socket socket, String serverId) {
	        this.socket = socket;
	        this.serverId = serverId;
	 }
	 
	 public WorkerRunnable(){
	 }

	  public void run() {
		  try {
				int i = 0;
				while (keepRunning)	{
					/// This makes it possible for FILE_EXISTS and FILE_DOESNT_EXIST go into outputstream correctly
					while (i == 0){
						System.out.println("Writing to client serverId " + serverId + "."); 
						socket.getOutputStream().write(serverId.getBytes());		
						socket.getOutputStream().write(END);
						System.out.println("Writing done");
						i++;
					}
						
					// If the client closes the connection, disconnect
					int command = socket.getInputStream().read();
					
					if (command == -1) {

					}
					if (command == LIST_FILES) {
						System.out.println("Received sendFileList command '" + (char) command + "'");
						sendFileList();

					}
					if (command == SEND_FILE){
						System.out.println("Received sendFile command '" + (char) command + "'");
						sendFile();
					}
					
					new Thread().start();
					i++;
					
				}
				new Thread().start();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {			
			}
	  }
	  /**
	   * Sends the list of files stored  in the directory to the client.
	   * 
	   * @throws IOException
	   */
	  private synchronized void sendFileList() throws IOException {
			File serverFilesDir = new File("serverfiles");
			if (! serverFilesDir.exists() || serverFilesDir.isFile()) {
				System.out.println("'serverfiles' is not an existing directory");
				throw new IOException("'serverfiles' directory does not exist.");
			}
			File[] files = serverFilesDir.listFiles();		
			for (File file: files) {
				socket.getOutputStream().write(file.getName().getBytes());
				socket.getOutputStream().write(END);
			}	
			socket.getOutputStream().write(END_OF_LIST);
		}
		
		
		/**
		 * The sendFile() method sends the file to the client that was requested.
		 * 
		 * @throws IOException
		 */
		private synchronized void sendFile() throws IOException {
			
				System.out.println("Server ready to send");
				if (socket.isConnected()) {
					System.out.println("Server connected, ready to send");
					StringBuilder stringBuilder = new StringBuilder();
					int character; 
					while ((character = socket.getInputStream().read()) > -1 && character != END
							&& (char)character != END_OF_LIST) {						
						stringBuilder.append((char)character);
					}
					String fileName = stringBuilder.toString();
					File file = new File (System.getProperty("user.dir") + System.getProperty("file.separator")
							+ "serverfiles", fileName);
					
					/////////////Adding file length///////////////////////
					String totalLength = String.valueOf(file.length());
					System.out.println("Server:" + totalLength);
					if (file.exists()){
						socket.getOutputStream().write(FILE_EXISTS);
						socket.getOutputStream().write(totalLength.getBytes());
						socket.getOutputStream().write(END);
					} else {
						socket.getOutputStream().write(FILE_DOESNT_EXIST);
					}
					//////////////////////////////////////////////////////
				
					
					int numberOfBytesRead = 0;
					byte[] buffer = new byte[1024*2];
					FileInputStream fis = new FileInputStream(file);
					
					while ((numberOfBytesRead = fis.read(buffer)) > -1){
						socket.getOutputStream().write(buffer, 0, numberOfBytesRead);
					}
				
					fis.close();
				} else {
					System.out.println("Socket not connected");
				}
					
		}	
		
	     
	    
}

