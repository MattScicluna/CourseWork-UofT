package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SearchListener implements ActionListener{

	private guiBuild guiObject;

	public SearchListener(guiBuild guiObject) {
		this.guiObject = guiObject;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		JTextField queryCat = new JTextField();
		JTextField queryMin = new JTextField();
		JTextField queryMax = new JTextField();

		Object[] message4 = {"Type in category name or PRICEUp, PRICEDown, AVAILUp, AVAILDown, ALPHAUp or ALPHAdown", queryCat,
				"From: ", queryMin,
				"to: ", queryMax,
				"<html> Type in category name to limit search to get products from a specific category<br>"
						+ "PRICEUp and PRICEDown to sort by price <br>"
						+ "AVAILUp and AVAILDown to sort by availability <br>"
						+ "ALPHAUp or ALPHAdown to sort by alphabetical order <br>"
						+ "To sort by something other then price, set price fields to 0.<html>"
		};

		Object[] message5 = {"Type in category name or PRICEUp, PRICEDown, AVAILUp, AVAILDown, ALPHAUp or ALPHAdown", queryCat,
				"From: ", queryMin,
				"to: ", queryMax,
				"<html> You did not enter an appropriate string! <br>"
						+ "Type in category name to limit search to get products from a specific category<br>"
						+ "PRICEUp and PRICEDown to sort by price <br>"
						+ "AVAILUp and AVAILDown to sort by availability <br>"
						+ "ALPHAUp or ALPHAdown to sort by alphabetical order <br>"
						+ "To sort by something other then price, set price fields to 0.<br>"
						+ "You can thank Vincent and Gal for that!! <html>"
		};

		int regForm1 = JOptionPane.showConfirmDialog(null, message4, "Enter Search Information", JOptionPane.OK_CANCEL_OPTION);
		boolean worked = false;

		while (regForm1 == JOptionPane.OK_OPTION && !worked){
			try {
				guiObject.updateProds(guiObject.project.browseShop(Float.parseFloat(queryMin.getText()),Float.parseFloat(queryMax.getText()),queryCat.getText()));
				System.out.println("Query for " + queryCat.getText() + ", from " + Float.parseFloat(queryMin.getText()) + " to " + Float.parseFloat(queryMax.getText()));
				worked = true;

			}
			catch (NumberFormatException ex) {
				regForm1 = JOptionPane.showConfirmDialog(null, message5, "Enter Search Information", JOptionPane.OK_CANCEL_OPTION);
			}

		}
	}

}