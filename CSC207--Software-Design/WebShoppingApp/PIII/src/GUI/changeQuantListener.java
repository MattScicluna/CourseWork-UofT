package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import finalproject.ProjectV1.ShoppingCartItem;

public class changeQuantListener implements ActionListener{

	private guiBuild guiObject;
	private ShoppingCartItem theProd;

	public changeQuantListener(guiBuild guiObject, ShoppingCartItem theProd){
		this.guiObject = guiObject;
		this.theProd = theProd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField qtyChoice = new JTextField();
		Object[] message = {"Change Quantity to: ", qtyChoice};
		Object[] message2 = {"Change Quantity to: ", qtyChoice, "Please enter a valid number"};

		int regForm1 = JOptionPane.showConfirmDialog(null, message, "change quantity to:", JOptionPane.OK_CANCEL_OPTION);
		boolean worked = false;
		while(regForm1 == JOptionPane.OK_OPTION && !worked){
			try {
				guiObject.project.updateCartQuantity(theProd.getProdID(), Integer.parseInt(qtyChoice.getText()), guiObject.sessionID, guiObject.custID);
				worked = true;
				guiObject.CustList.refreshCart();
			}
			catch(NumberFormatException ex){
				regForm1 = JOptionPane.showConfirmDialog(null, message2, "change quantity to:", JOptionPane.OK_CANCEL_OPTION);
			}
		}
	}
}
