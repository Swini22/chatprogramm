package view;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import model.Service;

public class LoginView extends JFrame{

	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private TextField usernameText;
	private JPasswordField passwordText;
	private JButton okButton;
	private JButton cancleButton;
	private Service db_service;

	public LoginView(Service db_service) {
		super("Login");
		this.db_service = db_service;
		prepareGui();
	}

	private void prepareGui() {
		
		setSize(400, 400);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		usernameLabel = new JLabel("Username");
		usernameLabel.setSize(150, 30);
		usernameLabel.setLocation(50, 100);

		passwordLabel = new JLabel("Passwort");
		passwordLabel.setSize(150, 30);
		passwordLabel.setLocation(50, 200);

		usernameText = new TextField();
		usernameText.setSize(150, 30);
		usernameText.setLocation(200, 100);

		passwordText = new JPasswordField();
		passwordText.setSize(150, 30);
		passwordText.setLocation(200, 200);

		okButton = new JButton("OK");
		okButton.setSize(130, 30);
		okButton.setLocation(50, 300);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameText.getText();
				String password = passwordText.getText();
				if (username != null && password != null) {
					if (db_service.checkLogin(username, password)){
						dispose();
						new MainMenuView(db_service, username);
					}else{
						JOptionPane.showMessageDialog(new JFrame(), "wrong username or passwod");
					}
				}
			}
		});

		cancleButton = new JButton("CANCEL");
		cancleButton.setSize(130, 30);
		cancleButton.setLocation(200, 300);
		cancleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		add(usernameLabel);
		add(passwordLabel);
		add(usernameText);
		add(passwordText);
		add(okButton);
		add(cancleButton);
		setVisible(true);
	}


	public void dispose() {
		setVisible(false);
	}

}
