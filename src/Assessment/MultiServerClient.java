package Assessment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * This client connects to a server and is able to request files to be listed
 * and downloaded.
 * 
 * @author Gary McGuire
 *
 */

public class MultiServerClient implements IConstants {

	private Socket socket;
	public static final String FILE_REQUESTED = "serverfiles/";

	public MultiServerClient() {

	}

	/**
	 * Connects to the server socket.
	 * 
	 * @param ipAddress
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */

	protected String connect(String ipAddress)
			throws UnknownHostException, IOException, ParserConfigurationException, TransformerException {
		socket = new Socket(ipAddress, MultiThreadedServer.PORT);
		System.out.println("Connected to server.");
		String serverId = readFromServer();
		return serverId;
	}

	/**
	 * Reads the serverId from the server from the socket input stream. This is
	 * done by reading Reads one character at a time from the input stream until
	 * the END character is read or until the socket connection is closed
	 * (character == -1).
	 * 
	 * @return String
	 */
	protected synchronized String readFromServer() {
		try {
			// Verify that the socket connection to the server is still open.
			if (socket.isConnected()) {
				StringBuilder stringBuilder = new StringBuilder();
				InputStream input = socket.getInputStream();
				int character;
				while ((character = input.read()) > -1 && character != END) {
					stringBuilder.append((char) character);
				}
				return stringBuilder.toString();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {

		}
		return null;
	}

	/**
	 * Sends a request to the server to send back a list of all available files
	 * found in its file directory.
	 * 
	 * @throws IOException
	 */
	public synchronized List<String> requestListOfFiles() throws IOException {
		if (socket.isClosed()) {
			return null;
		}
		OutputStream outputStream = socket.getOutputStream();
		InputStream inputStream = socket.getInputStream();
		ArrayList<String> listOfFiles = new ArrayList<>();
		outputStream.write(LIST_FILES);

		int character;

		StringBuilder stringBuilder = new StringBuilder();
		while ((character = inputStream.read()) != END_OF_LIST && character != -1) {
			stringBuilder.append((char) character);
			while ((character = inputStream.read()) != END) {
				stringBuilder.append((char) character);
			}
			listOfFiles.add(stringBuilder.toString());
			stringBuilder.setLength(0);
		}
		if (character == -1) {
			return null;
		}
		return listOfFiles;
	}

	/**
	 * This method requests a specified file from the server to be sent to the
	 * client.
	 */
	protected synchronized void requestFile(String fileName) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		InputStream inputStream = socket.getInputStream();

		outputStream.write(SEND_FILE);
		outputStream.write(fileName.getBytes());
		outputStream.write(END);

		int input = inputStream.read();

		if (input == FILE_EXISTS) {
			File file = new File("clientfiles", fileName);
			file.createNewFile();

			OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
			try {
				//Increase buffer to allow for larger files to be transferred.
				byte[] buffer = new byte[1024 * 2];

				StringBuilder lengthText = new StringBuilder();

				//////////// This returns the file lengths////////////////////
				int character;
				while ((character = socket.getInputStream().read()) != END) {
					lengthText.append((char) character);
				}
				long totalLength = Long.parseLong(lengthText.toString());
				System.out.println("total length=" + totalLength);
				/////////////////////////////////////////////////////////////

				int numberOfBytesRead;
				int totalOfBytesRead = 0;
				while ((numberOfBytesRead = inputStream.read(buffer)) > 0 && totalOfBytesRead < totalLength) {
					fos.write(buffer, 0, numberOfBytesRead);
					// Flush contents of fos on each iteration
					fos.flush();

					totalOfBytesRead += numberOfBytesRead;

					if (totalOfBytesRead >= totalLength) {
						System.out.println("totalBytesRead >= totalLength");
						break;
					}
				}
			} catch (Exception e) {
				
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "File does not exist.", "File Not Present", JOptionPane.ERROR_MESSAGE);
		}
	}
}
