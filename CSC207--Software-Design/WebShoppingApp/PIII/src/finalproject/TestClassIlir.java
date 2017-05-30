package finalproject;

public class TestClassIlir {

	public static void main(String[] args) {
		Project p = new Project();
		boolean result = p.addUser("Ilir","pass",true);// the result must be true
		System.out.println(result);
		boolean result2 = p.addUser("Ilir","pass",true);// the result must be false; user "Ilir" exists already
		System.out.println(!result2);
		int mySession = p.login("Ilir","pass"); // expected result is a positive integer (session id)
		System.out.println(mySession);
		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
		System.out.println(catID);
		int someID = p.addCategory("TSHIRT", mySession); // expected result is -1; "TSHIRT" exists
		System.out.println(someID==-1);
		boolean result3 = p.addUser("Johnny","pass",false);// expected result true
		System.out.println(result3);
		int johnnySession = p.login("Johnny","pass");//expect a session ID, positive integer, not equal to mySesssion
		System.out.println(johnnySession);
		int someID2 = p.addCategory("TSHIRT",johnnySession); // expect -1 because Johnny is not administrator
		System.out.println(someID2 == -1);
		p.logout(johnnySession);// now user Johhny cannot do anything else because he logged out. All Johhny data must be saved in the files.
		p.logout(mySession);// user Ilir is also out
		System.out.println(p.addCategory("TSHIRT",mySession) == -1); //must fail (return -1) because user Ilir logged out

	}

}
