import java.io.PrintStream;

public abstract class ConsoleUI {

	public static void printLoginScreen(PrintStream out) {
		out.println("----- Welcome to the BBI e-shop! -----");
		out.println("Log in or create a new account.");
		out.println("1. Log in with an existing account.");
		out.println("2. Create a new account.");
		out.println("3. Exit.");
		out.println("Choose an option(\"1\", \"2\" or \"3\"): ");
	}

	public static void printHomeScreen(Account clientAcc, PrintStream out) {
		if(clientAcc instanceof UserAcc) {
			out.println();
			out.println("Logged in as " + clientAcc.getUsername() + ".");
			out.println();
			out.println("1. Browse all products.");
			out.println("2. Browse products by category.");
			out.println("3. Browse only BlackFriday products.");
			out.println("4. Browse BlackFriday products by category.");
			out.println();
			out.println("5. View purchase history.");
			out.println("6. Edit account.");
			out.println("7. Log out.");
		} else if(clientAcc instanceof AdminAcc) {
			out.println();
			out.println("Logged in as " + clientAcc.getUsername() + ".");
			out.println();
			out.println("1. Browse all products to edit.");
			out.println("2. Browse products by category to edit.");
			out.println("3. Browse only BlackFriday products to edit.");
			out.println("4. Browse BlackFriday products by category to edit.");
			out.println();
			out.println("5. Create a new product.");
			out.println("6. View revenue.");
			out.println();
			out.println("7. Create an administrator account.");
			out.println("8. Edit account.");
			out.println("9. Log out.");
		}
	}

	public static void printProducts(PrintStream out, ProductCategory cat, boolean onlyBlackFriday) {
		int option = 1;
		if (onlyBlackFriday == false) {
			if (cat == null) {
				for (Product p : Product.AllProducts) {
					out.println(option++ + ". " + p.toString());
					out.println();
				}
			} else {
				for (Product p : Product.AllProducts) {
					if (p.categories.contains(cat)) {
						out.println(option++ + ". " + p.toString());
						out.println();
					}
				}
			} 
		} else {
			if(Product.BlackFridayProducts.isEmpty()) {
				out.println("There are currently no products in the BlackFriday campaign.");
				
			}else if (cat == null) {
				for (Product p : Product.BlackFridayProducts) {
					out.println(option++ + ". " + p.toString());
					out.println();
				}
			} else {
				for (Product p : Product.BlackFridayProducts) {
					if (p.categories.contains(cat)) {
						out.println(option++ + ". " + p.toString());
						out.println();
					}
				}
			} 
		}
	}

	public static void printCategories(PrintStream out) {
		int option = 1;
		out.println("Choose category: ");
		for(ProductCategory cat : ProductCategory.values()) {
			out.println(option++ + ". " + cat.toString());
		}	
	}
}
