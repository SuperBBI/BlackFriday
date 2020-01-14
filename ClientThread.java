import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ClientThread extends Thread {
	private final Socket clientSocket;
	private final PrintStream output;
	private final Scanner scanCL;

	private Account clientAcc;

	public ClientThread(Socket acceptedSocket) throws IOException {
		clientSocket = acceptedSocket;

		output = new PrintStream(clientSocket.getOutputStream());
		scanCL = new Scanner(clientSocket.getInputStream());
	}

	@Override
	public void run() {
		try {
			handleClientSocket();
		} catch (NoSuchElementException e) {
			Account.OnlineAccounts.remove(clientAcc);
			System.out.println("[" + new Date() + "] " + clientAcc.getUsername() + " logged out.");
			clientAcc = null;
			try {
				clientSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void handleClientSocket() throws NoSuchElementException { 
		while(clientSocket.isConnected() && !clientSocket.isClosed()) {
			boolean login = false;
			while(login != true) login = handleLoginScreen();

			if (clientSocket.isClosed()!=true) {
				boolean homeMenu = false;
				while (homeMenu != true) homeMenu = handleHomeScreen(); // includes: handleBuy(), UserAcc.buyProduct(), handleEdit(), AdminAcc.editProduct(), AdminAcc.createProduct()
			}
		}
	}

	private boolean handleHomeScreen() { 
		if (clientAcc instanceof UserAcc) { ////////////////////////-----    USER SIDE    ------
			ConsoleUI.printHomeScreen(clientAcc, output);

			String line = scanCL.nextLine();

			if(line.contentEquals("1")) {
				ConsoleUI.printProducts(output, null, false);
				handleBuy(line, false);

			} else if(line.contentEquals("2")) {
				ConsoleUI.printCategories(output);

				boolean properCat = false;
				ProductCategory cat = null;
				do {
					try {
						line = scanCL.nextLine();
						cat = ProductCategory.values()[Integer.parseInt(line)];
						properCat = true;
					} catch (NumberFormatException e) {
						output.println("Bad Input!");
					} catch (ArrayIndexOutOfBoundsException e) {
						output.println("No such category!");
					} catch (Exception e) {
						output.println("An Exception occurred! Try again.");
					}
				} while (properCat != true);

				ConsoleUI.printProducts(output, cat, false);
				handleBuy(line, false);

			} else if(line.contentEquals("3")) {
				ConsoleUI.printProducts(output, null, true);
				handleBuy(line, true);

			} else if(line.contentEquals("4")) {
				ConsoleUI.printCategories(output);

				boolean properCat = false;
				ProductCategory cat = null;
				do {
					try {
						line = scanCL.nextLine();
						cat = ProductCategory.values()[Integer.parseInt(line)];
						properCat = true;
					} catch (NumberFormatException e) {
						output.println("Bad Input!");
					} catch (ArrayIndexOutOfBoundsException e) {
						output.println("No such category!");
					} catch (Exception e) {
						output.println("An Exception occurred! Try again.");
					}
				} while (properCat != true);

				ConsoleUI.printProducts(output, cat, true);
				handleBuy(line, true);

			} else if(line.contentEquals("5")) {
				output.println(((UserAcc) clientAcc).getStringPurchaseHistory());
			} else if(line.contentEquals("6")) {
				output.println("Type 'done' to finish editing your account.");
				output.println("Type 'set <field> <value>' to edit your account.");
				output.println(clientAcc.toString());
				
				String[] tokens;
				
				do {
					line = scanCL.nextLine();
					tokens = line.split(" ");
					
					if(tokens[0].contentEquals("set")) {
						if(tokens.length >= 3) {
							StringBuilder value = new StringBuilder();
							
							if(tokens[1].equalsIgnoreCase("username")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								try {
									clientAcc.setUsername(value.toString());
									output.println("Changed field successfully! Change will take effect after return to home screen!");
								} catch (InvalidUsernameException e) {
									output.println(e.getMessage());
								}
							} else if(tokens[1].equalsIgnoreCase("password")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								try {
									clientAcc.setPassword(value.toString());
									output.println("Changed field successfully! Change will take effect after return to home screen!");
								} catch (InvalidPasswordException e) {
									output.println(e.getMessage());
								}
							} else if(tokens[1].equalsIgnoreCase("address")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								((UserAcc) clientAcc).setAddress(value.toString());
								output.println("Changed field successfully! Change will take effect after return to home screen!");
								
							} else if(tokens[1].equalsIgnoreCase("phoneNumber")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								((UserAcc) clientAcc).setPhoneNumber(value.toString());
								output.println("Changed field successfully! Change will take effect after return to home screen!");
								
							} else if(tokens[1].equalsIgnoreCase("firstName")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								((UserAcc) clientAcc).setFirstName(value.toString());
								output.println("Changed field successfully! Change will take effect after return to home screen!");
								
							} else if(tokens[1].equalsIgnoreCase("lastName")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								((UserAcc) clientAcc).setLastName(value.toString());
								output.println("Changed field successfully! Change will take effect after return to home screen!");
								
							}
						} else {
							output.println("Invalid Input!");
						}
					} else if(line.contentEquals("done")) {
						output.println("Finished editing account.");
						break;
					} else {
						output.println("Invalid Input!");
					}
				} while(!line.contentEquals("done"));
			} else if(line.contentEquals("7")){
				Account.OnlineAccounts.remove(clientAcc);
				System.out.println("[" + new Date() + "] " + clientAcc.getUsername() + " logged out.");
				clientAcc = null;
				return true;
			} else {
				output.println("Invalid option: " + line);
				return false;
			}
			return false;
		} else if(clientAcc instanceof AdminAcc) { ////////////////////////-----    ADMIN SIDE    ------
			ConsoleUI.printHomeScreen(clientAcc, output);

			String line = scanCL.nextLine();

			if(line.contentEquals("1")) {
				ConsoleUI.printProducts(output, null, false);
				handleEdit(line, false);

			} else if(line.contentEquals("2")) {
				ConsoleUI.printCategories(output);

				boolean properCat = false;
				ProductCategory cat = null;
				do {
					try {
						line = scanCL.nextLine();
						cat = ProductCategory.values()[Integer.parseInt(line)];
						properCat = true;
					} catch (NumberFormatException e) {
						output.println("Bad Input!");
					} catch (ArrayIndexOutOfBoundsException e) {
						output.println("No such category!");
					} catch (Exception e) {
						output.println("An Exception occurred! Try again.");
					}
				} while (properCat != true);

				ConsoleUI.printProducts(output, cat, false);
				handleEdit(line, false);

			} else if(line.contentEquals("3")) {
				ConsoleUI.printProducts(output, null, true);
				handleEdit(line, true);

			} else if(line.contentEquals("4")) {
				ConsoleUI.printCategories(output);

				boolean properCat = false;
				ProductCategory cat = null;
				do {
					try {
						line = scanCL.nextLine();
						cat = ProductCategory.values()[Integer.parseInt(line)];
						properCat = true;
					} catch (NumberFormatException e) {
						output.println("Bad Input!");
					} catch (ArrayIndexOutOfBoundsException e) {
						output.println("No such category!");
					} catch (Exception e) {
						output.println("An Exception occurred! Try again.");
					}
				} while (properCat != true);

				ConsoleUI.printProducts(output, cat, true);
				handleEdit(line, true);

			} else if(line.contentEquals("5")) {
				((AdminAcc) clientAcc).createProduct(output, scanCL);

			} else if(line.contentEquals("6")) {

			} else if(line.contentEquals("7")) {
				output.println("Enter desired username[A-Z|a-z|0-9|5-20 characters]\nand password[8-20 characters]: <username> <password> <Id>");

				String[] tokens;

				boolean validate = false;
				do {
					line = scanCL.nextLine();
					tokens = line.split(" ");

					if(tokens.length == 3) {
						boolean taken = false;
						if (AccValidation.ValidateUsername(tokens[0]) && AccValidation.ValidatePassword(tokens[1])) {
							for (Account a : Account.AllAccounts) {
								if(a.getUsername().equalsIgnoreCase(tokens[0])) {
									output.println("This username has already been taken. Try another one.");
									taken = true;
									break;
								}
							}
							validate = true;
						}
						if(validate == true){
							if (taken == false) {
								try {
									AdminAcc a = new AdminAcc(tokens[0], tokens[1], tokens[2]);
									Account.AllAccounts.add(a);
									output.println("Account registered successfully!");
									return false;
								} catch (InvalidUsernameException e) {
									output.println(e.getMessage());
								} catch (InvalidPasswordException e) {
									output.println(e.getMessage());
								} catch (Exception e) {
									output.println("An exception occurred!");
								}
							}
						} else {
							output.println("Invalid username or password! Try again:");
						}
					} else if(line.contentEquals("cancel")) {
						output.println("Creation of administrator account cancelled.");
						return false;
					} else {
						output.println("Invalid Input!");
					}
				} while(!line.contentEquals("cancel"));

			} else if(line.contentEquals("8")) {
				output.println("Type 'done' to finish editing your account.");
				output.println("Type 'set <field> <value>' to edit your account.");
				output.println(clientAcc.toString());
				
				String[] tokens;
				
				do {
					line = scanCL.nextLine();
					tokens = line.split(" ");
					
					if(tokens[0].contentEquals("set")) {
						if(tokens.length >= 3) {
							StringBuilder value = new StringBuilder();
							
							if(tokens[1].equalsIgnoreCase("username")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								try {
									clientAcc.setUsername(value.toString());
									output.println("Changed field successfully! Change will take effect after return to home screen!");
								} catch (InvalidUsernameException e) {
									output.println(e.getMessage());
								}
							} else if(tokens[1].equalsIgnoreCase("password")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								try {
									clientAcc.setPassword(value.toString());
									output.println("Changed field successfully! Change will take effect after return to home screen!");
								} catch (InvalidPasswordException e) {
									output.println(e.getMessage());
								}
							} else if(tokens[1].equalsIgnoreCase("id")) {
								for(int i = 2; i < tokens.length; i++) {
									value.append(tokens[i] + " ");
								}
								
								value.deleteCharAt(value.lastIndexOf(" "));
								
								((AdminAcc) clientAcc).setId(value.toString());
								output.println("Changed field successfully! Change will take effect after return to home screen!");
								
							}
						} else {
							output.println("Invalid Input!");
						}
					} else if(line.contentEquals("done")) {
						output.println("Finished editing account.");
						break;
					} else {
						output.println("Invalid Input!");
					}
				} while(!line.contentEquals("done"));
			} else if(line.contentEquals("9")) {
				Account.OnlineAccounts.remove(clientAcc);
				System.out.println("[" + new Date() + "] " + clientAcc.getUsername() + " logged out.");
				clientAcc = null;
				return true;
			} else {
				output.println("Invalid option: " + line);
				return false;
			}
			return false;
		}
		return false;
	}				////////////////////--------------------- Home Screen finish ---------------------------------

	private void handleEdit(String line, boolean BlackFriday) {
		do {
			output.println("Enter product's number to edit or \"back\" to go back:");

			int option = 0;
			line = scanCL.nextLine();

			if (!line.contentEquals("back")) {
				try {
					if (BlackFriday == false) {
						option = Integer.parseInt(line);
						Product p = Product.AllProducts.get(--option);
						((AdminAcc) clientAcc).editProduct(p, output, scanCL);
						break;

					} else if (BlackFriday == true) {
						option = Integer.parseInt(line);
						Product p = Product.BlackFridayProducts.get(--option);
						((AdminAcc) clientAcc).editProduct(p, output, scanCL);
						break;
					}
				} catch (NumberFormatException e) {
					output.println("Bad Input!");
				} catch (ArrayIndexOutOfBoundsException e) {
					output.println("Invalid option!");
				} catch (Exception e) {
					output.println("An exception occurred when trying to edit the product.");
					output.println(e.getMessage());
				} 
			}
		} while (!line.equalsIgnoreCase("back"));
	}

	private void handleBuy(String line, boolean BlackFriday) {
		do {
			output.println("Enter product's number to buy or \"back\" to go back:");

			int option = 0;
			line = scanCL.nextLine();

			if (!line.contentEquals("back")) {
				try {
					if (BlackFriday == false) {
						option = Integer.parseInt(line);
						Product p = Product.AllProducts.get(--option);
						output.println(p.toString());
						output.println("Enter desired quantity: ");
						line = scanCL.nextLine();
						((UserAcc) clientAcc).buyProduct(p, Integer.parseInt(line));
						output.println("Congratulations! You successfully bought " + line + " of "
								+ p.properties.get("manufacturer") + " " + p.properties.get("model"));
					} else if (BlackFriday == true) {
						option = Integer.parseInt(line);
						Product p = Product.BlackFridayProducts.get(--option);
						output.println(p.toString());
						output.println("Enter desired quantity: ");
						line = scanCL.nextLine();
						((UserAcc) clientAcc).buyProduct(p, Integer.parseInt(line));
						output.println("Congratulations! You successfully bought " + line + " of "
								+ p.properties.get("manufacturer") + " " + p.properties.get("model"));
					}
					
				} catch (NotEnoughQuantityException e) {
					output.println(e.getMessage());
				} catch (NumberFormatException e) {
					output.println("Bad Input!");
				} catch (IndexOutOfBoundsException e) {
					output.println("Invalid option!");
				} catch (Exception e) {
					output.println("Exception occurred when trying to buy the product.");
				} 
			}
		} while (!line.equalsIgnoreCase("back"));
	}

	private boolean handleLoginScreen() throws NoSuchElementException {
		ConsoleUI.printLoginScreen(output);

		String line = scanCL.nextLine();
		String tokens[];

		if(line.contentEquals("1")) {
			output.println("Enter username and password: <username> <password>");
			line = scanCL.nextLine();
			tokens = line.split(" ");

			do {
				if (tokens.length == 2) {
					for (Account acc : Account.AllAccounts) {
						if (acc.getUsername().contentEquals(tokens[0]) && acc.getPassword().contentEquals(tokens[1])) {
							if (!Account.OnlineAccounts.contains(acc)) {
								clientAcc = acc;
								Account.OnlineAccounts.add(clientAcc);
								System.out.println("[" + new Date().toString() + "] " + clientAcc.getUsername() + " logged in.");
								return true;
							} else {
								output.println("This account is already logged in.");
								return false;
							}
						}
					}
					if (clientAcc == null) {
						output.println("No such account found!\nTry again or type \"back\" to go back.");
					} 
				} else {
					output.println("Invalid input! Try again:");
				}
				line = scanCL.nextLine();
				tokens = line.split(" ");
			} while (!line.equalsIgnoreCase("back"));

			return false;

		} else if(line.contentEquals("2")) {
			String[] loginTokens = new String[4];
			output.println("Enter desired username[A-Z|a-z|0-9|5-20 characters]\nand password[8-20 characters]: <username> <password>");

			line = scanCL.nextLine();
			tokens = line.split(" ");

			boolean validate = false;
			do {
				if(tokens.length != 2) {
					output.println("Invalid input! Try again:");
				} else {
					if (AccValidation.ValidateUsername(tokens[0]) && AccValidation.ValidatePassword(tokens[1])) {
						boolean taken = false;
						for (Account a : Account.AllAccounts) {
							if(a.getUsername().equalsIgnoreCase(tokens[0])) {
								output.println("This username has already been taken. Try another one.");
								taken = true;
								break;
							}
						}
						if (taken == false) {
							validate = true;
						}
					} else {
						output.println("Invalid username or password! Try again:");
					}
				}

				line = scanCL.nextLine();
				tokens = line.split(" ");

			} while(validate != true);

			loginTokens[0] = tokens[0];
			loginTokens[1] = tokens[1];

			do {
				output.println("Enter address: ");
			} while ((line = scanCL.nextLine()).contentEquals(""));
			loginTokens[2] = line;

			output.println("Enter phone number: ");
			line = scanCL.nextLine();

			validate = false;
			while(validate != true) {
				if(AccValidation.ValidatePhoneNumber(line)) {
					validate = true;
				} else {
					output.println("Invalid phone number! Try again:");
					line = scanCL.nextLine();
				}
			}

			loginTokens[3] = line;

			try {
				UserAcc u = new UserAcc(loginTokens[0], loginTokens[1], loginTokens[2], loginTokens[3]);
				Account.AllAccounts.add(u);
				output.println("Account registered successfully!");
				return false;
			} catch (InvalidUsernameException e) {
				output.println(e.getMessage());
			} catch (InvalidPasswordException e) {
				output.println(e.getMessage());
			} catch (InvalidPhoneNumberException e) {
				output.println(e.getMessage());
			}
		} else if(line.contentEquals("3")) {
			try {
				output.close();
				scanCL.close();
				clientSocket.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			output.println("Invalid option: " + line);
			return false;
		}
		return false;
	}
}
