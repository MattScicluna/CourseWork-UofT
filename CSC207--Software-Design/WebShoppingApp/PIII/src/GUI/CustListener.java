package GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import finalproject.ProjectV1.ShoppingCartItem;

public class CustListener implements ActionListener{

	private guiBuild guiObject;
	ArrayList<ShoppingCartItem> Cart;
	JFrame cartFrame;
	JPanel cartPanel;
	JLabel f1;
	JLabel f2;
	JButton f3;
	JButton f4;
	JButton f5;

	public CustListener(guiBuild guiObject){
		this.guiObject = guiObject;
	}

	public void refreshCart(){
		cartFrame.dispose();
		makeCart();
		guiObject.updateProds(guiObject.project.browseShop(0, 0, "PRICEUp"));
	}

	public void makeCart(){
		Cart = (ArrayList<ShoppingCartItem>) guiObject.project.getShoppingCart(guiObject.userName);

		//build frame
		cartFrame = new JFrame(guiObject.userName + "'s Cart");
		cartPanel = new JPanel(new GridBagLayout());
		cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cartFrame.getContentPane().add(cartPanel, BorderLayout.CENTER);
		GridBagConstraints outputConstraints = new GridBagConstraints();
		outputConstraints.gridwidth = 1;
		int counter = 1;
		//JLabel f6 = new JLabel("Warning! this operation cannot be undone!");
		f5 = new JButton("Clear Cart");
		cartPanel.add(f5, outputConstraints);
		clearButtonListener clearButtonList = new clearButtonListener(guiObject);
		f5.addActionListener(clearButtonList);
		//cartPanel.add(f6, outputConstraints);
		f5.setHorizontalTextPosition(SwingConstants.LEFT);
		//f6.setHorizontalTextPosition(SwingConstants.LEFT);

		cartFrame.setVisible(true);

		for (ShoppingCartItem p: Cart){
			outputConstraints.gridx = 0;
			outputConstraints.gridy = counter;
			outputConstraints.weighty = 1;
			int ID = p.getProdID();
			int total = 0;
			for (int i : p.getQuantity()){
				total += i;
			}
			f1 = new JLabel("Product: " + guiObject.project.getProdNameFromProdID(ID));
			f2 = new JLabel("Quantity: " + total);
			f3 = new JButton("Change Quantity");
			f4 = new JButton("Remove From Cart");
			f1.setHorizontalTextPosition(SwingConstants.LEFT);
			f2.setHorizontalTextPosition(SwingConstants.LEFT);
			f3.setHorizontalTextPosition(SwingConstants.LEFT);
			f4.setHorizontalTextPosition(SwingConstants.LEFT);

			changeQuantListener prodButtonList = new changeQuantListener(guiObject, p);
			f3.addActionListener(prodButtonList);

			canButtonListener canButtonList = new canButtonListener(guiObject, p);
			f4.addActionListener(canButtonList);



			JPanel prodPanel = new JPanel();
			prodPanel.add(f1);
			prodPanel.add(f2);
			prodPanel.add(f3);
			prodPanel.add(f4);
			cartPanel.add(prodPanel, outputConstraints);
			cartPanel.revalidate();
			counter++;
		}
		cartFrame.add(cartPanel);
		cartFrame.setSize(500, 300);
		cartFrame.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); 
		System.out.println(command);
		if( command.equals( "Go to Cart" ))  {
			//get products
			makeCart();

			cartFrame.add(cartPanel);
			cartFrame.setSize(500, 300);
			cartFrame.revalidate();
		}

		if (command.equals("Checkout Cart")){
			guiObject.project.checkout(guiObject.custID, guiObject.sessionID);
			List<String> inMsg = guiObject.project.getInvoiceForCustomer(guiObject.userName); 
			JFrame InvoiceFrame = new JFrame("Thank you for purchasing!");
			JPanel InvoicePanel = new JPanel(new GridBagLayout());
			InvoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			InvoiceFrame.getContentPane().add(InvoicePanel, BorderLayout.CENTER);
			JLabel sLab = new JLabel(inMsg.get(inMsg.size()-1));
			InvoicePanel.add(sLab);
			InvoiceFrame.setVisible(true);
			InvoiceFrame.setSize(500, 300);
			InvoiceFrame.revalidate();

		}


		if (command.equals("Get Invoices")){
			List<String> inMsg = guiObject.project.getInvoiceForCustomer(guiObject.userName); 
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





