package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import finalproject.ProjectV1.ShoppingCartItem;

public class canButtonListener implements ActionListener{
	guiBuild guiObject;
	ShoppingCartItem p;

	public canButtonListener(guiBuild guiObject, ShoppingCartItem p) {
		this.guiObject = guiObject;
		this.p = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		guiObject.project.removeFromShoppingCart(p.getProdID(),  guiObject.sessionID, guiObject.custID);
		guiObject.CustList.refreshCart();
	}

}
