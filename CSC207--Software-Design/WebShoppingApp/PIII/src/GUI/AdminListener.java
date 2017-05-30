package GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminListener implements ActionListener{


	private guiBuild guiObject;

	public AdminListener(guiBuild guiObject){
		this.guiObject = guiObject;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); 
		System.out.println(command);
		if( command.equals( "Add Category" ))  {
			JTextField catName = new JTextField();
			Object[] message = {
					"Enter name:", catName,
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Create new category", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				guiObject.project.addCategory(catName.getText(), guiObject.sessionID);
			}
		}
		if( command.equals( "Add Product" ))  {
			JTextField prodName = new JTextField();
			JTextField category = new JTextField();
			JTextField price = new JTextField();
			JTextField ImageFileName = new JTextField();

			Object[] message = {
					"Enter name:", prodName,
					"Enter Category Name:", category,
					"Enter Price", price,
					"Enter file location of of image", ImageFileName
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Create new Product", JOptionPane.OK_CANCEL_OPTION);


			if (regForm == JOptionPane.OK_OPTION) {
				guiObject.project.addProduct(prodName.getText(), 
						guiObject.project.getNumFromName(category.getText(),guiObject.sessionID), 
						Float.parseFloat(price.getText()),
						guiObject.sessionID,
						ImageFileName.getText());
				guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
				System.out.println("product named " + prodName.getText() + " was added" + 
						" into category # "+ guiObject.project.getNumFromName(category.getText(),guiObject.sessionID)
						+ "which is " +category.getText());
			}
		}

		if( command.equals( "Add A Warehouse" ))  {
			JTextField CityName = new JTextField();
			Object[] message = {
					"Enter City:", CityName,
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Add Warehouse to System?", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				guiObject.project.addDistributionCenter(CityName.getText(),guiObject.sessionID);
			}
		}

		if( command.equals( "Maintain Product Quantities" ))  {
			JTextField prodName = new JTextField();
			JTextField distCen = new JTextField();
			JTextField qty = new JTextField();
			Object[] message = {
					"Enter Product Name:", prodName,
					"Enter Distribution Center", distCen,
					"Enter Quantity", qty
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Update Product Supply at a Distribution Center", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				System.out.println("quantity " + Integer.parseInt(qty.getText()) + " of " + guiObject.project.getProdIDFromProdName(prodName.getText()) + 
						" which is named " + prodName.getText() + " was added to center in " + distCen.getText());
				guiObject.project.updateQuantity(guiObject.project.getProdIDFromProdName(prodName.getText()), 
						distCen.getText(), Integer.parseInt(qty.getText()), guiObject.sessionID);
				guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
			}
		}

		if( command.equals( "Maintain Shipping Graph" ))  {
			JTextField cityA = new JTextField();
			JTextField cityB = new JTextField();
			JTextField distance = new JTextField();
			Object[] message = {
					"Enter City name:", cityA,
					"Enter Neighbouring City name:", cityB,
					"Enter Distance between cities:", distance
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Create new route", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				guiObject.project.addRoute(cityA.getText(), cityB.getText(), Integer.parseInt(distance.getText()), guiObject.sessionID);
			}
		}

		if( command.equals( "Produce Sales Report" ))  {

			JTextField catName = new JTextField();
			Object[] message = {
					"Enter Category Name:", catName
			};
			int regForm = JOptionPane.showConfirmDialog(null, message, "Select Category for Invoice Report", JOptionPane.OK_CANCEL_OPTION);

			if (regForm == JOptionPane.OK_OPTION) {
				List<String> inMsg = guiObject.project.getSalesReport(guiObject.sessionID,
						guiObject.project.getNumFromName(catName.getText(), guiObject.sessionID)); 
				JFrame InvoiceFrame = new JFrame(guiObject.userName + "'s Invoices");
				JPanel InvoicePanel = new JPanel(new GridBagLayout());
				InvoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				InvoiceFrame.getContentPane().add(InvoicePanel, BorderLayout.CENTER);
				GridBagConstraints InConstraints = new GridBagConstraints();
				InConstraints.gridwidth = 1;
				InConstraints.weighty = 1;
				int counter = 0;
				for (String s : inMsg){
					System.out.println(s);
					InConstraints.gridx = 0;
					InConstraints.gridy = counter;
					JLabel sLab = new JLabel(s);
					InvoicePanel.add(sLab,InConstraints);
					counter ++;
				}

				InvoiceFrame.setVisible(true);
				InvoiceFrame.setSize(500, 300);
				InvoiceFrame.revalidate();
			}
		}
	}
}

