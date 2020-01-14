import java.util.ArrayList;

public abstract class Account {
	public static ArrayList<Account> AllAccounts = new ArrayList<Account>();
	public static ArrayList<Account> OnlineAccounts = new ArrayList<Account>();
	
	private String username;
	private String password;
	
	public Account(String username, String password) throws InvalidUsernameException, InvalidPasswordException, NullPointerException {
		if(!AccValidation.ValidateUsername(username)) {
			throw new InvalidUsernameException("Invalid username!");
		}else if(!AccValidation.ValidatePassword(password)) {
			throw new InvalidPasswordException("Invalid password!");
		}
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) throws InvalidUsernameException {
		if(!AccValidation.ValidateUsername(username)) {
			throw new InvalidUsernameException("Invalid username!");
		}
		this.username = username;
	}

	public void setPassword(String password) throws InvalidPasswordException {
		if(!AccValidation.ValidatePassword(password)) {
			throw new InvalidPasswordException("Invalid password!");
		}
		this.password = password;
	}
	
}
