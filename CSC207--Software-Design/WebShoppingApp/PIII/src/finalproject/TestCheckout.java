package finalproject;

import java.util.ArrayList;
import java.util.List;

import finalproject.ProjectV1.ShoppingCartItem;

public class TestCheckout {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		System.out.println("Vince Shopper Logged in; sessionID: " + mySession);
		
		int mySession3 = p.login("Conrad", "pass"); // expected result is a positive integer (session id)
		System.out.println(mySession3);
		
		p.addDistributionCenter("Narnia", mySession);
		System.out.println("Narnia DistributionCentre added by Admin");
		p.addDistributionCenter("Toronto", mySession);
		System.out.println("Toronto DistributionCentre Added by Admin");
		p.addDistributionCenter("Taipei", mySession);
		System.out.println("Taipei DistributionCentre Added by Admin");
		
		p.addRoute("Narnia", "Toronto", 5000, mySession);
		System.out.println("Narnia-Toronto route added by admin");
		p.addRoute("Toronto", "Montreal", 5, mySession);
		System.out.println("Toronto-Montreal Route Added by Admin");
		p.addRoute("Toronto", "Taipei", 50, mySession);
		
		System.out.println("Toronto-Taipei Route Added by Admin");
		p.addRoute("Narnia", "Toronto", 500, mySession);
		System.out.println("Narnia-Toronto Route Added by Admin");
		
		int customerID1 = p.addCustomer("Vincent Huor", "Montreal", "Bahen Street", mySession2);
		System.out.println("Customer Vincent Huor Added Under Shopper Vince; CustomerID: " + customerID1);
		
		int customerID2 = p.addCustomer("Conrad Chan", "Montreal", "Bahen Street", mySession3);
		System.out.println("Customer Conrad Chan Added Under Shopper Conrad; CustomerID: " + customerID2);
		
		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println("Categoty TSHIRT Added; CatID: " + catID);
		
		int catID2 = p.addCategory("TOY CARS",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println(catID2);
		
		int prodID0 = p.addProduct("Cardboard Box", catID2, 3.0 ,mySession, null);
		System.out.println("Added cardboard box by Admin; CatID: " + catID2 + "; ProductID: " +prodID0); // must be positive int
		int prodID1 = p.addProduct("Red Shirt", catID, 2.0 ,mySession, null);
		System.out.println("Red Shirt Added to TSHIRT Category by Admin; CatID: " + catID + "; ProductID: " +prodID1); // must be positive int
		
		int prodID2 = p.addProduct("Red Car", catID2, 5.0 ,mySession, null);
		System.out.println("added Red Car. productID: " +prodID2); // must be positive int
		
		boolean updateQty1 = p.updateQuantity(prodID1 ,"Toronto", 4, mySession); // admin session
		System.out.println("Red shirt Quantity Updated by Admin in Toronto to 4?: " + updateQty1);
		
		int avail = p.prodInquiry(prodID1,"Toronto");
		System.out.println("Red Shirt avail in Toronto after updateQuantity 4?: " + avail);
		
		boolean updateQty2 = p.updateQuantity(prodID1 ,"Taipei", 30, mySession); // admin session
		System.out.println("Red Shirt updated + 30?: " + updateQty2);
		
		
		boolean updateQty3 = p.updateQuantity(prodID0 ,"Narnia", 35, mySession); // admin session
		System.out.println("Cardboard updated + 35?: " + updateQty3);
		
		boolean updateQty4 = p.updateQuantity(prodID1 ,"Narnia", 35, mySession); // admin session
		System.out.println("Red Shirt updated + 35?: " + updateQty4);
		
		boolean updateQty5 = p.updateQuantity(prodID2 ,"Narnia", 15, mySession); // admin session
		System.out.println("Red Car updated + 15?: " + updateQty5);
		
		int avail0 = p.prodInquiry(prodID1,"Taipei");
		System.out.println("Red Car avail in Taipei after updateQuantity 30?: " + avail0);
		
		boolean item = p.addToShoppingCart(prodID1, 40, mySession2, customerID1);
		System.out.println("Added 45 Red Shirt; " + item);
		
		List<ShoppingCartItem> cart0 = p.getShoppingCart("Vince");
		System.out.println(cart0);
		
		boolean item01 = p.addToShoppingCart(prodID1, 5, mySession2, customerID1);
		System.out.println("Added 45 Red Shirt; " + item01);
		
		List<ShoppingCartItem> cart01 = p.getShoppingCart("Vince");
		System.out.println(cart01);
		
		boolean item2 = p.addToShoppingCart(prodID2, 15, mySession2, customerID1);
		System.out.println("Added 4 Red Car; " + item2);
		
		boolean item3 = p.addToShoppingCart(prodID0, 13, mySession2, customerID1);
		System.out.println("Added 13 Cardboard Box; " + item3);
		
		List<ShoppingCartItem> cart1 = p.getShoppingCart("Vince");
		System.out.println(cart1);
		
		/*boolean item5 = p.updateCartQuantity(prodID0, 3, mySession2, customerID1);
		System.out.println("Updated Carboard Box in cart:"+item5);
		
		List<ShoppingCartItem> cart3 = p.getShoppingCart("Vince");
		System.out.println(cart3);
		
		boolean item6 = p.updateCartQuantity(prodID0, 20, mySession2, customerID1);
		System.out.println("Updated Carboard Box in cart:"+item6);
		
		List<ShoppingCartItem> cart4 = p.getShoppingCart("Vince");
		System.out.println(cart4);
		
		boolean item4 = p.removeFromShoppingCart(prodID0, mySession2, customerID1);
		System.out.println("Removed Cardboard Box from cart:" + item4);
		
		List<ShoppingCartItem> cart2 = p.getShoppingCart("Vince");
		System.out.println(cart2);
		
		int avail12 = p.prodInquiry(prodID0, "Narnia");
		System.out.println(avail12);
		
		p.cancelCart("Vince");
		System.out.println(p.getShoppingCart("Vince"));*/
		
		System.out.println(p.prodInquiry(prodID1, "Taipei"));
		System.out.println(p.prodInquiry(prodID1, "Narnia"));
		System.out.println(p.prodInquiry(prodID1, "Toronto"));
		
		
		int invoice1 = p.checkout(customerID1, mySession2);
		double invoiceAmount1 = p.invoiceAmount(invoice1, mySession2);
		System.out.println("vince $149 + shipping: " + invoiceAmount1);
		
		int afteravail = p.prodInquiry(prodID1,"Toronto");
		System.out.println("Red Shirt avail in Toronto after updateQuantity 0?: " + afteravail);
		
		int afteravail0 = p.prodInquiry(prodID1,"Taipei");
		System.out.println("Red Car avail in Taipei after updateQuantity 24?: " + afteravail0);
		
		int afteravail2 = p.prodInquiry(prodID1,"Narnia");
		System.out.println("Red Shirt avail in Narnia after updateQuantity 0?: " + afteravail2);
		
		System.out.println(p.getSalesReport(mySession, catID));
		
		
		
		
	}

}
