package fundamentals;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTest {

	@Test
	public void test() {
		String timeStr = "01:02:13PM";

		Pattern timePattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2})(AM|PM)");
		Matcher matcher = timePattern.matcher(timeStr);
		System.out.println(matcher.matches());

		String hourStr = matcher.group(1);
		String minuteStr = matcher.group(2);
		String secondStr = matcher.group(3);
		String format = matcher.group(4);

		if (hourStr.equals("12") && format.equals("AM")) {
			hourStr = "00";
		} else if (format.equals("PM")) {
			hourStr = String.valueOf(Integer.valueOf(hourStr) + 12);
		}

		System.out.println(hourStr + ":" + minuteStr + ":" + secondStr);
	}

}
