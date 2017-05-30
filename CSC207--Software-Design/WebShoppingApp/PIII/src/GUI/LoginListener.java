package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginListener implements ActionListener{

	private guiBuild guiObject;

	public LoginListener(guiBuild guiObject){
		this.guiObject = guiObject;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand(); 
		System.out.println(command);

		if( command.equals( "Register" ))  {
			JTextField username = new JTextField();
			JTextField password = new JPasswordField();
			JButton admin = new JButton("Yes");
			AdListener AdList = new AdListener(guiObject);
			admin.addActionListener(AdList);
			
			Object[] message = {
					"Create Username:", username,
					"Create Password:", password,
					"Create Admin?", admin
			};
			Object[] message2 = {
					"Create Username:", username,
					"Create Password:", password,
					"Create Admin?", admin,
					"Failed to register user, please choose a different name!"
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Register New User", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				guiObject.userName = username.getText();
				String pass = new String(((JPasswordField) password).getPassword());
				Boolean worked = guiObject.project.addUser(guiObject.userName, pass, guiObject.admin);

				while (!worked && regForm == JOptionPane.OK_OPTION){
					regForm = JOptionPane.showConfirmDialog(null, message2, "Register New User", JOptionPane.OK_CANCEL_OPTION);
					guiObject.userName = username.getText();
					worked = guiObject.project.addUser(guiObject.userName, pass, false);
				}

				if (worked && !guiObject.admin){
					guiObject.sessionID = guiObject.project.login(guiObject.userName, pass);

					//get customer details
					JTextField custstrt = new JTextField();
					JTextField cty = new JTextField();
					Object[] message3 = {"Please enter customer details:",
							"Enter Street:", custstrt,
							"Enter City:", cty,
					};
					int regForm1 = JOptionPane.showConfirmDialog(null, message3, "Enter Customer Information", JOptionPane.OK_CANCEL_OPTION);

					if (regForm1 == JOptionPane.OK_OPTION) {
						guiObject.project.addCustomer(guiObject.userName, cty.getText(), custstrt.getText(), guiObject.sessionID);
					}
					guiObject.logUserIn(guiObject.userName, pass);
					guiObject.guiBuildFromUserLogin(guiObject.userName);
				}
				
				else{
					//bypass getting customer details!
					guiObject.logUserIn(guiObject.userName, pass);
					guiObject.guiBuildFromAdminLogin(guiObject.userName);
				}

			}

		}

		if ( command.equals( "Log In" ) ){
			JTextField username = new JTextField();
			JTextField password = new JPasswordField();
			Object[] message = {
					"Username:", username,
					"Password:", password
			};
			Object[] message2 = {
					"Username:", username,
					"Password:", password,
					"Failed to log in"
			};
			int LogForm = JOptionPane.showConfirmDialog(null, message, "Log In", JOptionPane.OK_CANCEL_OPTION);
			guiObject.logUserIn(username.getText(),new String(((JPasswordField) password).getPassword()));

			
			while (guiObject.sessionID == -1 && LogForm == JOptionPane.OK_OPTION){
				LogForm = JOptionPane.showConfirmDialog(null, message2, "Log In", JOptionPane.OK_CANCEL_OPTION);
				guiObject.logUserIn(username.getText(),new String(((JPasswordField) password).getPassword()));
			}

			if (!(guiObject.sessionID == -1)){
				int firstDigit = Integer.parseInt(Integer.toString(guiObject.sessionID).substring(0, 1));
				guiObject.admin = firstDigit == 1;
				if (!guiObject.admin){
					guiObject.admin = false;
					guiObject.loggedOut = false;
					guiObject.guiBuildFromUserLogin(username.getText());
				}
				else{
					guiObject.admin = true;
					guiObject.loggedOut = false;
					guiObject.guiBuildFromAdminLogin(username.getText());
				}

			}
		}

		if ( command.equals( "Log Out" ) ){
			int Logout = JOptionPane.showConfirmDialog(null, "Log out of session?", "Log In", JOptionPane.OK_CANCEL_OPTION);
			if (Logout == JOptionPane.OK_OPTION){
				guiObject.project.logout(guiObject.sessionID); //log out of session
				guiObject.guiBuildFromLogout();
				guiObject.userName = null;
				guiObject.sessionID = -1;
				guiObject.custID = -1;
				guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
				guiObject.mainPanel.revalidate();

			}


		}
	}
}
