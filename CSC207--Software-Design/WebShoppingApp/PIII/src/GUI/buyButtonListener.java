package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import finalproject.Product;

public class buyButtonListener implements ActionListener{

	private guiBuild guiObject;
	private Product theProd;

	public buyButtonListener(guiBuild guiObject, Product theProd){
		this.guiObject = guiObject;
		this.theProd = theProd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (guiObject.loggedOut){
			Object[] message = {"Log in to buy stuff!!"};
			JOptionPane.showConfirmDialog(null, message, "Error!", JOptionPane.OK_CANCEL_OPTION);
		}
		else if (guiObject.admin){
			Object[] message = {"Users with your role cannot buy items!"};
			JOptionPane.showConfirmDialog(null, message, "Error!", JOptionPane.OK_CANCEL_OPTION);
		}
		else{
			JTextField qty = new JTextField();
			Object[] message = {"Add how much of " + theProd.getName() + " to the cart ?", qty};
			Object[] message2 = {"Add how much of " + theProd.getName() + " to the cart ?", qty, "Please enter a valid number"};

			int regForm1 = JOptionPane.showConfirmDialog(null, message, "Enter Information", JOptionPane.OK_CANCEL_OPTION);
			boolean worked = false;
			boolean addedToCart = false;
			while(regForm1 == JOptionPane.OK_OPTION && !worked){
				try {
					addedToCart = guiObject.project.addToShoppingCart(theProd.getID(), Integer.parseInt(qty.getText()), guiObject.sessionID, guiObject.custID);
					worked = true;
				}
				catch(NumberFormatException ex){
					regForm1 = JOptionPane.showConfirmDialog(null, message2, "Enter Information", JOptionPane.OK_CANCEL_OPTION);
				}
			}
			
			if(addedToCart){
				Object[] message4 = {Integer.parseInt(qty.getText()) + " of " +theProd.getName() +" added to the cart successfully"};
				JOptionPane.showConfirmDialog(null,message4, "Success", JOptionPane.OK_CANCEL_OPTION);
				guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
			}
			else{
				Object[] message3 = {Integer.parseInt(qty.getText()) + " of " + theProd.getName()+" could not be added to the cart"};
				JOptionPane.showConfirmDialog(null,message3, "Error", JOptionPane.OK_CANCEL_OPTION);
			}
		}

	}



}
