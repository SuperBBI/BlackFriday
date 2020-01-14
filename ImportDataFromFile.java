import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class ImportDataFromFile {
	private final static String USERS_FILE = "users.json";
	private final static String ADMINS_FILE = "admins.json";
	private final static String PRODUCTS_FILE = "products.json";

	public static void ImportUsersFromFile() throws FileNotFoundException {
		FileReader fr = new FileReader(new File("src\\" + USERS_FILE).getAbsolutePath());
		BufferedReader br = new BufferedReader(fr);

		Gson GsonObject = new Gson();

		Type accListType = new TypeToken<ArrayList<UserAcc>>(){}.getType();

		ArrayList<UserAcc> accFromFile = GsonObject.fromJson(br, accListType);
		
		for(UserAcc acc : accFromFile) {
			if(acc.getFirstName() != null && acc.getFirstName() != "") {
				try {
					UserAcc u = new UserAcc(acc.getUsername(), acc.getPassword(), acc.getAddress(), acc.getPhoneNumber());
					
					u.setFirstName(acc.getFirstName());
					u.setLastName(acc.getLastName());
					Account.AllAccounts.add(u);
					
				} catch (InvalidUsernameException e) {
					System.out.println(e.getMessage());
				} catch (InvalidPasswordException e) {
					System.out.println(e.getMessage());
				} catch (InvalidPhoneNumberException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					throw e;
				}
			} else {
				try {
					UserAcc u = new UserAcc(acc.getUsername(), acc.getPassword(), acc.getAddress(), acc.getPhoneNumber());
					Account.AllAccounts.add(u);
					
				} catch (InvalidUsernameException e) {
					System.out.println(e.getMessage());
				} catch (InvalidPasswordException e) {
					System.out.println(e.getMessage());
				} catch (InvalidPhoneNumberException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					throw e;
				}
			}
		}
		
		if(br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fr != null) {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void ImportAdminsFromFile() throws FileNotFoundException {
		FileReader fr = new FileReader(new File("src\\" + ADMINS_FILE).getAbsolutePath());
		BufferedReader br = new BufferedReader(fr);

		Gson GsonObject = new Gson();

		Type accListType = new TypeToken<ArrayList<AdminAcc>>(){}.getType();

		ArrayList<AdminAcc> accFromFile = GsonObject.fromJson(br, accListType);
		
		for(AdminAcc acc : accFromFile) {
			try {
				AdminAcc a = new AdminAcc(acc.getUsername(), acc.getPassword(), acc.getId());
				Account.AllAccounts.add(a);
				
			} catch (InvalidUsernameException e) {
				System.out.println(e.getMessage());
			} catch (InvalidPasswordException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				throw e;
			}
		}
		
		if(br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fr != null) {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void ImportProductsFromFile() throws FileNotFoundException {
		FileReader fr = new FileReader(new File("src\\" + PRODUCTS_FILE).getAbsolutePath());
		BufferedReader br = new BufferedReader(fr);
		Gson GsonObject = new Gson();

		Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();

		ArrayList<Product> productsFromFile = GsonObject.fromJson(br, productListType);
		
		Product.AllProducts = productsFromFile;
		
		if(br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fr != null) {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
