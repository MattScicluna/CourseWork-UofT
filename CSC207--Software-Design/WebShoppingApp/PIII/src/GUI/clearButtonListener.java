package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class clearButtonListener implements ActionListener{
		guiBuild guiObject;

		public clearButtonListener(guiBuild guiObject) {
			this.guiObject = guiObject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean worked = guiObject.project.cancelCart(guiObject.userName);
			if (worked){
				JOptionPane.showConfirmDialog(null, null, "Cleared Cart of all contents!", JOptionPane.OK_CANCEL_OPTION);
				guiObject.CustList.refreshCart();
				guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
			}
		}

	}

