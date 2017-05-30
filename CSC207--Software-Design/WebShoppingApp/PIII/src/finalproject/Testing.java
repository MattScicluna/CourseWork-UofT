package finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import finalproject.ProjectV1.ShoppingCartItem;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		Category TShirts = new Category("T-Shirts");
		//Product t1 = new Product("t1", "desc", null, 1.00, TShirts); //build a Product constructor!
		//Product t2 = new Product("t2", "desc", null, 2.00, TShirts);
		//Product a1 = new Product("a1", "desc", null, 3.00, TShirts);
//		System.out.println("First by name");
//		System.out.println(TShirts.display_alph());
//		System.out.println("Now by price");
//		System.out.println(TShirts.display_price());
//		
//		Admin a = new Admin("h", "h", true);
//		Shopper b = new Customer("f", "h", false);
//		Shopper c = new Customer("e", "h", false);
//		Admin d = new Admin("w", "h", true);
//		System.out.println(a.sessionID);
//		System.out.println(b.sessionID);
//		System.out.println(c.sessionID);
//		System.out.println(d.sessionID);
		
		ProjectV1 p = new ProjectV1();

		boolean result = p.addUser("Ilir","pass",true); //add admin 
		System.out.println("tried addeding Ilir as Admin" + result);
		
		boolean result1 = p.addUser("Vince","pass",false); //add Shopper
		System.out.println("tried addeding Vincent as Shopper" +result1);
		
		boolean result2 = p.addUser("Conrad","pass",false); //add Shopper
		System.out.println("tried addeding Conrad as Shopper" +result2);
//		
		int mySession = p.login("Ilir","pass"); // expected result is a positive integer (session id)
		System.out.println("Ilir Admin Logged in; sessionID: " + mySession);
		
		int mySession2 = p.login("Vince","pass"); // expected result is a positive integer (session id)
		System.out.println(mySession2);
		System.out.println("Vince Shopper Logged in; sessionID: " + mySession2);
		
		int mySession3 = p.login("Conrad", "pass"); // expected result is a positive integer (session id)
		System.out.println(mySession3);
		
		int customerID1 = p.addCustomer("Vincent Huor", "Montreal", "Bahen Street", mySession2);
		System.out.println("Customer Vincent Huor Added Under Shopper Vince; CustomerID: " + customerID1);
		
		int customerID2 = p.addCustomer("Conrad Chan", "Montreal", "Bahen Street", mySession3);
		System.out.println("Customer Conrad Chan Added Under Shopper Conrad; CustomerID: " + customerID2);
		
		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println("Categoty TSHIRT Added; CatID: " + catID);
		
		int catID2 = p.addCategory("TOY CARS",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println(catID2);
//		
//		int catID3 = p.addCategory("CC",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
//		System.out.println(catID3);
		
		p.addDistributionCenter("Toronto", mySession);
		System.out.println("Toronto DistributionCentre Added by Admin");
		//p.addDistributionCenter("Montreal", mySession);
		System.out.println("Montreal DistributionCentre Added by Admin");
		p.addDistributionCenter("Taipei", mySession);
		System.out.println("Taipei DistributionCentre Added by Admin");
		
		p.addRoute("Toronto", "Montreal", 5, mySession);
		System.out.println("Toronto-Montreal Route Added by Admin");
		p.addRoute("Toronto", "Taipei", 50, mySession);
		System.out.println("Toronto-Taipei Route Added by Admin");
		
//		int customerID2 = p.addCustomer("Conrad Chan", "Toronto", "Bahen Street", mySession3);
//		System.out.println("added customer. this cutomerID is: " + customerID2);
		
		int prodID1 = p.addProduct("Red Shirt", catID, 2.0 ,mySession, "");
		System.out.println("Red Shirt Added to TSHIRT Category by Admin; CatID: " + catID + "; ProductID: " + prodID1); // must be positive int
		int prodID3 = p.addProduct("Red Pants", catID, 50.0 ,mySession, "");
		System.out.println("Red Pants Added to TSHIRT Category by Admin; CatID: " + catID + "; ProductID: " + prodID3);
		
		int prodID2 = p.addProduct("Red Car", catID2, 5.0 ,mySession, "");
		System.out.println("added Red Car. productID: " + prodID2); // must be positive int
		
		
		boolean updateQty1 = p.updateQuantity(prodID1 ,"Toronto", 5, mySession); // admin session
		System.out.println("Red shirt Quantity Updated by Admin in Toronto to 1?: " + updateQty1);
		
		int avail = p.prodInquiry(prodID1,"Toronto");
		System.out.println("Red Shirt avail in Toronto after updateQuantity 1?: " + avail);
		
		boolean updateQty2 = p.updateQuantity(prodID1 ,"Taipei", 5, mySession); // admin session
		System.out.println("Red shirt updated + 1?: " + updateQty2);
		
		int avail0 = p.prodInquiry(prodID1,"Taipei");
		System.out.println("Red shirt avail in Taipei after updateQuantity 1?: " + avail0);
		
		boolean updateQty4 = p.updateQuantity(prodID2 ,"Taipei", 20, mySession); // shopper session: fail
		System.out.println("Red car Quantity Updated by Admin in Taipei to 20?: " + updateQty4);
		
		int avail1 = p.prodInquiry(prodID2,"Taipei");
		System.out.println("Red car avail in Taipei after updateQuantity 20?: " + avail1);
		
//		PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown
//		PRICEUp 2, then 50
//		PRICEDown 50, then 2
//		ALPHAUp p, s
//		ALPHADown s, p
		
		System.out.println("*******by TSHIRT********");
		List<Product> list = p.browseShop(0, 0, "ALPHADown"); //good
		System.out.println(list);
		System.out.println("*********by TOY CARS**********");
		List<Product> list1 = p.browseShop(100, 200, "TOY CARS");
		System.out.println(list1);
		
//		
//		int prodID3 = p.addProduct("Blue Shirt", catID, 3.0 ,mySession);
//		System.out.println("added Blue Shirt. productID: " +prodID3); // must be positive int
//		
//		int prodID4 = p.addProduct("Blue Car", catID2, 4.0 ,mySession);
//		System.out.println("added Blue Car. productID: " + prodID4); // must be positive int
//		
//		int prodID5 = p.addProduct("Yellow Car", catID2, 6.0 ,mySession);
//		System.out.println("added Yellow Car. productID: " + prodID5); // must be positive int
	
		
//		boolean updateQty3 = p.updateQuantity(prodID3 ,"Montreal", 20, mySession); // shopper session: fail
//		System.out.println("Blue shirt updated + 20?: " + updateQty3);
		
//		boolean updateQty5 = p.updateQuantity(prodID5 ,"Taipei", 40, mySession); // shopper session: fail
//		System.out.println("Yellow Car updated + 40?: " + updateQty5);
		
//		int avail2 = p.prodInquiry(prodID2,"Toronto");
//		System.out.println("Red Car avail 30?: " + avail2);
		
//		int avail3 = p.prodInquiry(prodID3,"Montreal");
//		System.out.println("Blue Shirt avail 20?: " + avail3);
		
//		int avail4 = p.prodInquiry(prodID1,"Taipei");
//		System.out.println("Blue Car avail distributionCentre added by user 30? Taipei: " + avail4);
		
//		int avail5 = p.prodInquiry(prodID5,"Taipei");
//		System.out.println("Yellow Car avail distributionCentre added by user -1?: " + avail5);
		
//		p.addRoute("Toronto", "Montreal", 5, mySession);
//		p.addRoute("Toronto", "Taipei", 50, mySession);
		
		boolean item = p.addToShoppingCart(prodID1, 3, mySession2, customerID1);
		System.out.println("Added 1 Red Shirt vince; " + item);
		
		boolean item2 = p.addToShoppingCart(prodID2, 1, mySession2, customerID1);
		System.out.println("Added 3 Red Car vince; " + item2);
		
		boolean item3 = p.addToShoppingCart(prodID1, 7, mySession3, customerID2);
		System.out.println("Added 1 Red Shirt conrad; " + item3);
		
		boolean item4 = p.addToShoppingCart(prodID2, 1, mySession3, customerID2);
		System.out.println("Added 2 Red Car conrad; " + item4);
		
//		int avail3 = p.prodInquiry(prodID1,"Toronto");
//		System.out.println("Red Shirt avail Toronto 1?: " + avail3); //should be 1. the distCentre still hold the item, but availQnty seen by user is 0
//		int avail4 = p.prodInquiry(prodID1,"Taipei");
//		System.out.println("Red Shirt avail Taipei 1?: " + avail4); //should be 1. the distCentre still hold the item, but availQnty seen by user is 0
		
		
//		List<ShoppingCartItem> cart1 = p.getShoppingCart("Vince");
//		System.out.println(cart1);
//		System.out.println(cart1.get(0).getCenter());
//		System.out.println(cart1.get(0).getQuantity());
//		System.out.println(cart1.get(1).getCenter());
//		System.out.println(cart1.get(1).getQuantity());
//		System.out.println("***************");
		
//		List<ShoppingCartItem> cart2 = p.getShoppingCart("Conrad");
//		System.out.println(cart2);
//		System.out.println(cart2.get(0).getCenter());
//		System.out.println(cart2.get(0).getQuantity());
//		System.out.println(cart2.get(1).getCenter());
//		System.out.println(cart2.get(1).getQuantity());
		
		int num = p.availQ(prodID1);
		System.out.println("AVAILABLE QUANTITY prodID1: " + num);
		
		p.logout(mySession2);
		System.out.println("vince logs out");
		
		boolean item5 = p.addToShoppingCart(prodID1, 3, mySession3, customerID2);
		System.out.println("Added 3 Red Shirt to conrad after vince logouts; " + item5);
		
		int invoice2 = p.checkout(customerID2, mySession3);
		System.out.println("conrad checkout id: " + invoice2);
		double invoiceAmount2 = p.invoiceAmount(invoice2, mySession3);
//		System.out.println("vince $17 + shipping: " + invoiceAmount1);
		System.out.println("conrad $?? + shipping: " + invoiceAmount2);
		
		int sessionID4 = p.login("Vince", "pass");
		System.out.println("vince logs in, sessionID: " + sessionID4);
		
		System.out.println("vince's cart: ");
		List<ShoppingCartItem> cart3 = p.getShoppingCart("Vince"); 
		System.out.println(cart3);
		System.out.println(cart3.get(0).getCenter());
		System.out.println(cart3.get(0).getQuantity());
//		System.out.println(cart3.get(1).getCenter());
//		System.out.println(cart3.get(1).getQuantity());
		
		int invoice1 = p.checkout(customerID1, mySession2);
		System.out.println("vinc checkout id: " + invoice1);
		double invoiceAmount1 = p.invoiceAmount(invoice1, mySession2);
//		System.out.println("vince $17 + shipping: " + invoiceAmount1);
		System.out.println("vince $15 + shipping: " + invoiceAmount1);
	
		
		int avail2 = p.prodInquiry(prodID1,"Toronto");
		System.out.println("Red Car avail 0?: " + avail2);
		
		int avail3 = p.prodInquiry(prodID1,"Taipei");
		System.out.println("Red Car avail 0?: " + avail3);
		
//		int invoice2 = p.checkout(customerID2, mySession3);
//		double invoiceAmount2 = p.invoiceAmount(invoice2, mySession3);
//		System.out.println("conrad $12 + shipping; " + invoiceAmount2);

//		int invoiceID1 = p.placeOrder(customerID1, prodID1, 1, mySession2);
//		System.out.println("Vincent bought Red Shirt. Quantity 1; InvoiceID: " + invoiceID1);
//		
//		double invoicePrice1 = p.invoiceAmount(invoiceID1, mySession2);
//		System.out.println("Invoice Total price, Vincent bought 1x2.0?: " + invoicePrice1);
//		
//		int afterPurchase = p.prodInquiry(prodID1, "Taipei");
//		System.out.println("Taipei Qty left of Red Shirt 30?: " + afterPurchase);
//		
//		int afterPurchase1 = p.prodInquiry(prodID1, "Toronto");
//		System.out.println("Toronto Qty left of Red Shirt 19?: " + afterPurchase1);
		
//		p.logout(mySession3);
//		
//		int invoiceID2 = p.placeOrder(customerID2, prodID2, 2, mySession3);
//		System.out.println("Conrad bought Red Car X1 || InvoiceID: " + invoiceID2);
//		double invoicePrice2 = p.invoiceAmount(invoiceID2, mySession3);
//		System.out.println("Invoice Total price, Conrad bought 2x5.0?:: " + invoicePrice2);
//		int afterPurchase2 = p.prodInquiry(prodID2,"Toronto");
//		System.out.println("Toronto Qty left of Red Car 28?: " + afterPurchase2);
//		
//		int invoiceID3 = p.placeOrder(customerID2, prodID2, 2, mySession); // fail bc admin cannot place orders
//		System.out.println("Admin bought Red Car X1 || InvoiceID: " + invoiceID3);
//		double invoicePrice3 = p.invoiceAmount(invoiceID1, mySession);
//		System.out.println("Invoice Total price: " + invoicePrice3);
//		int afterPurchase3 = p.prodInquiry(prodID2,"Toronto");
//		System.out.println("Toronto Qty left of Red Car 28?, No purchase made: " + afterPurchase3);
		
		
//		ArrayList<String> path = p.getDeliveryRoute(invoiceID1, mySession2);
//		System.out.println(path);
}}
