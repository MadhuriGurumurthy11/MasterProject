import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JunitExchangePairs {

	private String str;
	private String expected;

	public JunitExchangePairs(String str, String expected) {
		this.expected = expected;
		this.str = str;
	}

	@Parameters
	public static Collection<Object[]> exchangePairs() {
		Object[][] i = prepareData();
		return Arrays.asList(i);
	}

	/**
	 * Test method for {@link TestSumArray#runSum()}.
	 * 
	 * Input size is 500 and whole numbers
	 */
	public static Object[][] prepareData() {

		File file = new File(
				"C:/Users/Madhuri/TestCases/Testing/exchangePairs.properties");

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(fis);
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String strToBetested = null;
		@SuppressWarnings("rawtypes")
		Enumeration ekeys = props.keys();
		Object[][] dataToBeTested = new Object[props.size()][2];
		int j = -1;
		while (ekeys.hasMoreElements()) {
			++j;
			String key = (String) ekeys.nextElement();
			String value = props.getProperty(key);

			String[] values = value.split(":");

			strToBetested = values[0];

			dataToBeTested[j][0] = strToBetested;
			dataToBeTested[j][1] = values[1];

		}

		return dataToBeTested;

	}

	@Test
	public void testForExchangePairs() {
		// Addition add = new Addition();
		Recursion2 recursion = new Recursion2();

		assertEquals(expected, recursion.exchangePairs(str));
	}
}