package ichatty.login;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dialog.ModalityType;

public class LoginView extends JFrame {

	private JPanel contentPane;
	private JTextField jtfPort;
	private JComboBox jcbUserName;
	private ILoginController loginController;
	private boolean exitStatus = false;

	public void setController(ILoginController lc) {
		loginController = lc;		
	}
	
	protected void init() {
		exitStatus = false;
		jcbUserName.removeAllItems();
		List<String> names = loginController.getUserNames();
		for (String s : names) {
			jcbUserName.addItem(s);
		}
		jtfPort.setText("" + loginController.getUserPort());
	}
	
	public boolean run() {
		this.setVisible(true);
		init();
		try {
			synchronized (this) {
				this.wait();
			}			
		} catch (InterruptedException e) {
		}
		return this.exitStatus;
	}	
	
	public boolean getExitStatus() {
		return exitStatus;
	}
	
	private void done(boolean exitStatus) {
		this.exitStatus = exitStatus;
		this.setVisible(false);
		synchronized (this) {
			this.notify();
		}				
	}	

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setResizable(false);		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				init();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				done(exitStatus);
			}
		});
		setTitle("iChatty login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 201, 114);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("User name:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		jcbUserName = new JComboBox();
		jcbUserName.setEditable(true);
		GridBagConstraints gbc_jcbUserName = new GridBagConstraints();
		gbc_jcbUserName.insets = new Insets(0, 0, 5, 0);
		gbc_jcbUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcbUserName.gridx = 1;
		gbc_jcbUserName.gridy = 0;
		contentPane.add(jcbUserName, gbc_jcbUserName);
		
		JLabel lblMainAppPort = new JLabel("Data port:");
		GridBagConstraints gbc_lblMainAppPort = new GridBagConstraints();
		gbc_lblMainAppPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblMainAppPort.gridx = 0;
		gbc_lblMainAppPort.gridy = 1;
		contentPane.add(lblMainAppPort, gbc_lblMainAppPort);
		
		jtfPort = new JTextField();
		GridBagConstraints gbc_jtfPort = new GridBagConstraints();
		gbc_jtfPort.insets = new Insets(0, 0, 5, 0);
		gbc_jtfPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtfPort.gridx = 1;
		gbc_jtfPort.gridy = 1;
		contentPane.add(jtfPort, gbc_jtfPort);
		jtfPort.setColumns(10);
		
		JButton jbLogin = new JButton("Login");
		jbLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String portString = jtfPort.getText();
				String userString = (String)jcbUserName.getEditor().getItem();
				
				short port = 0;
				try {
					port = Short.parseShort(portString);
				} catch (Exception ex) {
				}
				if (port < 1000) {
					JOptionPane.showMessageDialog(LoginView.this, "Invalid port setting, should be a number between 1000 and 32767");
					return;
				}				
				if (userString.isEmpty()) {
					JOptionPane.showMessageDialog(LoginView.this, "Invalid user name, should consit of letters A-Z, a-z");
					return;
				}
				
				loginController.setUserName(userString);
				loginController.setUserPort(port);				
				done(true);				
			}
		});
		GridBagConstraints gbc_jbLogin = new GridBagConstraints();
		gbc_jbLogin.insets = new Insets(0, 0, 0, 5);
		gbc_jbLogin.gridx = 0;
		gbc_jbLogin.gridy = 2;
		contentPane.add(jbLogin, gbc_jbLogin);
		
		JButton jbExit = new JButton("Exit");
		jbExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				done(false);
			}
		});
		GridBagConstraints gbc_jbExit = new GridBagConstraints();
		gbc_jbExit.anchor = GridBagConstraints.WEST;
		gbc_jbExit.gridx = 1;
		gbc_jbExit.gridy = 2;
		contentPane.add(jbExit, gbc_jbExit);
	}

}
