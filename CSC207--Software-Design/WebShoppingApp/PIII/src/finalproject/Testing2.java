package finalproject;

import java.util.ArrayList;

public class Testing2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ProjectV1 p = new ProjectV1();

		boolean result = p.addUser("Admin","pass",true); //add admin 
		System.out.println("tried addeding Ilir as Admin" + result);
		
		int mySession = p.login("Admin","pass"); // expected result is a positive integer (session id)
		System.out.println("Ilir Admin Logged in; sessionID: " + mySession);
		
		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println("Categoty TSHIRT Added; CatID: " + catID);
		
		int catID2 = p.addCategory("CARS",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println(catID2);
		
		int prodID1 = p.addProduct("Red Shirt", catID, 2.0 ,mySession, "");
		System.out.println("Red Shirt Added to TSHIRT Category by Admin; CatID: " + catID + "; ProductID: " + prodID1); // must be positive int
		int prodID3 = p.addProduct("Blue Pants", catID, 50.0 ,mySession, "");
		System.out.println("Red Pants Added to TSHIRT Category by Admin; CatID: " + catID + "; ProductID: " + prodID3);
		
		int prodID2 = p.addProduct("Silver Car", catID2, 2.0 ,mySession, "");
		System.out.println("Silver Car Added to car Category by Admin; CatID: " + catID + "; ProductID: " + prodID1); // must be positive int
		int prodID4 = p.addProduct("Gold Car", catID2, 50.0 ,mySession, "");
		System.out.println("Gold Car Added to car Category by Admin; CatID: " + catID + "; ProductID: " + prodID3);
		
		p.addRoute("Toronto", "Ottawa", 10, mySession);
		System.out.println("Toronto-Ottawa Route Added by Admin");
		p.addRoute("Montreal", "Ottawa", 10, mySession);
		System.out.println("Montreal-Ottawa Route Added by Admin");
		p.addRoute("Toronto", "Montreal", 50, mySession);
		System.out.println("Toronto-Montreal Route Added by Admin");
		p.addRoute("Toronto", "Taipei", 500, mySession);
		System.out.println("Toronto-Taipei Route Added by Admin");
		p.addRoute("Ottawa", "Taipei", 1000, mySession);
		System.out.println("Ottawa-Taipei Route Added by Admin");
		
		p.addDistributionCenter("Ottawa", mySession);
		System.out.println("Ottawa DistributionCentre Added by Admin");
		p.addDistributionCenter("Montreal", mySession);
		System.out.println("Montreal DistributionCentre Added by Admin");
		p.addDistributionCenter("Taipei", mySession);
		System.out.println("Taipei DistributionCentre Added by Admin");

		boolean updateQty1 = p.updateQuantity(prodID1 ,"Ottawa", 40, mySession); // admin session
		System.out.println("Red shirt Quantity Updated by Admin in Ottawa to 40?: " + updateQty1);
		boolean updateQty2 = p.updateQuantity(prodID1 ,"Montreal", 10, mySession); // admin session
		System.out.println("Red shirt Quantity Updated by Admin in Montreal to 10?: " + updateQty2);
		boolean updateQty3 = p.updateQuantity(prodID3 ,"Ottawa", 5, mySession); // admin session
		System.out.println("Blue shirt Quantity Updated by Admin in Ottawa to 5?: " + updateQty3);
		boolean updateQty4 = p.updateQuantity(prodID3 ,"Montreal", 40, mySession); // admin session
		System.out.println("Blue shirt Quantity Updated by Admin in Montreal to 40?: " + updateQty4);
		boolean updateQty5 = p.updateQuantity(prodID3 ,"Taipei", 5, mySession); // admin session
		System.out.println("Blue shirt Quantity Updated by Admin in Taipei to 1?: " + updateQty5);
		boolean updateQty6 = p.updateQuantity(prodID2 ,"Taipei", 50, mySession); // admin session
		System.out.println("Silver Car Quantity Updated by Admin in Toronto to 50?: " + updateQty6);
		boolean updateQty7 = p.updateQuantity(prodID4 ,"Montreal", 1, mySession); // admin session
		System.out.println("Gold car Quantity Updated by Admin in Toronto to 1?: " + updateQty7);
		
		
		boolean result1 = p.addUser("Vince","pass",false); //add Shopper
		System.out.println("tried addeding Vincent as Shopper" +result1);
		
		boolean result2 = p.addUser("Conrad","pass",false); //add Shopper
		System.out.println("tried addeding Conrad as Shopper" +result2);
		
		p.logout(mySession); // admin logs out
		System.out.println("admin logs out");
		
		int mySession2 = p.login("Vince","pass"); // expected result is a positive integer (session id)
		System.out.println(mySession2);
		System.out.println("Vince Shopper Logged in; sessionID: " + mySession2);
		
		int customerID1 = p.addCustomer("Vincent Huor", "Toronto", "Bahen Street", mySession2);
		System.out.println("Customer Vincent Huor Added Under Shopper Vince; CustomerID: " + customerID1);
		
		boolean item = p.addToShoppingCart(prodID3, 46, mySession2, customerID1);
		System.out.println("Added 1 Red Shirt vince; " + item);
		boolean item2 = p.addToShoppingCart(prodID4, 1, mySession2, customerID1);
		System.out.println("Added 1 Gold Car vince; " + item2);
		
		p.logout(mySession2);
		System.out.println("vince logs out");
		
		int mySession3 = p.login("Conrad", "pass");
		System.out.println("conrad logs in");
		
		int customerID2 = p.addCustomer("Conrad Chan", "Toronto", "Bahen Street", mySession3);
		System.out.println("Customer Conrad Chan Added Under Shopper Conrad; CustomerID: " + customerID2);
		
		
		boolean item1 = p.addToShoppingCart(prodID1, 10, mySession3, customerID2);
		System.out.println("Added 1 Red Shirt conrad; " + item1);
		boolean item4 = p.addToShoppingCart(prodID3, 4, mySession3, customerID2);
		System.out.println("Added 1 Blue pants conrad; " + item4);
		boolean item3 = p.addToShoppingCart(prodID4, 1, mySession3, customerID2);
		System.out.println("Added 1 Gold car conrad; " + item3);
		
		int inv = p.checkout(customerID2, mySession3);
		System.out.println("conrad checkout id: " + inv);
		double invoiceAmount2 = p.invoiceAmount(inv, mySession3);
		System.out.println("conrad $?? + shipping: " + invoiceAmount2);
		
		p.logout(mySession3);
		System.out.println("conrad logs out");
		
		int mySession4 = p.login("Vince", "pass");
		System.out.println("vince logs in" + mySession4);
		System.out.println(p.availQ(prodID3));
		System.out.println("Vince's Shopping Cart: "+p.getShoppingCart("Vince"));
		
		int inv2 = p.checkout(customerID1, mySession4);
		System.out.println("vince checkout id: " + inv2);
		double invoiceAmount3 = p.invoiceAmount(inv2, mySession4);
		System.out.println("vince $?? + shipping: " + invoiceAmount3);
		
		p.logout(mySession4);
		System.out.println("vince logs out");
		
		int mySession5 = p.login("Admin" , "pass");
		ArrayList<String> sales = (ArrayList<String>) p.getSalesReport(mySession5, catID);
		System.out.println(sales);
	
}}
