package live.learnjava.usermanagement.utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Password {
	public static String generateRandomPassword() {
		PasswordGenerator passwordGenerator = new PasswordGenerator();

		/* @formatter:off */
		String password = passwordGenerator.generatePassword(8, 
				new CharacterRule(EnglishCharacterData.LowerCase),
				new CharacterRule(EnglishCharacterData.UpperCase), 
				new CharacterRule(EnglishCharacterData.Digit));
		/* @formatter:on */
		return password;
	}
}
