package ichatty.main;

import ichatty.common.IMessage;
import ichatty.common.IMessageListener;
import ichatty.common.IUser;
import ichatty.common.impl.User;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.Box;
import java.awt.Panel;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.CharConversionException;
import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatView extends JDialog {

	private JPanel contentPane;
	private JTextField jtfPort;
	private ChatController controller;
	private JComboBox jcbUser;
	private JTextArea jtaHistory;
	private JTextArea jtaMessage;
	boolean userCbUpdating = false;
	boolean exitStatus = false;

	
	public void setController(ChatController cc) {
		controller = cc;
	}
	
	private IMessageListener listener = new IMessageListener() {		
		@Override
		public void onMessage(IMessage m) {
			String text = jtaHistory.getText();
			text += m.toString() + "\n";
			jtaHistory.setText(text);
		}
	};
	
	private void init() {
		exitStatus = false;
		initUserList();
		initHistory();				
		controller.setMessageListener(listener);
		
		String defUser = (String)jcbUser.getEditor().getItem();
		if (!defUser.isEmpty()) {
			switchUser();
		}
	}
	
	public boolean getExitStatus() {
		return exitStatus;
	}
	
	
	private void switchUser() {
		String userName = (String)jcbUser.getEditor().getItem();				
		controller.setUser(new User(userName, userName));
		String settings = controller.getNetworkSettings();
		if (settings != null) {
			jtfPort.setText(settings);
		}
		initHistory();		
	}
	
	private void initUserList() {
		userCbUpdating = true;
		jcbUser.removeAllItems();
		Set<IUser> users = controller.getAllUsers();	
		for (IUser u : users) {
			jcbUser.addItem(u.getName());
		}
		
		IUser cu = controller.getUser();
		if (cu != null) {			
			System.out.println("set item to: " + cu.getName());
			jcbUser.setSelectedItem(cu.getName());
		}						
		userCbUpdating = false;				
	}
	
	private void initHistory() {
		StringBuilder sb = new StringBuilder();				
		try {			
			List<IMessage> msgs = controller.getHistory();
			for (IMessage m : msgs) {
				sb.append(m.toString());
				sb.append("\n");
			}
			jtaHistory.setText(sb.toString());
		} catch (IOException e) {					
		}		
	}

	/**
	 * Create the frame.
	 */
	public ChatView() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				init();
			}
		});
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 473, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{375, 0};
		gbl_contentPane.rowHeights = new int[]{20, 100, 50, 10, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 3.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		Panel jpSettings = new Panel();
		GridBagConstraints gbc_jpSettings = new GridBagConstraints();
		gbc_jpSettings.fill = GridBagConstraints.HORIZONTAL;
		gbc_jpSettings.insets = new Insets(0, 0, 5, 0);
		gbc_jpSettings.gridx = 0;
		gbc_jpSettings.gridy = 0;
		contentPane.add(jpSettings, gbc_jpSettings);
		GridBagLayout gbl_jpSettings = new GridBagLayout();
		gbl_jpSettings.columnWidths = new int[]{55, 137, 46, 125, 0, 0};
		gbl_jpSettings.rowHeights = new int[]{14, 0};
		gbl_jpSettings.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_jpSettings.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		jpSettings.setLayout(gbl_jpSettings);
		
		JLabel lblUserName = new JLabel("User name:");
		GridBagConstraints gbc_lblUserName = new GridBagConstraints();
		gbc_lblUserName.insets = new Insets(0, 0, 0, 5);
		gbc_lblUserName.anchor = GridBagConstraints.EAST;
		gbc_lblUserName.gridx = 0;
		gbc_lblUserName.gridy = 0;
		jpSettings.add(lblUserName, gbc_lblUserName);
		
		jcbUser = new JComboBox();
		jcbUser.setEditable(true);
		jcbUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (userCbUpdating) {
					return;
				}
				switchUser();
			}
		});
		GridBagConstraints gbc_jcbUser = new GridBagConstraints();
		gbc_jcbUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcbUser.insets = new Insets(0, 0, 0, 5);
		gbc_jcbUser.gridx = 1;
		gbc_jcbUser.gridy = 0;
		jpSettings.add(jcbUser, gbc_jcbUser);
		
		JLabel lblPort = new JLabel("Host:port");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.insets = new Insets(0, 0, 0, 5);
		gbc_lblPort.gridx = 2;
		gbc_lblPort.gridy = 0;
		jpSettings.add(lblPort, gbc_lblPort);
		
		jtfPort = new JTextField();
		GridBagConstraints gbc_jtfPort = new GridBagConstraints();
		gbc_jtfPort.insets = new Insets(0, 0, 0, 5);
		gbc_jtfPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtfPort.gridx = 3;
		gbc_jtfPort.gridy = 0;
		jpSettings.add(jtfPort, gbc_jtfPort);
		jtfPort.setColumns(10);
		
		JButton jbSetNetwork = new JButton("Set");
		jbSetNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String userName = (String)jcbUser.getEditor().getItem();					
					controller.setUser(new User(userName, userName));
					controller.setNetworkSettings(jtfPort.getText());
					initUserList();
					initHistory();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(ChatView.this, "Invalid network settings!");
				}
			}
		});
		GridBagConstraints gbc_jbSetNetwork = new GridBagConstraints();
		gbc_jbSetNetwork.anchor = GridBagConstraints.WEST;
		gbc_jbSetNetwork.gridx = 4;
		gbc_jbSetNetwork.gridy = 0;
		jpSettings.add(jbSetNetwork, gbc_jbSetNetwork);
		
		jtaHistory = new JTextArea();
		GridBagConstraints gbc_jtaHistory = new GridBagConstraints();
		gbc_jtaHistory.fill = GridBagConstraints.BOTH;
		gbc_jtaHistory.insets = new Insets(0, 0, 5, 0);
		gbc_jtaHistory.gridx = 0;
		gbc_jtaHistory.gridy = 1;
		contentPane.add(jtaHistory, gbc_jtaHistory);
		
		jtaMessage = new JTextArea();
		GridBagConstraints gbc_jtaMessage = new GridBagConstraints();
		gbc_jtaMessage.fill = GridBagConstraints.BOTH;
		gbc_jtaMessage.insets = new Insets(0, 0, 5, 0);
		gbc_jtaMessage.gridx = 0;
		gbc_jtaMessage.gridy = 2;
		contentPane.add(jtaMessage, gbc_jtaMessage);
		
		Panel jpControls = new Panel();
		GridBagConstraints gbc_jpControls = new GridBagConstraints();
		gbc_jpControls.fill = GridBagConstraints.HORIZONTAL;
		gbc_jpControls.gridx = 0;
		gbc_jpControls.gridy = 3;
		contentPane.add(jpControls, gbc_jpControls);
		GridBagLayout gbl_jpControls = new GridBagLayout();
		gbl_jpControls.columnWidths = new int[]{59, 85, 0, 67, 53, 0};
		gbl_jpControls.rowHeights = new int[]{23, 0};
		gbl_jpControls.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_jpControls.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		jpControls.setLayout(gbl_jpControls);
		
		JButton jbSend = new JButton("Send");
		jbSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.sendMessage(jtaMessage.getText());
					jtaMessage.setText("");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(ChatView.this, "Failed to send message!");
				}
			}
		});
		GridBagConstraints gbc_jbSend = new GridBagConstraints();
		gbc_jbSend.anchor = GridBagConstraints.NORTHWEST;
		gbc_jbSend.insets = new Insets(0, 0, 0, 5);
		gbc_jbSend.gridx = 0;
		gbc_jbSend.gridy = 0;
		jpControls.add(jbSend, gbc_jbSend);
		
		JButton jbLogout = new JButton("Logout");
		jbLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitStatus = true;
				ChatView.this.setVisible(false);
			}
		});
		GridBagConstraints gbc_jbLogout = new GridBagConstraints();
		gbc_jbLogout.anchor = GridBagConstraints.NORTHWEST;
		gbc_jbLogout.insets = new Insets(0, 0, 0, 5);
		gbc_jbLogout.gridx = 3;
		gbc_jbLogout.gridy = 0;
		jpControls.add(jbLogout, gbc_jbLogout);
		
		JButton jbExit = new JButton("Exit");
		jbExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatView.this.setVisible(false);
			}
		});
		GridBagConstraints gbc_jbExit = new GridBagConstraints();
		gbc_jbExit.anchor = GridBagConstraints.NORTHWEST;
		gbc_jbExit.gridx = 4;
		gbc_jbExit.gridy = 0;
		jpControls.add(jbExit, gbc_jbExit);
	}

}
