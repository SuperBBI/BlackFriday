import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AccValidation {
	private static final String USERNAME_PATTERN = "^[A-Za-z0-9]{5,20}$";
	private static final String PASSWORD_PATTERN = "^[A-Za-z0-9]{8,20}$";
	private static final String PHONE_PATTERN = "^[0-9]{10}$";
	private static Pattern p;
	private static Matcher m;

	public static boolean ValidateUsername(final String username) {
		p = Pattern.compile(USERNAME_PATTERN);
		m = p.matcher(username);

		return m.matches();
	}

	public static boolean ValidatePassword(final String password) {
		p = Pattern.compile(PASSWORD_PATTERN);
		m = p.matcher(password);

		return m.matches();
	}
	
	public static boolean ValidatePhoneNumber(final String phoneNumber) {
		p = Pattern.compile(PHONE_PATTERN);
		m = p.matcher(phoneNumber);

		return m.matches();
	}
	
}