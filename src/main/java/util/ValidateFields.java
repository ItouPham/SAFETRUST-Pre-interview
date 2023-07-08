package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateFields {
	public static boolean checkFormatEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean checkFormatPhoneNumber(String phoneNumber) {
		String regex = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}
}
