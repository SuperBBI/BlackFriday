import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminAcc extends Account implements Administrator {
	private String Id;

	public AdminAcc(String username, String password, String Id) throws InvalidUsernameException, InvalidPasswordException {
		super(username, password);
		this.Id = Id;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}	

	@Override
	public void createProduct(PrintStream out, Scanner scanCL) {
		out.println("Minimum required properties for a product are:\n"
				+ "manufacturer, model, price, minPrice, quantity!");
		out.println("The product has to be categorised in atleast one category!");
		out.println();
		out.println("To add a property, type: 'add <property> <value>'");
		out.println("To add a category, type: 'add <category>'");
		out.println("To finish creating the product, type: 'CREATE'");
		out.println("To cancel, type: 'cancel'");

		Map<String, String> properties = new HashMap<String, String>();
		ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();

		String line;
		String[] tokens;

		do {
			line = scanCL.nextLine();
			tokens = line.split(" ");

			if(line.contentEquals("CREATE")) {
				if (properties.containsKey("manufacturer") && properties.containsKey("model") && properties.containsKey("quantity") &&
						properties.containsKey("price") && properties.containsKey("minPrice") && (categories.size() > 0)) {
					Product p = new Product(properties, categories);
					p.setMinPrice(Double.parseDouble(p.properties.get("minPrice")));
					Product.AllProducts.add(p);
					break;
				} else {
					out.println("Cannot create the product!\n The product is missing one or more of the minimum requirements!");
					out.println("Minimum required properties for a product are:\n"
							+ "manufacturer, model, price, minPrice, quantity!");
					out.println("The product has to be categorised in atleast one category!");
				}
			} else if(line.contentEquals("cancel")) {
				out.println("Product creation cancelled.");
				break;
			} else if(tokens[0].contentEquals("add")) {
				if(tokens.length==2) {
					if (tokens[1].contentEquals("Electronics")) {
						categories.add(ProductCategory.Electronics);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Book")) {
						categories.add(ProductCategory.Book);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Home")) {
						categories.add(ProductCategory.Home);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Bicycles")) {
						categories.add(ProductCategory.Bicycles);
						out.println("Successfully added category!");
					} else {
						out.println("Invalid category!");
					}
				} else if(tokens.length > 2){
					StringBuilder wholeValue = new StringBuilder();

					for(String s : tokens) {
						if(!s.contentEquals(tokens[0]) && !s.contentEquals(tokens[1])) {
							wholeValue.append(s + " ");
						}
					}

					wholeValue.deleteCharAt(wholeValue.lastIndexOf(" "));
					
					try {
						if(tokens[1].contentEquals("price")) {
							@SuppressWarnings("unused")
							Double d = Double.parseDouble(wholeValue.toString());

						} else if(tokens[1].contentEquals("minPrice")) {
							@SuppressWarnings("unused")
							Double d = Double.parseDouble(wholeValue.toString());

						} else if(tokens[1].contentEquals("promoPrice")) {
							@SuppressWarnings("unused")
							Double d = Double.parseDouble(wholeValue.toString());

						} else {
							properties.put(tokens[1], wholeValue.toString());
						}
						out.println("Successfully added property!");

					} catch (NumberFormatException e) {
						out.println("Invalid input for a price: " + wholeValue.toString() + "!");
					}
				}
			} else {
				out.println("Invalid Input!");
			}
		} while (!line.equalsIgnoreCase("cancel"));

	}

	@Override
	public void editProduct(Product p, PrintStream out, Scanner scanCL) throws ArrayIndexOutOfBoundsException {
		out.println("Type 'done' to finish editing.");
		out.println("To make a discount type: 'discount <value>' (value is in percent).");
		out.println("To add the product to the BlackFriday campaign type: 'add bf <discount>' (discount value is in percent).");
		out.println("To remove the product from the BlackFriday campaign type: 'remove bf'.");
		out.println();
		out.println("Type 'add <property> <value>' to add or edit a property.");
		out.println("Type 'remove <property> <value>' to remove a property.");
		out.println("Type 'add <category>' to add a category.");
		out.println("Type 'remove <category>' to remove a category.");
		out.println("Type 'REMOVE' to remove the product.");
		out.println();
		out.println(p.toString());
		
		out.print("Categories: ");
		for(ProductCategory cat : p.categories) {
			out.print(cat.toString() + " ");
		}
		
		out.print("\n");

		String line;
		String[] tokens;

		do {
			line = scanCL.nextLine();
			tokens = line.split(" ");

			if(tokens[0].contentEquals("discount")) {
				if (tokens.length == 2) {
					try {
						p.setPromoPrice(Double.parseDouble(tokens[1]));
					} catch (NumberFormatException e) {
						out.println("Bad Input!");
					} catch (PromoPriceException e) {
						out.println(e.getMessage());
					}
				} else {
					out.println("Invalid discount input!");
				}
				
			} else if(tokens[0].contentEquals("add")) {
				if(tokens.length == 2) {
					if (tokens[1].contentEquals("Electronics")) {
						p.addCategory(ProductCategory.Electronics);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Book")) {
						p.addCategory(ProductCategory.Book);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Home")) {
						p.addCategory(ProductCategory.Home);
						out.println("Successfully added category!");
					} else if (tokens[1].contentEquals("Bicycles")) {
						p.addCategory(ProductCategory.Bicycles);
						out.println("Successfully added category!");
					} else {
						out.println("Invalid category!");
					}
				} else if (tokens[1].equalsIgnoreCase("bf")){
					if (tokens.length == 3) {
						try {
							p.setPromoPrice(Double.parseDouble(tokens[2]));
							Product.BlackFridayProducts.add(p);
							out.println("Successfully added product to the BlackFriday campaign with the specified discount.");
						
						} catch (NumberFormatException e) {
							out.println(e.getMessage());
						} catch (PromoPriceException e) {
							out.println(e.getMessage());
						} 
					} else {
						out.println("Invalid discount value!");
					}
					
				} else {
					StringBuilder wholeValue = new StringBuilder();

					for(String s : tokens) {
						if(!s.contentEquals(tokens[0]) && !s.contentEquals(tokens[1])) {
							wholeValue.append(s + " ");
						}
					}

					wholeValue.deleteCharAt(wholeValue.lastIndexOf(" "));
					try {
						if(tokens[1].contentEquals("price")) {
							p.setPrice(Double.parseDouble(wholeValue.toString()));

						} else if(tokens[1].contentEquals("minPrice")) {
							p.setMinPrice(Double.parseDouble(wholeValue.toString()));

						} else if(tokens[1].contentEquals("promoPrice")) {
							p.setPromoPrice(Double.parseDouble(wholeValue.toString()));

						} else {
							p.addProperty(tokens[1], wholeValue.toString());
						}
						out.println("Successfully added property!");

					} catch (NumberFormatException e) {
						out.println(e.getMessage());
					} catch (PromoPriceException e) {
						out.println(e.getMessage());
					}
				}
			} else if(tokens[0].contentEquals("remove")) {
				if(tokens.length == 2) {
					if (tokens[1].contentEquals("bf")) {
						boolean removed = false;
						for(Product p1 : Product.BlackFridayProducts) {
							if(p.equals(p1)) {
								Product.BlackFridayProducts.remove(p);
								Double d = 0.0;
								p.properties.put("promoPrice", d.toString());
								out.println("Successfully removed product from the BlackFriday campaign.");
								removed = true;
								break;
							}
						}
						if(removed == false) {
							out.println("Didn't remove product.");
						}
						
					} else {
						if (tokens[1].contentEquals("Electronics")) {
							p.removeCategory(ProductCategory.Electronics);
							out.println("Successfully removed category!");
						} else if (tokens[1].contentEquals("Book")) {
							p.removeCategory(ProductCategory.Book);
							out.println("Successfully removed category!");
						} else if (tokens[1].contentEquals("Home")) {
							p.removeCategory(ProductCategory.Home);
							out.println("Successfully removed category!");
						} else if (tokens[1].contentEquals("Bicycles")) {
							p.removeCategory(ProductCategory.Bicycles);
							out.println("Successfully removed category!");
						} else {
							out.println("Invalid category!");
						} 
					}
				} else {
					StringBuilder wholeValue = new StringBuilder();

					for(String s : tokens) {
						if(!s.contentEquals(tokens[0]) && !s.contentEquals(tokens[1])) {
							wholeValue.append(s + " ");
						}
					}

					wholeValue.deleteCharAt(wholeValue.lastIndexOf(" "));

					if (p.properties.containsKey(tokens[1]) && p.properties.containsValue(wholeValue.toString())) {
						p.removeProperty(tokens[1], wholeValue.toString());
						out.println("Successfully removed property!");
					} else {
						out.println("Invalid property or value!");
					}
				}
			} else if(tokens[0].contentEquals("REMOVE")) {
				Product.AllProducts.remove(p);
				out.println("Product removed!");
				break;
			} else if(tokens[0].contentEquals("done")){
				break;
			} else {
				out.println("Invalid Input!");
			}
		} while (!line.equalsIgnoreCase("done"));
	}


	@Override
	public String toString() {
		return "Username: " + super.getUsername() + "\n" +
				"Password: " + super.getPassword() + "\n" + 
				"Id: " + getId();
	}

}
