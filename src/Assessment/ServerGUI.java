package Assessment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import gui.Constants.GUIConstants;

/**
 * This is the GUI that allows the client to access a server, request a list of
 * files stored on the server, and download those files.
 * 
 * @author Gary McGuire
 *
 */

public class ServerGUI extends JFrame implements GUIConstants, ActionListener {

	/**
	 * Default serial version added.
	 */
	private static final long serialVersionUID = 1L;

	private JTextField txtIPAddress = new JTextField();
	private JTextField txtDeveloperName;
	private JLabel lblAddress = new JLabel("IPAddress:");
	private JLabel lblDeveloper = new JLabel("Developer Name:");
	private JComboBox<String> cmbBox;
	private JButton btnConnect;

	private JPanel pnlCenter;
	private JPanel pnlCLeft;
	private JPanel pnlCCenter;
	private JPanel pnlCRright;
	private JButton btnDelete = new JButton("Delete");
	private JButton btnGetList = new JButton("Get List of Files");
	private JButton btnGetFile = new JButton("Get File");

	private DefaultListModel<String> serverListModel = new DefaultListModel<>();
	private JList<String> listServer = new JList<String>(serverListModel);
	private DefaultListModel<String> clientGetListModel = new DefaultListModel<>();
	private JList<String> listClientGetList = new JList<String>(clientGetListModel);
	private DefaultListModel<String> clientGetFileModel = new DefaultListModel<>();
	private JList<String> listClientGetFile = new JList<String>(clientGetFileModel);

	private JScrollPane scpServer;
	private JScrollPane scpClientGetList;
	private JScrollPane scpClientDownloadList;

	private JButton btnExit = new JButton("Exit");

	public ServerGUI() throws IOException {
		populateServerList();
		populateDownloadList();
		initGUI();
		pack();
		writeXmlToComboBox();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * This method builds all of the components of the GUI
	 */

	public void initGUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 143, 155, 143, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		///////////////////////////////////////////////// North
		///////////////////////////////////////////////// PANEL///////////////////////////////////////////////////////

		panel.add(lblAddress, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, EAST, NONE, new Insets(5, 10, 5, 10), 0, 0));
		panel.add(txtIPAddress,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
		txtIPAddress.setColumns(15);

		txtDeveloperName = new JTextField();

		panel.add(lblDeveloper,
				new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, EAST, NONE, new Insets(5, 10, 5, 10), 0, 0));
		panel.add(txtDeveloperName,
				new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0, WEST, HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
		txtDeveloperName.setColumns(15);

		cmbBox = new JComboBox<>();
		panel.add(cmbBox, new GridBagConstraints(2, 0, 1, 1, 2.0, 0.0, WEST, NONE, new Insets(5, 20, 5, 20), 0, 0));

		btnConnect = new JButton("Connect");
		panel.add(btnConnect, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(5, 10, 5, 10), 0, 0));

		////////////////////////////////////////////////// Center
		////////////////////////////////////////////////// Panel/////////////////////////////////////////////////////
		pnlCenter = new JPanel();
		getContentPane().add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new GridBagLayout());
		pnlCenter.add(btnExit,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));

		/*
		 * Below are the Left, Center, and Right (respectively) panels of
		 * pnlCenter; in a GridBagLayout
		 */

		//////////////////////////////// Center
		//////////////////////////////// Left//////////////////////////////
		pnlCLeft = new JPanel();
		pnlCLeft.setLayout(new GridBagLayout());
		pnlCLeft.setVisible(true);

		pnlCenter.add(pnlCLeft,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));
		pnlCLeft.add(btnDelete,
				new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));

		listServer.setVisibleRowCount(30);
		scpServer = new JScrollPane(listServer);
		scpServer.setPreferredSize(new Dimension(150, 300));

		pnlCLeft.add(scpServer,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, CENTER, BOTH, new Insets(5, 10, 5, 10), 0, 0));

		Border brdBlackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(brdBlackline, "Server Files");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		scpServer.setBorder(titledBorder);

		////////////////////////// Center Center////////////////////////////////
		pnlCCenter = new JPanel();
		pnlCCenter.setLayout(new GridBagLayout());

		pnlCenter.add(pnlCCenter,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));

		listClientGetList.setVisibleRowCount(30);
		scpClientGetList = new JScrollPane(listClientGetList);
		scpClientGetList.setPreferredSize(new Dimension(150, 300));

		pnlCCenter.add(scpClientGetList,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, CENTER, BOTH, new Insets(5, 10, 5, 10), 0, 0));
		pnlCCenter.add(btnGetList,
				new GridBagConstraints(1, 2, 1, 1, 3.0, 3.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));

		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(brdBlackline, "Available Files");
		titledBorder1.setTitleJustification(TitledBorder.CENTER);
		scpClientGetList.setBorder(titledBorder1);

		/////////////////////////// Center right////////////////////////////////
		pnlCRright = new JPanel();
		pnlCRright.setLayout(new GridBagLayout());
		pnlCenter.add(pnlCRright,
				new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));
		pnlCRright.add(btnGetFile,
				new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, CENTER, NONE, new Insets(5, 10, 5, 10), 0, 0));

		listClientGetFile.setVisibleRowCount(30);
		scpClientDownloadList = new JScrollPane(listClientGetFile);
		scpClientDownloadList.setPreferredSize(new Dimension(150, 300));

		pnlCRright.add(scpClientDownloadList,
				new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, CENTER, BOTH, new Insets(5, 10, 5, 10), 0, 0));

		TitledBorder titledBorder2 = BorderFactory.createTitledBorder(brdBlackline, "Downloaded Files");
		titledBorder2.setTitleJustification(TitledBorder.CENTER);
		scpClientDownloadList.setBorder(titledBorder2);

		/////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////
		////// Anonymous Listeners //////
		/////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////

		/**
		 * Instance of the server client to allow the listeners to access
		 * various method calls.
		 */
		MultiServerClient clientConnect = new MultiServerClient();
		ComboBoxModel<String> model = cmbBox.getModel();

		////////////// Connect Button///////////////////
		btnConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				///////// Connect to server here/////////
				if (e.getSource() == btnConnect) {

					try {
						String serverId = clientConnect.connect(txtIPAddress.getText());
						System.out.println("Connected to server: " + serverId);
						String ipAddress = txtIPAddress.getText();

						/**
						 * Adds IPAddresses to combo box if they aren't already
						 * contained in the combo box.
						 */
						int i = 0;
						do {
							if (!(cmbBox.getItemAt(i).toString()).equals(ipAddress)) {
								cmbBox.addItem(ipAddress);
							}
							i++;
						} while (i < model.getSize());

						txtDeveloperName.setText(serverId);
						txtDeveloperName.setEditable(false);

					} catch (IOException | ParserConfigurationException | TransformerException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(ServerGUI.this, "Cannot Connect to server");
					}
				}
			}
		});

		cmbBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println("select item=" + cmbBox.getSelectedItem());
					txtIPAddress.setText(cmbBox.getSelectedItem().toString());
				}
			}
		});

		///////////////// GetListOfFiles button/////////////////////////
		btnGetList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					clientGetListModel.clear();
					List<String> listOfFiles = new Vector<>();
					listOfFiles = clientConnect.requestListOfFiles();
					System.out.println(listOfFiles);
					for (String file : listOfFiles) {
						clientGetListModel.addElement(file);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});

		///////////////// Download file Button////////////////
		btnGetFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					int index = listClientGetList.getSelectedIndex();
					if (index >= 0) {
						String fileName = clientGetListModel.getElementAt(index);
						System.out.println("Transferring file " + fileName);
						clientConnect.requestFile(fileName);
						System.out.println("Exit: Requesting file.");

						clientGetListModel.remove(index);
						clientGetFileModel.addElement(fileName);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		MultiThreadedServer serverInstance = new MultiThreadedServer();
		/////////////////// Delete Button ///////////////////
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = listServer.getSelectedIndex();
				if (index >= 0) {
					String fileName = serverListModel.getElementAt(index);
					serverInstance.deleteServerFile(fileName);
					serverListModel.remove(index);
				}
			}
		});

		///////////////// EXIT Button ///////////////////////
		btnExit.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnExit) {
			storeIPToXml();
			dispose();

		}
	}

	/**
	 * Stores the IPAddresses from the combo box to an XML file.
	 */

	public void storeIPToXml() {

		String ipAddress = txtIPAddress.getText();
		cmbBox.addItem(ipAddress);
		DOMClientHandler ipAddressStore = new DOMClientHandler();
		try {
			List<String> serverIPAddresses = new ArrayList<>();
			for (int i = 0; i < cmbBox.getItemCount(); i++) {
				serverIPAddresses.add(cmbBox.getItemAt(i));
			}
			ipAddressStore.writeServerIPAddressesToXML(serverIPAddresses);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Writes the stored IPAddresses from the XML file into the combo box.
	 */

	public void writeXmlToComboBox() {
		try {
			DOMClientHandler ipAddressRead = new DOMClientHandler();
			List<String> serverIPAddresses = new ArrayList<>();
			serverIPAddresses = ipAddressRead.readServerIdFromXml();
			for (String read : serverIPAddresses) {
				cmbBox.addItem(read);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * This method populates the server JList with a list of files contained in
	 * the server.
	 * 
	 * @throws IOException
	 */
	public void populateServerList() throws IOException {
		File serverFilesDir = new File("serverfiles");
		if (!serverFilesDir.exists() || serverFilesDir.isFile()) {
			System.out.println("'serverfiles' is not an existing directory");
			throw new IOException("'serverfiles' directory does not exist.");
		}
		File[] files = serverFilesDir.listFiles();
		for (File file : files) {
			serverListModel.addElement(file.getName());
		}

	}

	/**
	 * This method populates the client-side Download JList with a list of files
	 * contained in the client.
	 * 
	 * @throws IOException
	 */
	public void populateDownloadList() throws IOException {
		File clientFilesDir = new File("clientfiles");
		if (!clientFilesDir.exists() || clientFilesDir.isFile()) {
			System.out.println("'clientfiles' is not an existing directory");
			throw new IOException("'clientfiles' directory does not exist.");
		}
		File[] files = clientFilesDir.listFiles();
		for (File file : files) {
			clientGetFileModel.addElement(file.getName());
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerGUI test = new ServerGUI();
		test.setVisible(true);
	}
}
