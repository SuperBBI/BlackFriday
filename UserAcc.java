import java.util.ArrayList;
import java.util.Date;

public class UserAcc extends Account implements Customer {
	private String address = null;
	private String phoneNumber = null;
	private String firstName = null;
	private String lastName = null;
	
	private ArrayList<String> purchaseHistory = new ArrayList<String>();

	public UserAcc(String username, String password, String address, String phoneNumber) throws InvalidUsernameException, InvalidPasswordException, InvalidPhoneNumberException {
		super(username, password);
		if(!AccValidation.ValidatePhoneNumber(phoneNumber)) {
			throw new InvalidPhoneNumberException("Invalid phone number!");
		}
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getFirstName() {
		if (firstName==null || firstName.contentEquals("")) {
			return "N/A";
		} else {
			return firstName;
		}
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		if (lastName==null || lastName.contentEquals("")) {
			return "N/A";
		} else {
			return lastName;
		}
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void addPurchase(Product p, int quantity) {
		Integer q = quantity;
		Product p1 = new Product(p.getProperties(), p.getCategories()) {
			@Override
			public String toString() {
				int spacing = 2;
				StringBuilder line = new StringBuilder();
				line.append(properties.get("manufacturer") + "  ");
				line.append(properties.get("model") + "\t");
				for(String key : properties.keySet()) {
					if (!key.contentEquals("manufacturer") && !key.contentEquals("model")) {
						if(key.contentEquals("promoPrice")) {
							if(properties.get(key) == null || properties.get(key).contentEquals("0")) {
								line.append(key + ": N/A");
								line.append("  ");
								spacing += 1;
							} else {
								line.append(key + ": " + properties.get(key));
								line.append("  ");
								spacing += 2;
							}
						} else if (key.contentEquals("quantity")) {
							line.append(key + ": " + q.toString());
							line.append("  ");
							spacing += 2;
						} else if (spacing<=4) {
							line.append(key + ": " + properties.get(key));
							line.append("  ");
							spacing += 2;
						} else if (spacing >= 6) {
							line.append("\n");
							spacing = 0;
							line.append(key + ": " + properties.get(key));
							line.append("  ");
							spacing += 2;
						}
					}
				}
				return line.toString();
				
			}
		};
		Product.AllProducts.remove(p1);
		String s = "[" + new Date() + "]\n" + p1.toString();
		purchaseHistory.add(s);
	}

	public String getStringPurchaseHistory() {
		if (purchaseHistory.size() > 0) {
			StringBuilder total = new StringBuilder();
			for (String s : purchaseHistory) {
				total.append(s + "\n");
			}
			return total.toString();
		} else {
			return "N/A";
		}
	}
	
	@Override
	public void buyProduct(Product p, int quantity) throws NotEnoughQuantityException {
		p.setQuantity(quantity);
		addPurchase(p, quantity);
	}
	
	@Override
	public String toString() {
		return "Username: " + super.getUsername() + "\n" +
				"Password: " + super.getPassword() + "\n" +
				"Address: " + address + "\n" +
				"PhoneNumber: " + phoneNumber + "\n" +
				"FirstName: " + getFirstName() + "\n" +
				"LastName: " + getLastName() + "\n";
	}
	
	
}
