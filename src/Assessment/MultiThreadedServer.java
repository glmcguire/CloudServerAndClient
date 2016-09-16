package Assessment;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * This is a multi-threaded server, designed to handle multiple clients. It allows
 * for the clients to download files from the server.
 * 
 * @author Gary McGuire
 *
 */

public class MultiThreadedServer implements IConstants, Runnable {

	private Socket socket = null;;
	protected ServerSocket serverSocket = null;	
	public static final int PORT = 2665;
	private String serverId = "Gary McGuire";
	private Thread clientThread = null;
	protected ExecutorService threadPool =
	        Executors.newFixedThreadPool(10);


	
	
	/*
	 * Flag to control the termination of while loop in the run() method.
	 * If assigned false, then the run() method terminates and this thread ends.
	 */
	private volatile boolean keepRunning = true;

	public MultiThreadedServer(){
		
	}
	
	
	/**
	 *  Runs and opens the server socket and creates a thread
	 * for each client that connects to it.
	 */
	
	public void run() {
		synchronized (this) {
			this.clientThread = Thread.currentThread();
		}
		
		DOMClientHandler retriever = new DOMClientHandler();
		List<String> serverData = new ArrayList<>();
		try {
			serverData = retriever.readServerIdFromXml();
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
			openServerSocket();
			while (keepRunning){
				try {
		             socket = this.serverSocket.accept();
		         } catch (IOException e) {
		             if(!keepRunning) {
		                 System.out.println("Server Stopped.") ;
		                 break;
		             }
		             throw new RuntimeException(
		                "Error accepting client connection", e);
		         }
		        
		            this.threadPool.execute(
		            new WorkerRunnable(socket, serverId));
			}
	
	}
	
	private void openServerSocket(){
		try {
			this.serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + PORT);
		}
		
	}
	
	protected synchronized void deleteServerFile (String fileName){
		File file = new File ("serverfiles", fileName);
		file.delete();
	}
	

	public static void main(String[] args) throws IOException { 
		MultiThreadedServer server = new MultiThreadedServer();
		Thread thread = new Thread(server);
		thread.start();
	}
}
